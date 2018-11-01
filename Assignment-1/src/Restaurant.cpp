//
// Created by eyal on 10/31/18.
//

#include "../include/Restaurant.h"
#include<fstream>
#include <sstream>

Restaurant::Restaurant() { }
Restaurant::Restaurant(const std::string &configFilePath) {
    readFile(configFilePath);
}

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
                    tables.push_back(new Table(vect[i]));
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
            std::cout<< line << std::endl; // Prints our STRING.
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
Restaurant::~Restaurant() {
    clean();
}

void Restaurant::steal(Restaurant &other) {
    tables = other.tables;
    for(int i = 0; i < other.getMenu().size(); i++){
        menu.push_back(other.getMenu()[i]);
    }
    actionsLog = other.actionsLog;
    open = other.open;

    //TODO
    other.actionsLog.clear();
    other.tables.clear();
    other.menu.clear();
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


int Restaurant::getNumOfTables() const { return tables.size(); }
std::vector<Dish>& Restaurant::getMenu() { return menu; }
const std::vector<BaseAction*>& Restaurant::getActionsLog() const { return actionsLog; }

void Restaurant::start() {
    std::string closeAll = "closeall";

    std::string Action;
    std::cin >> Action;
    std::cout << "Restaurant is now open!" <<std::endl;

    while(Action != closeAll){

    }
}