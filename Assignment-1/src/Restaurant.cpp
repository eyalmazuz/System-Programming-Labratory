//
// Created by eyal on 10/31/18.
//

#include "../include/Restaurant.h"
#include<fstream>
#include <sstream>
#include <iterator>
#include <regex>
//constructors

Restaurant::Restaurant(): open(true), custIndex(0){ }

Restaurant::Restaurant(const std::string &configFilePath): open(true), custIndex(0){
    readFile(configFilePath);
}

Restaurant::Restaurant(Restaurant &&other) {
    steal(other);
}

Restaurant& Restaurant::operator=(Restaurant &&other) {
    if(&other!= this){
        clean();
        steal(other);
    }
    return *this;
}

Restaurant::Restaurant(Restaurant &other) {
    copy(other);
}

Restaurant& Restaurant::operator=(Restaurant &other) {
    if(&other!= this){
        clean();
        copy(other);
    }
    return *this;
}

//destructor

Restaurant::~Restaurant() {
    clean();
}

//getters

Table* Restaurant::getTable(int ind) { return tables[ind]; }

std::vector<Table*> Restaurant::getTables() const {return tables;}

int Restaurant::getNumOfTables() const { return tables.size(); }

std::vector<Dish>& Restaurant::getMenu() { return menu; }

const std::vector<BaseAction*>& Restaurant::getActionsLog() const { return actionsLog; }

void Restaurant::start() {
    std::string closeAll = "closeall";
    BaseAction *Action;
    std::cout << "Restaurant is now open! " << std::endl;
    std::string input = "";
    while (input != closeAll) {
        std::getline(std::cin, input);
        if (checkValidInput(input)) {
            std::istringstream iss(input);
            std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                            std::istream_iterator<std::string>{}};
            if (checkValidCommand(tokens)) {
                if (tokens[0] == "open") {
                    openCommand(tokens, Action);
                }
                else if (tokens[0] == "order") {
                    orderCommand(tokens, Action);

                } else if (tokens[0] == "move") {
                   moveCommand(tokens, Action);

                } else if (tokens[0] == "close") {
                    closeCommand(tokens, Action);

                } else if (tokens[0] == "closeall") {
                    closeallCommand(tokens, Action);

                } else if (tokens[0] == "menu") {
                    printMenuCommand(tokens, Action);

                } else if (tokens[0] == "status") {
                    printTableStatusCommand(tokens, Action);

                } else if (tokens[0] == "log") {
                    printLogCommand(tokens, Action);

                } else if (tokens[0] == "backup") {
                    backupCommand(tokens, Action);

                } else if (tokens[0] == "restore") {
                    restoreCommand(tokens, Action);

                }
            } else {
                std::cout << "Invalid Command" << std::endl;
            }

        } else {
            std::cout << "Invalid Command" << std::endl;
        }
    }

}

//helper functions

void Restaurant::readFile(const std::string &configFilePath) {
    std::ifstream myReadFile;
    std::string line;
    myReadFile.open(configFilePath);
    if (myReadFile.is_open()) {
        while (!myReadFile.eof()) {
            getline(myReadFile, line); // Saves the line in STRING.
            if (line.find("#number") == 0) {
                readNumOfTables(myReadFile);
            } else if (line.find("#tables") == 0) {
                readTables(myReadFile);

            } else if (line.find("#Menu") == 0) {
                readMenu(myReadFile);
            }
        }
    }
    myReadFile.close();
}

void Restaurant::readNumOfTables(std::ifstream &myReadFile) {
    std::string line = "";
    getline(myReadFile, line);
    int numOfTables = std::stoi(line);

}

void Restaurant::readTables(std::ifstream &myReadFile) {
    std::string line = "";
    getline(myReadFile, line);
    std::vector<int> vect;
    std::stringstream ss(line);
    while (std::getline(ss, line, ',')) {
        tables.push_back(new Table(std::stoi(line)));
    }

}

