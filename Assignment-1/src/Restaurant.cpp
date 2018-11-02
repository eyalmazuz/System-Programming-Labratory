//
// Created by eyal on 10/31/18.
//

#include "../include/Restaurant.h"
#include<fstream>
#include <sstream>
#include <iterator>
#include <regex>

//constructors

Restaurant::Restaurant() { }

Restaurant::Restaurant(const std::string &configFilePath) {
    readFile(configFilePath);
}

Restaurant::Restaurant(Restaurant &&other) {
    steal(other);
}

Restaurant& Restaurant::operator=(Restaurant &&other) {
    if(this != &other){
        clean();
        steal(other);
    }
    return *this;
}
Restaurant::Restaurant(Restaurant &other) {
    copy(other);
}

Restaurant& Restaurant::operator=(Restaurant &other) {
    if(this != &other){
        clean();
        copy(other);
    }
    return *this;
}

//destructor

Restaurant::~Restaurant() {
    clean();
}



Table* Restaurant::getTable(int ind) { return tables[ind]; }

int Restaurant::getNumOfTables() const { return tables.size(); }

std::vector<Dish>& Restaurant::getMenu() { return menu; }

const std::vector<BaseAction*>& Restaurant::getActionsLog() const { return actionsLog; }


void Restaurant::start() {
    std::string closeAll = "closeall";
    BaseAction *Action;
    std::cout << "Restaurant is now open!" <<std::endl;
    std::string input = "";

    while(input!= closeAll){
        input = "";
        std::getline(std::cin, input);
        std::istringstream iss(input);
        std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                        std::istream_iterator<std::string>{}};
        if(tokens[0] == "open"){
            if(checkOpenValid(tokens)){
                int tableId = std::stoi(tokens[1]);
                std::vector<Customer *> Customers;
                std::smatch match;
                std::regex re ("(?!.*[0-9])\\S.*");
                std::regex_search(input, match, re);
                std::istringstream iss(match[0]);
                std::vector<std::string> cust{std::istream_iterator<std::string>{iss},
                                                std::istream_iterator<std::string>{}};
                for (int i = 0; i < cust.size(); ++i) {
                    std::stringstream ss(cust[i]);
                    std::vector<std::string> data;
                    while(std::getline(ss, cust[i], ',')){
                        data.push_back(cust[i]);
                    }
                    if(data[1] == "chp"){
                        Customer *c = new CheapCustomer(data[0], i+1);
                        Customers.push_back(c);
                    }else if(data[1] == "alc"){
                        Customer *c = new AlchoholicCustomer(data[0], i+1);
                        Customers.push_back(c);
                    }else if(data[1] == "spc"){
                        Customer *c = new SpicyCustomer(data[0], i+1);
                        Customers.push_back(c);
                    }else if(data[1] == "veg"){
                        Customer*c = new VegetarianCustomer(data[0], i+1);
                        Customers.push_back(c);
                    }
                }
                Action = new OpenTable(tableId, Customers);
                Action->act(*this);
                delete Action;

            }
        }else if(tokens[0] == "order"){

        }else if(tokens[0] == "move"){

        }else if(tokens[0] == "close"){
            delete this;

        }else if(tokens[0] == "closeall"){
            std::cout<< "close all" << std::endl;

        }else if(tokens[0] == "menu"){

        }else if(tokens[0] == "status"){

        }else if(tokens[0] == "log"){

        }else if(tokens[0] == "backup"){

        }else if(tokens[0] == "restore"){

        }else{

        }

        if(Action == nullptr){
            delete Action;
        }

    }
}

//helper functions

void Restaurant::readFile(const std::string &configFilePath) {

    std::ifstream myReadFile;
    std::string line;
    int numberOfTables;
    myReadFile.open(configFilePath);
    if(myReadFile.is_open()){
        while(! myReadFile.eof()){
            getline(myReadFile, line); // Saves the line in STRING.
            if(line.find("#number") == 0){
                getline(myReadFile, line);
                numberOfTables = std::stoi(line);
                std::vector<Table*> tables(numberOfTables);
            }else if(line.find("#tables") == 0){
                getline(myReadFile, line);
                std::vector<int> vect;
                std::stringstream ss(line);
                int i;

                while (ss >> i)
                {
                    vect.push_back(i);

                    if (ss.peek() == ',')
                        ss.ignore();
                }
                for(int i = 0; i < numberOfTables; i++){
                    Table *t = new Table(vect[i]);
                    tables.push_back(t);
                }

            }
            else if(line.find("#Menu") == 0){
                int index = 0;
                while(!myReadFile.eof()){
                    getline(myReadFile, line);
                    std::stringstream ss(line);
                    std::vector<std::string> vect;
                    while(std::getline(ss, line, ',')){
                        vect.push_back(line);
                    }
                    int price = std::stoi(vect[2]);
                    menu.push_back(Dish(index, vect[0], price, convert(vect[1])));
                    index++;
                }
            }
        }
    }
    myReadFile.close();
}

DishType Restaurant::convert(std::string type) {
    if(type == "ALC") return ALC;
    else if(type == "BVG") return BVG;
    else if(type == "VEG") return VEG;
    else if(type == "SPC") return SPC;
}

void Restaurant::clean() {
    std::vector<Table*>::iterator tablesIterator;
    for(tablesIterator = tables.begin(); tablesIterator != tables.end(); ++tablesIterator) {
        delete (*tablesIterator);
    }
    std::vector<BaseAction*>::iterator actionIterator;
    for(actionIterator = actionsLog.begin(); actionIterator !=actionsLog.end(); ++actionIterator){
        delete(*actionIterator);
    }
}

void Restaurant::copy(Restaurant &other){
    for(int i = 0; i < other.getMenu().size(); i++){
        menu.push_back(other.getMenu()[i]);
    }
    for(int i = 0; i < other.getNumOfTables(); i++){
        tables.push_back(new Table(*other.tables[i]));
    }
    open = other.open;
    for(int i = 0; i < other.getActionsLog().size(); i++){
        actionsLog.push_back(other.actionsLog[i]);
    }
}

void Restaurant::steal(Restaurant &other) {
    tables = other.tables;
    for(int i = 0; i < other.menu.size(); i++){
        menu.push_back(other.menu[i]);
    }
    actionsLog = other.actionsLog;
    open = other.open;

    other.actionsLog.clear();
    other.tables.clear();
    other.menu.clear();
}

bool Restaurant::checkOpenValid(std::vector<std::string> tokens) {
    if(tokens.size() < 3){
        std::cout<<"not valid open command" << std::endl;
        return false;
    }
    if(tables[std::stoi(tokens[1])]->isOpen() == true){
        std::cout << "Table does not exist or already Open" <<std::endl;
        return false;
    }
    if(tables[std::stoi(tokens[1])]== nullptr){
        std::cout << "Table does not exist or already Open"<< std::endl;
        return false;
    }
    return true;

}

void Restaurant::execute(OpenTable &action) {
    this->tables[action.getId()-1]->openTable();
    for (int i = 0; i < action.getCustomers().size(); ++i) {
        this->tables[action.getId()-1]->addCustomer(action.getCustomers()[i]);

    }
}