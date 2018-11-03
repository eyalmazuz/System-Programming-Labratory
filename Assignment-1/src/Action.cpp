//
// Created by eyal on 10/31/18.
//

#include "../include/Action.h"
#include "../include/Table.h"
#include "../include/Restaurant.h"
#include <typeinfo>
#include <algorithm>


using namespace std;

//ToDo: check if neccesarry to put pending as default value
BaseAction::BaseAction()  { status = PENDING; }
ActionStatus BaseAction::getStatus() const { return status; }
std::string BaseAction::getErrorMsg() const { return errorMsg; }



void BaseAction::complete() { status = COMPLETED; }
void BaseAction::error(std::string errorMsg) {
    status = ERROR;
    logger.append(errorMsg);
    std::cout << "Error:" << errorMsg << std::endl;
}

void BaseAction::complete(std::string out) {
    cout << out << endl;
    logger.append(out);
    complete();
}

const Logger &BaseAction::getLogger() const {
    return logger;
}

void OpenTable::act(Restaurant &restaurant) {
    //update the logger
    std::string out = "open " + to_string(tableId); + " " ;

    for(const Customer  *c : customers)
        out.append(c->toString()) + " ";
    logger.append(out);

    Table *t = restaurant.getTable(tableId);
    if (t == nullptr || t->isOpen()){
         error("Table does not exist or is already open");
     }else if (customers.size() + t->getCustomersNum() <= t->getCapacity() ){
         t->openTable();
         t->addCustomers(customers);
         complete(out);
     }

     if (t != nullptr) delete(t);
}



std::string OpenTable::toString() const {
    return typeid(this).name();;
}

OpenTable::OpenTable(int id, std::vector<Customer *> &customersList) :
    tableId(id), customers(customersList) {
}

//ToDo: check if d'tor in OpenTable is needed
//OpenTable::~OpenTable() {
//    for (int i = 0; i < customers.size(); ++i) {
//        if (customers[i] != nullptr) delete(customers[i]);
//    }
//}


Order::Order(int id) :
    tableId(id) {

}

void Order::act(Restaurant &restaurant) {
    Table *t = restaurant.getTable(tableId);
    if (t == nullptr || !t->isOpen()){
        error("Table does not exist or is already open");
    }else{
        std::string out = "order " + to_string(tableId); + "\n" ;
        t->order(restaurant.getMenu());
        for(const OrderPair & o :t->getOrders()){
            out.append(o.first  + " orders " + o.second.getName() + "\n") ;
        }
        complete(out);

    }

    if (t != nullptr) delete(t);
}

std::string Order::toString() const {
    return typeid(this).name();
}

MoveCustomer::MoveCustomer(int src, int dst, int customerId) :
    srcTable(src), dstTable(dst), id(customerId) {
}

void MoveCustomer::act(Restaurant &restaurant) {
    Table *t_src = restaurant.getTable(srcTable);
    Table *t_dst = restaurant.getTable(dstTable);

    if (t_src == nullptr || !t_src->isOpen() || t_dst == nullptr || !t_src->isOpen() || !t_dst->isFull())
        error("Cannot move customer");
    else{
        Customer *c = t_src->getCustomer(id);
        if (c == nullptr)
            error("Cannot move customer");
        else {
            t_src->removeCustomer(id);
            t_dst->addCustomer(c);
            complete("move " + to_string(srcTable) + " " + to_string(dstTable) + " " + to_string(id););
            delete(c);
        }
    }

    if (t_src != nullptr) delete(t_src);
    if (t_dst != nullptr) delete(t_dst);
}

std::string MoveCustomer::toString() const {
    return typeid(this).name();
}

Close::Close(int id) : tableId(id) {

}

void Close::act(Restaurant &restaurant) {
    Table *t = restaurant.getTable(tableId);
    if (t == nullptr || !t->isOpen()) {
        error("Table does not exist or is not open");
    }else{
        int bill = t->getBill();
        t->closeTable();
        complete("Table " + to_string(tableId)  + " was closed. Bill " + to_string(bill));
    }

    if (t != nullptr) delete(t);

}

std::string Close::toString() const {
    return typeid(this).name();
}

CloseAll::CloseAll() {

}

//ToDo:complete CloseAll after updating resturant.cpp
void CloseAll::act(Restaurant &restaurant) {
//    string out="";
//    for(const Table & t :restaurant.getTable()){
//        if (t.isOpen(){
//            out+= "Table " + t.getId() + " was closed. Bill " + t.getBill()
//        }
//    }
//    complete(out)
}

std::string CloseAll::toString() const {
    return typeid(this).name();
}

PrintMenu::PrintMenu() {

}


void PrintMenu::act(Restaurant &restaurant) {
    string out="";
    for(const Dish & d :restaurant.getMenu()){
        out+= d.getName() + " " + to_string(d.getType()) + to_string (d.getPrice());
    }
    complete(out);
}

std::string PrintMenu::toString() const {
    return typeid(this).name();
}

PrintTableStatus::PrintTableStatus(int id) :
    tableId(id) {

}

//ToDo - create toString method to customer <id><name>
void PrintTableStatus::act(Restaurant &restaurant) {
//    string out="";
//    for(const Table & t :restaurant.getTables()){
//
//        out+= "Table " + t.getId() + " status: ";
//        out+= t.isOpen() ? "open" : "closed"
//        if (t.isOpen()){
//            out+="open \n";
//            out+="Customers: \n";
//            for(const Customer &c : t.getCustomers())
//                out+= c.getId() + " " + c.getName() + "\n";
//            out+="Orders: \n";
//            for(const OrderPair &o : t.getOrders())
//                out+= o.second.getName() + " " + o.second.getPrice() + " " + o.first + "\n";
//
//
//        }
//    }
//    complete(out)
}

std::string PrintTableStatus::toString() const {
    return typeid(this).name();
}

PrintActionsLog::PrintActionsLog() {

}

void PrintActionsLog::act(Restaurant &restaurant) {
    string out="";
    for (auto it = restaurant.getActionsLog().rbegin(); it != restaurant.getActionsLog().rend(); ++it){
        out+= it.operator*()->getLogger().getText();
    }

    complete(out);
}

std::string PrintActionsLog::toString() const {
    return std::__cxx11::string();
}

BackupRestaurant::BackupRestaurant() {

}

void BackupRestaurant::act(Restaurant &restaurant) {

}

std::string BackupRestaurant::toString() const {
    return std::__cxx11::string();
}

RestoreResturant::RestoreResturant() {

}

void RestoreResturant::act(Restaurant &restaurant) {

}

std::string RestoreResturant::toString() const {
    return std::__cxx11::string();
}