void Restaurant::readMenu(std::ifstream &myReadFile) {
    int index = 0;
    while (!myReadFile.eof()) {
        std::string line = "";
        getline(myReadFile, line);
        if (line != "") {
            std::stringstream ss(line);
            std::vector<std::string> vect;
            while (std::getline(ss, line, ',')) {
                vect.push_back(line);
            }
            int price = std::stoi(vect[2]);
            menu.push_back(Dish(index, vect[0], price, convert(vect[1])));
            index++;
        }
    }
}

DishType Restaurant::convert(std::string type) {
    if (type == "ALC") return ALC;
    else if (type == "BVG") return BVG;
    else if (type == "VEG") return VEG;
    else if (type == "SPC") return SPC;
}



//rule of 5 helper methods

void Restaurant::clean() {
    std::vector<Table *>::iterator tablesIterator;
    for (tablesIterator = tables.begin(); tablesIterator != tables.end(); ++tablesIterator) {
        delete (*tablesIterator);
    }
    std::vector<BaseAction *>::iterator actionIterator;
    for (actionIterator = actionsLog.begin(); actionIterator != actionsLog.end(); ++actionIterator) {
        delete (*actionIterator);
    }
    menu.clear();
    tables.shrink_to_fit();
    actionsLog.shrink_to_fit();
    menu.shrink_to_fit();
}

void Restaurant::copy(Restaurant &other) {
    for (int i = 0; i < other.getMenu().size(); i++) {
        menu.push_back(other.getMenu()[i]);
    }
    for (int i = 0; i < other.getNumOfTables(); i++) {
        tables.push_back(new Table(*other.tables[i]));
    }
    open = other.open;
    for (int i = 0; i < other.getActionsLog().size(); i++) {
        //TODO fix copy C'tor actionlog copy
        //actionsLog.push_back();
 /*       if (typeid(*other.getActionsLog()[i]) == typeid(OpenTable)){
            actionsLog.push_back(new OpenTable())
        }*/
    }
}

void Restaurant::steal(Restaurant &other) {
    tables = other.tables;
    for (int i = 0; i < other.menu.size(); i++) {
        menu.push_back(other.menu[i]);
    }
    actionsLog = other.actionsLog;
    open = other.open;

    other.actionsLog.clear();
    other.tables.clear();
    other.menu.clear();
}

//commands methods

bool Restaurant::checkValidInput(std::string input) {
    if (input == "" || input == " ") {
        return false;
    }
    return true;
}

bool Restaurant::checkValidCommand(std::vector<std::string> tokens) {
    std::string command = tokens[0];
    if (command != "open" && command != "order" && command != "move" && command != "close" && command != "log" &&
        command != "status" && command != "closeall" && command != "menu" && command != "backup" &&
        command != "restore") {
        return false;
    }
    return true;
}

void Restaurant::openCommand(std::vector<std::string> tokens, BaseAction *Action) {
    int tableId = std::stoi(tokens[1]) - 1;
    std::vector<Customer *> Customers;
    if (checkOpenValid(tokens, *tables[tableId])) {
        int tableSize = tables[tableId]->getCapacity();
        delete tables[tableId];
        tables[tableId] = new Table(tableSize);
        for (int i = 2; i < tokens.size(); ++i) {
            std::stringstream ss(tokens[i]);
            std::vector<std::string> data;
            while (std::getline(ss, tokens[i], ',')) {
                data.push_back(tokens[i]);
            }
            if (data[1] == "chp") {
                Customers.push_back(new CheapCustomer(data[0], custIndex));
                custIndex++;
            } else if (data[1] == "alc") {
                Customers.push_back(new AlchoholicCustomer(data[0], custIndex));
                custIndex++;
            } else if (data[1] == "spc") {
                Customers.push_back(new SpicyCustomer(data[0], custIndex));
                custIndex++;
            } else if (data[1] == "veg") {
                Customers.push_back(new VegetarianCustomer(data[0], custIndex));
                custIndex++;
            }
        }

        Action = new OpenTable(tableId, Customers);
        actionsLog.push_back(Action);
        Action->act(*this);
    } else {
        Action = new OpenTable(tableId, Customers);
        Action->setError();
        actionsLog.push_back(Action);
    }
}

