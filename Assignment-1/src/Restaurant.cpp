//
// Created by eyal on 10/31/18.
//

#include "../include/Restaurant.h"
#include<fstream>
#include <sstream>
#include <iterator>
#include <regex>
extern Restaurant* backup;
//constructors

Restaurant::Restaurant() { }

Restaurant::Restaurant(const std::string &configFilePath){
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
                while(std::getline(ss, line, ',')){
                    vect.push_back(std::stoi(line));
                }
                for(int i = 0; i < vect.size(); i++){
                    Table *t = new Table(vect[i]);
                    tables.push_back(t);
                }

            }
            else if(line.find("#Menu") == 0){
                int index = 0;
                while(!myReadFile.eof() && line != ""){
                    getline(myReadFile, line);
                    if(line != "") {
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
        }
    }
    myReadFile.close();
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
    int custIndex = 0;
    std::string closeAll = "closeall";
    BaseAction *Action;
    std::cout << "Restaurant is now open!" <<std::endl;
    std::string input = "";
    while(input!= closeAll){
        input = "";
        std::getline(std::cin, input);
        std::cout<< input << std::endl;
        std::istringstream iss(input);
        std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                        std::istream_iterator<std::string>{}};
        if(tokens[0] == "open"){
            if(checkOpenValid(tokens, *tables[std::stoi(tokens[1])-1])){
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
                        Customer *c = new CheapCustomer(data[0], custIndex);
                        Customers.push_back(c);
                        custIndex++;
                    }else if(data[1] == "alc"){
                        Customer *c = new AlchoholicCustomer(data[0], custIndex);
                        Customers.push_back(c);
                        custIndex++;
                    }else if(data[1] == "spc"){
                        Customer *c = new SpicyCustomer(data[0], custIndex);
                        Customers.push_back(c);
                        custIndex++;
                    }else if(data[1] == "veg"){
                        Customer*c = new VegetarianCustomer(data[0], custIndex);
                        Customers.push_back(c);
                        custIndex++;
                    }
                }
                Action = new OpenTable(tableId, Customers);
                actionsLog.push_back(Action);
                Action->act(*this);

            }
        }else if(tokens[0] == "order"){
            Action = new Order(std::stoi(tokens[1])-1);
            Action->act(*this);
            actionsLog.push_back(Action);

        }else if(tokens[0] == "move"){
            Action = new MoveCustomer(std::stoi(tokens[1])-1, std::stoi(tokens[2])-1, std::stoi(tokens[3]));
            Action->act(*this);
            actionsLog.push_back(Action);

        }else if(tokens[0] == "close"){
            Action = new Close(std::stoi(tokens[1])-1);
            Action->act(*this);
            actionsLog.push_back(Action);

        }else if(tokens[0] == "closeall"){
            Action = new CloseAll();
            Action->act(*this);
            delete Action;
            clean();

        }else if(tokens[0] == "menu"){
            Action = new PrintMenu();
            Action->act(*this);

        }else if(tokens[0] == "status"){
            Action = new PrintTableStatus(std::stoi(tokens[1])-1);
            Action->act(*this);
            actionsLog.push_back(Action);

        }else if(tokens[0] == "log"){

        }else if(tokens[0] == "backup"){
            Action = new BackupRestaurant();
            Action->act(*this);
            actionsLog.push_back(Action);

        }else if(tokens[0] == "restore"){
            Action = new RestoreResturant();
            Action->act(*this);
            actionsLog.push_back(Action);

        }else{

        }


    }
}

//helper functions

void Restaurant::readFile(const std::string &configFilePath) {

}

DishType Restaurant::convert(std::string type) {
    if(type == "ALC") return ALC;
    else if(type == "BVG") return BVG;
    else if(type == "VEG") return VEG;
    else if(type == "SPC") return SPC;
}

