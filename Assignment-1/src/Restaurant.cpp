//
// Created by eyal on 10/31/18.
//

#include "../include/Restaurant.h"
#include<fstream>
#include <sstream>
#include <iterator>
#include <regex>
//constructors

Restaurant::Restaurant(): open(true), tables{},  menu{}, actionsLog{}, custIndex(0){ }

Restaurant::Restaurant(const std::string &configFilePath): open(true), tables{},  menu{}, actionsLog{}, custIndex(0){
    readFile(configFilePath);
}

Restaurant::Restaurant(Restaurant &&other):open(other.open), tables{},  menu{}, actionsLog{}, custIndex(other.custIndex) {
    steal(other);
}

Restaurant& Restaurant::operator=(Restaurant &&other) {
    if(&other!= this){
        clean();
        steal(other);
    }
    return *this;
}

Restaurant::Restaurant(Restaurant &other):open(other.open), tables{},  menu{}, actionsLog{},  custIndex(other.custIndex) {
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

int Restaurant::getNumOfTables() const { return static_cast<int>(tables.size()); }

std::vector<Dish>& Restaurant::getMenu() { return menu; }

const std::vector<BaseAction*>& Restaurant::getActionsLog() const { return actionsLog; }

void Restaurant::start() {
    std::string closeAll = "closeall";
    std::cout << "Restaurant is now open! " << std::endl;
    std::string input;
    while (input != closeAll) {
        std::getline(std::cin, input);
        if (checkValidInput(input)) {
            std::istringstream iss(input);
            std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                            std::istream_iterator<std::string>{}};
            if (checkValidCommand(tokens)) {
                if (tokens[0] == "open") {
                    openCommand(tokens);
                }
                else if (tokens[0] == "order") {
                    orderCommand(tokens);

                } else if (tokens[0] == "move") {
                    moveCommand(tokens);

                } else if (tokens[0] == "close") {
                    closeCommand(tokens);

                } else if (tokens[0] == "closeall") {
                    closeallCommand(tokens);

                } else if (tokens[0] == "menu") {
                    printMenuCommand(tokens);

                } else if (tokens[0] == "status") {
                    printTableStatusCommand(tokens);

                } else if (tokens[0] == "log") {
                    printLogCommand(tokens);

                } else if (tokens[0] == "backup") {
                    backupCommand(tokens);

                } else if (tokens[0] == "restore") {
                    restoreCommand(tokens);

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
            }else{

            }
        }
    }
    myReadFile.close();
}

void Restaurant::readNumOfTables(std::ifstream &myReadFile) {
    std::string line;
    getline(myReadFile, line);
}

void Restaurant::readTables(std::ifstream &myReadFile) {
    std::string line;
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
        std::string line;
        getline(myReadFile, line);
        if (!line.empty()) {
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
    DishType dish;
    if (type == "ALC") dish =  ALC;
    else if (type == "BVG") dish =  BVG;
    else if (type == "VEG") dish =  VEG;
    else if (type == "SPC") dish = SPC;
    return dish;
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
    actionsLog.clear();
    tables.clear();
}

void Restaurant::copy(Restaurant &other) {
    std::vector<Dish>::iterator itMenu;
    for (itMenu = other.menu.begin(); itMenu != other.menu.end(); ++itMenu) {
        menu.push_back(*itMenu);
    }
    std::vector<Table*>::iterator itTable;
    for (itTable = other.tables.begin(); itTable != other.tables.end(); ++itTable) {
        tables.push_back(new Table(*(*itTable)));
    }
    open = other.open;
    std::vector<BaseAction*>::const_iterator it;
    for (it = other.actionsLog.begin(); it != other.actionsLog.end(); ++it) {
        if (typeid(*(*it)) == typeid(OpenTable)){
            auto *tmp = dynamic_cast<OpenTable*>((*it));
            int tableId = tmp->getTableId();
            std::vector<Customer*> customers = dynamic_cast<OpenTable*>((*it))->getCustomers();
            actionsLog.push_back(new OpenTable(tableId, customers));
        }else if(typeid(*(*it)) == typeid(Order)){
            int tableId = dynamic_cast<Order*>((*it))->getTableId();
            actionsLog.push_back(new Order(tableId));

        }else if(typeid(*(*it)) == typeid(MoveCustomer)){
            int id = dynamic_cast<MoveCustomer*>((*it))->getId();
            int srcTable = dynamic_cast<MoveCustomer*>((*it))->getSrcTable();
            int dstTable = dynamic_cast<MoveCustomer*>((*it))->getDstTable();
            actionsLog.push_back(new MoveCustomer(srcTable,dstTable,id));

        }else if(typeid(*(*it)) == typeid(PrintTableStatus)){
            int tableId = dynamic_cast<PrintTableStatus*>((*it))->getTableId();
            actionsLog.push_back(new PrintTableStatus(tableId));

        }else if(typeid(*(*it)) == typeid(PrintMenu)){
            actionsLog.push_back(new PrintMenu());

        }else if(typeid(*(*it)) == typeid(Close)){
            int tableId = dynamic_cast<Close*>((*it))->getTableId();
            actionsLog.push_back(new Close(tableId));

        }else if(typeid(*(*it)) == typeid(PrintActionsLog)){
            actionsLog.push_back(new PrintActionsLog());

        }else if(typeid(*(*it)) == typeid(BackupRestaurant)){
            actionsLog.push_back(new BackupRestaurant());

        }else if(typeid(*(*it)) == typeid(RestoreResturant)){
            actionsLog.push_back(new RestoreResturant());

        }else if(typeid(*(*it)) == typeid(CloseAll)){
            actionsLog.push_back(new CloseAll());

        }

        actionsLog.back()->setLogger((*it)->getLogger());
    }
}

void Restaurant::steal(Restaurant &other) {
    tables = other.tables;
    std::vector<Dish>::iterator itMenu;
    for (itMenu = other.menu.begin(); itMenu != other.menu.end(); ++itMenu) {
        menu.push_back(*itMenu);
    }
    actionsLog = other.actionsLog;
    open = other.open;

    other.actionsLog.clear();
    other.tables.clear();
    other.menu.clear();
}

//commands methods

bool Restaurant::checkValidInput(std::string input) {
    return !(input.empty() || input == " ");
}

bool Restaurant::checkValidCommand(std::vector<std::string> tokens) {
    std::string command = tokens[0];
    return !(command != "open" && command != "order" && command != "move" && command != "close" && command != "log" &&
             command != "status" && command != "closeall" && command != "menu" && command != "backup" &&
             command != "restore");
}

void Restaurant::openCommand(std::vector<std::string> tokens) {
    int tableId = std::stoi(tokens[1]) - 1;
    std::vector<Customer *> Customers;
    if (checkOpenValid(tokens, *tables[tableId])) {
        int tableSize = tables[tableId]->getCapacity();
        delete tables[tableId];
        tables[tableId] = new Table(tableSize);
        for (unsigned int i = 2; i < tokens.size(); ++i) {
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

        auto *Action = new OpenTable(tableId, Customers);
        actionsLog.push_back(Action);
        Action->act(*this);
    } else {
        auto * Action = new OpenTable(tableId, Customers);
        Action->setError();
        actionsLog.push_back(Action);
    }
}

bool Restaurant::checkOpenValid(std::vector<std::string> tokens, Table &table) {
    if(!std::stoi(tokens[1])){
        return false;
    }
    if(std::stoi(tokens[1])> getNumOfTables() || table.isOpen()){
        return false;
    }
    return true;
}

void Restaurant::orderCommand(std::vector<std::string> tokens) {
    if(checkOrderValid(tokens[1])) {
        int tableId = std::stoi(tokens[1]) - 1;
        auto *Action = new Order(tableId);
        Action->act(*this);
        actionsLog.push_back(Action);
    }else{
        auto *Action = new Order(std::stoi(tokens[1])-1);
        Action->setError();
        actionsLog.push_back(Action);
    }
}

bool Restaurant::checkOrderValid(std::string index) {
    if(!std::stoi(index)){
        return false;
    }
    return std::stoi(index) <= getNumOfTables();
}

void Restaurant::moveCommand(std::vector<std::string> tokens) {
    if(checkMoveValid(tokens)) {
        auto *Action = new MoveCustomer(std::stoi(tokens[1]) - 1, std::stoi(tokens[2]) - 1,
                                        std::stoi(tokens[3]));
        Action->act(*this);
        actionsLog.push_back(Action);
    }else{
        auto *Action = new MoveCustomer(std::stoi(tokens[1]) - 1, std::stoi(tokens[2]) - 1,
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
    return !(std::stoi(tokens[1]) - 1 > getNumOfTables() || std::stoi(tokens[2]) - 1 > getNumOfTables());
}

void Restaurant::closeCommand(std::vector<std::string> tokens) {
    if(checkCloseValid(tokens[1])){
        auto *Action = new Close(std::stoi(tokens[1]) - 1);
        Action->act(*this);
        actionsLog.push_back(Action);
    }
}

bool Restaurant::checkCloseValid(std::string id) {
    if(!std::stoi(id)){
        return false;
    }
    return !(std::stoi(id) > getNumOfTables());
}

void Restaurant::printTableStatusCommand(std::vector<std::string> tokens){
    if(checkCloseValid(tokens[1])){
        auto *Action = new PrintTableStatus(std::stoi(tokens[1]) - 1);
        Action->act(*this);
        actionsLog.push_back(Action);
    }
}

void Restaurant::printLogCommand(std::vector<std::string> tokens) {
    auto *Action = new PrintActionsLog();
    Action->act(*this);
    actionsLog.push_back(Action);
}

void Restaurant::closeallCommand(std::vector<std::string> tokens) {
    auto *Action = new CloseAll();
    Action->act(*this);
    actionsLog.push_back(Action);
}

void Restaurant::printMenuCommand(std::vector<std::string> tokens) {
    auto *Action = new PrintMenu();
    Action->act(*this);
    actionsLog.push_back(Action);

}

void Restaurant::backupCommand(std::vector<std::string> tokens) {
    auto *Action = new BackupRestaurant();
    Action->act(*this);
    actionsLog.push_back(Action);
}

void Restaurant::restoreCommand(std::vector<std::string> tokens) {
    auto *Action = new RestoreResturant();
    Action->act(*this);
    actionsLog.push_back(Action);
}