bool Restaurant::checkOpenValid(std::vector<std::string> tokens, Table &table) {
    if(!std::stoi(tokens[1])){
        return false;
    }
    if(std::stoi(tokens[1])> tables.size() || table.isOpen()){
        return false;
    }
    return true;
}

void Restaurant::orderCommand(std::vector<std::string> tokens, BaseAction *Action) {
    if(checkOrderValid(tokens[1])) {
        int tableId = std::stoi(tokens[1]) - 1;
        Action = new Order(tableId);
        Action->act(*this);
        actionsLog.push_back(Action);
    }else{
        Action = new Order(std::stoi(tokens[1])-1);
        Action->setError();
        actionsLog.push_back(Action);
    }
}

bool Restaurant::checkOrderValid(std::string index) {
    if(!std::stoi(index)){
        return false;
    }
    if(std::stoi(index)> tables.size()){
        return false;
    }
    return true;
}

void Restaurant::moveCommand(std::vector<std::string> tokens, BaseAction *Action) {
    if(checkMoveValid(tokens)) {
        Action = new MoveCustomer(std::stoi(tokens[1]) - 1, std::stoi(tokens[2]) - 1,
                                  std::stoi(tokens[3]));
        Action->act(*this);
        actionsLog.push_back(Action);
    }else{
        Action = new MoveCustomer(std::stoi(tokens[1]) - 1, std::stoi(tokens[2]) - 1,
                                  std::stoi(tokens[3]));
        Action->setError();
        actionsLog.push_back(Action);
    }

}

bool Restaurant::checkMoveValid(std::vector<std::string> tokens) {
/*    if(!(std::stoi(tokens[1])-1) && !(std::stoi(tokens[2])-1) &&
            !(std::stoi(tokens[3]))){
        return false;
    }*/
    if(std::stoi(tokens[1])-1 > tables.size() || std::stoi(tokens[2])-1 > tables.size()){
        return false;
    }
    return true;
}

void Restaurant::closeCommand(std::vector<std::string> tokens, BaseAction *Action) {
    if(checkCloseValid(tokens[1])){
        Action = new Close(std::stoi(tokens[1]) - 1);
        Action->act(*this);
        actionsLog.push_back(Action);
    }
}

bool Restaurant::checkCloseValid(std::string id) {
    if(!std::stoi(id)){
        return false;
    }
    if(std::stoi(id)> tables.size() || !tables[std::stoi(id)-1]->isOpen()){
        return false;
    }
    return true;
}

void Restaurant::printTableStatusCommand(std::vector<std::string> tokens, BaseAction *Action){
    if(checkCloseValid(tokens[1])){
        Action = new PrintTableStatus(std::stoi(tokens[1]) - 1);
        Action->act(*this);
        actionsLog.push_back(Action);
    }
}

void Restaurant::printLogCommand(std::vector<std::string> tokens, BaseAction *Action) {
    Action = new PrintActionsLog();
    Action->act(*this);
    delete Action;
}

void Restaurant::closeallCommand(std::vector<std::string> tokens, BaseAction *Action) {
    Action = new CloseAll();
    Action->act(*this);
    delete Action;
}

void Restaurant::printMenuCommand(std::vector<std::string> tokens, BaseAction *Action) {
    Action = new PrintMenu();
    Action->act(*this);
}

void Restaurant::backupCommand(std::vector<std::string> tokens, BaseAction *Action) {
    Action = new BackupRestaurant();
    Action->act(*this);
    actionsLog.push_back(Action);
}

void Restaurant::restoreCommand(std::vector<std::string> tokens, BaseAction *Action) {
    Action = new RestoreResturant();
    Action->act(*this);
    actionsLog.push_back(Action);
}