void Restaurant::clean() {
/*    std::vector<Table*>::iterator tablesIterator;
    for(tablesIterator = tables.begin(); tablesIterator != tables.end(); ++tablesIterator) {
        delete (*tablesIterator);
    }
    std::vector<BaseAction*>::iterator actionIterator;
    for(actionIterator = actionsLog.begin(); actionIterator !=actionsLog.end(); ++actionIterator){
        delete(*actionIterator);
    }*/
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

bool Restaurant::checkOpenValid(std::vector<std::string> tokens, Table &table) {
    if(tokens.size() < 3){
        std::cout<<"not valid open command" << std::endl;
        return false;
    }

    if(std::stoi(tokens[1]) > tables.size() || table.isOpen()){
        std::cout << "Table does not exist or already Open"<< std::endl;
        return false;
    }
    return true;

}

bool Restaurant::checkTable(int src, int dst) {
    if(src> tables.size() || !tables[src]->isOpen() || dst > tables.size() || !tables[dst]->isOpen() || tables[dst]->getCustomers().size() == tables[dst]->getCapacity()){
        std::cout << "Cannot move customers"<< std::endl;
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

void Restaurant::execute(Order &action) {
    Table &t = *tables[action.getTableID()];
    std::vector<Customer*>::iterator it;
    for(it = t.getCustomers().begin(); it != t.getCustomers().end(); ++it ){
        std::vector<int> orders = (*it)->order(menu);
        for (int i = 0; i < orders.size(); ++i) {
            OrderPair::first_type f = (*it)->getId();
            OrderPair::second_type s = menu[orders[i]];
            OrderPair order = std::make_pair(f, s);
            tables[action.getTableID()]->getOrders().push_back(order);
            std::cout << (*it)->getName() + " ordered " + menu[orders[i]].getName() <<std::endl;
        }
    }
}

void Restaurant::execute(MoveCustomer &action) {
    int id = action.getID();
    std::vector<OrderPair> newOrderList;
    if(checkTable(action.getSrc(), action.getDst())) {
        Table &src = *tables[action.getSrc()];
        Table &dest = *tables[action.getDst()];
        dest.addCustomer(src.getCustomer(id));
        std::vector<OrderPair>::iterator it;
        for (it = src.getOrders().begin(); it != src.getOrders().end(); ++it) {
            if ((*it).first == id) {
                dest.getOrders().push_back((*it));

            }else{
                newOrderList.push_back(*it);
            }
        }
        if (src.getCustomers().size() == 0) {
            src.closeTable();
        }
        src.removeCustomer(id);
        src.changeOrderList(newOrderList);
    }
}

void Restaurant::execute(PrintTableStatus &action) {
    int sum =0;
    int tableId = action.getId();
    std::string status = "closed";
    if(tables[tableId]->isOpen()) {
        status = "open";
        std::cout << "Table " + std::to_string(tableId + 1) + " status: " + status << std::endl;
        std::cout << "Customers: " << std::endl;
        for (int i = 0; i < tables[tableId]->getCustomers().size(); i++) {
            std::cout << std::to_string(tables[tableId]->getCustomers()[i]->getId()) + " " +
                         tables[tableId]->getCustomers()[i]->getName() << std::endl;
        }
        std::cout << "Orders: " << std::endl;
        for (int j = 0; j < tables[tableId]->getOrders().size(); ++j) {
            std::cout << tables[tableId]->getOrders()[j].second.getName() + " " +
                         std::to_string(tables[tableId]->getOrders()[j].second.getPrice()) + "NIS " +
                         std::to_string(tables[tableId]->getOrders()[j].first) << std::endl;
            sum += tables[tableId]->getOrders()[j].second.getPrice();
        }
        std::cout << "Current Bill: " + std::to_string(sum) + "NIS" << std::endl;
    }else{
        std::cout << "Table " + std::to_string(tableId + 1) + " status: " + status << std::endl;
    }

}

void Restaurant::execute(Close &action) {
    int sum = 0;
    int tableId = action.getId();
    for (int j = 0; j < tables[tableId]->getOrders().size(); ++j) {
        sum+= tables[tableId]->getOrders()[j].second.getPrice();
    }
    std::cout << "Table " + std::to_string(tableId+1) + " was closed. Bill " + std::to_string(sum) + "NIS" <<std::endl;
    tables[tableId]->closeTable();

}

void Restaurant::execute(CloseAll &action) {
    BaseAction *close;
    for (int i = 0; i < tables.size(); ++i) {
        if(tables[i]->isOpen()) {
            close = new Close(i);
            close->act(*this);
            delete close;
        }
    }
}

std::string Restaurant::reverseCOnvert(DishType type) {
    if(type == ALC) return "ALC";
    else if(type == BVG) return "BVG";
    else if(type == VEG) return "VEG";
    else if(type == SPC) return "SPC";
}

void Restaurant::execute(PrintMenu &action) {
    for (int i = 0; i < menu.size(); i++){
        std::cout<< menu[i].getName() + " " + reverseCOnvert(menu[i].getType())+ " " + std::to_string(menu[i].getPrice()) + "NIS" << std::endl;
    }
}

void Restaurant::execute(BackupRestaurant &action) {
    backup = new Restaurant(*this);
}

void Restaurant::execute(RestoreResturant &action) {

    for (int i = 0; i < tables.size(); ++i) {
        delete tables[i];
    }
    tables.clear();
    menu.clear();
    for (int i = 0; i < backup->tables.size(); ++i) {
        tables.push_back(new Table(*backup->tables[i]));
    }
    for (int j = 0; j < backup->menu.size(); ++j) {
        menu.push_back(backup->menu[j]);
    }
    for (int k = 0; k < backup->actionsLog.size(); ++k) {
        actionsLog.push_back(backup->actionsLog[k]);
    }

}