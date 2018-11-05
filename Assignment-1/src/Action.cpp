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

std::string BaseAction::getStrStatus() const {
    switch (getStatus()) {
        case PENDING:
            return"Pending";
        case COMPLETED:
            return "Completed";
        case ERROR:
            return "break";
    }
}

void BaseAction::complete() {
    status = COMPLETED;
}
void BaseAction::error(std::string errorMsg) {
    status = ERROR;
    this-> errorMsg = errorMsg;
    logger.append(errorMsg);
    std::cout << "Error:" << errorMsg << std::endl;
}


void BaseAction::setError() {
    error(errorMsg);
}

const string &BaseAction::getLogger() const {
    return logger;
}

void BaseAction::setErrorMsg(const string &errorMsg) {
    BaseAction::errorMsg = errorMsg;
}

void OpenTable::act(Restaurant &restaurant) {
    Table *t = restaurant.getTable(tableId);
    if (t == nullptr || t->isOpen()){
         error(getErrorMsg());
     }else if (customers.size() + t->getCustomersNum() <= t->getCapacity() ){
         t->openTable();
         t->setId(tableId);
         t->addCustomers(customers);
        for(auto c : customers)
            logger.append(c->toString()) + " ";
        logger.append(" Completed");
        complete();
     }
}



std::string OpenTable::toString() const {
    return getStatus() == COMPLETED ? logger + getStrStatus()
                                    : logger + getStrStatus() + getErrorMsg();
}

OpenTable::OpenTable(int id, std::vector<Customer *> &customersList) :
    tableId(id) ,
    customers(customersList) {
    logger.append("open " + to_string(tableId + 1) +" ");
    setErrorMsg("Table is already open");
}

const int OpenTable::getTableId() const {
    return tableId;
}

const vector<Customer *> &OpenTable::getCustomers() const {
    return customers;
}

Order::Order(int id) :
    tableId(id) {
    logger = "order " + to_string(tableId + 1)  + " ";
}

void Order::act(Restaurant &restaurant) {
    Table *t = restaurant.getTable(tableId);
    if (t == nullptr || !t->isOpen()){
        error(getErrorMsg());
    }else{
        t->order(restaurant.getMenu());
        logger.append("Completed");
        complete();
    }
}

std::string Order::toString() const {
    return getStatus() == COMPLETED ? logger + getStrStatus()
                                    : logger + getStrStatus() + getErrorMsg();

}

const int Order::getTableId() const {
    return tableId;
}

MoveCustomer::MoveCustomer(int src, int dst, int customerId) :
    srcTable(src), dstTable(dst), id(customerId) {
    //logger = "Table " + to_string(customerId) + ": ";
    setErrorMsg("Cannot move customer");
}

void MoveCustomer::act(Restaurant &restaurant) {
    Table *t_src = restaurant.getTable(srcTable);
    Table *t_dst = restaurant.getTable(dstTable);

    if (t_src == nullptr || !t_src->isOpen() || t_dst == nullptr || !t_src->isOpen() || !t_dst->isFull())
        error(getErrorMsg());
    else{
        Customer *c = t_src->getCustomer(id);
        if (c == nullptr)
            error(getErrorMsg());
        else {
            //gather all customer orders
            vector<OrderPair>order;
            for(auto o : t_src->getOrders())
                if (o.first == id)
                    order.push_back(o);

            if (order.size() == 0)
                error(getErrorMsg());
            else {
                t_src->removeCustomer(id);
                t_dst->addCustomer(c);
                t_dst->updateOrder(order);
                logger.append("move " + to_string(srcTable+1) + " " + to_string(dstTable+1) + " " + to_string(id+1) + " Completed");
                complete();
            }
        }
    }
}

std::string MoveCustomer::toString() const {
    return getStatus() == COMPLETED ? logger + getStrStatus()
                                    : logger + getStrStatus() + getErrorMsg();
}

const int MoveCustomer::getSrcTable() const {
    return srcTable;
}

const int MoveCustomer::getDstTable() const {
    return dstTable;
}

const int MoveCustomer::getId() const {
    return id;
}

Close::Close(int id) : tableId(id) {
    setErrorMsg("Table does not exist or is not open");
}

void Close::act(Restaurant &restaurant) {
    Table *t = restaurant.getTable(tableId);
    if (t == nullptr || !t->isOpen()) {
        error(getErrorMsg());
    }else{
        int bill = t->getBill();
        t->closeTable();
        logger.append("Table " + to_string(tableId)  + " was closed. Bill " + to_string(bill));
        logger.append("Completed");
        complete();
    }

    //if (t != nullptr) delete(t);

}

std::string Close::toString() const {
    return logger;
}

const int Close::getTableId() const {
    return tableId;
}

CloseAll::CloseAll() {

}


void CloseAll::act(Restaurant &restaurant) {
    for(auto t :restaurant.getTables()){
        if (t->isOpen()){
            cout << "Table " << to_string(t->getId() + 1)  << " was closed. Bill " << to_string(t->getBill()) << endl;
        }
    }
    complete();
}

std::string CloseAll::toString() const {
    return "";
}

PrintMenu::PrintMenu() {

}


void PrintMenu::act(Restaurant &restaurant) {
    string out="";
    for(const Dish & d :restaurant.getMenu()){
        out+= d.getName() + " " + to_string(d.getType()) + to_string (d.getPrice()) + "\n";
    }
    cout << out << endl;
}

std::string PrintMenu::toString() const {
    return "";
}

PrintTableStatus::PrintTableStatus(int id) :
    tableId(id) {
    setErrorMsg("Table is not exist");
}


void PrintTableStatus::act(Restaurant &restaurant) {
    string out="";
    //for(auto t :restaurant.getTables()){
    Table *t = restaurant.getTable(tableId);
    if (t == nullptr)
        error(getErrorMsg());
    else {
        out += "Table " + to_string(t->getId() + 1) + " status: ";
        out += t->isOpen() ? "open \n" : "closed \n";
        if (t->isOpen()) {
            for (auto c : t->getCustomers())
                out += to_string(c->getId()) + " " + c->getName() + "\n";
            out += "Orders: \n";
            for (auto o : t->getOrders())
                out += o.second.getName() + " " + to_string(o.second.getPrice()) + "NIS " + to_string(o.first) + "\n";
        }
        out+="Current Bill: " + to_string(t->getBill()) + "\n";
        cout << out;
        //complete();
    }


}

std::string PrintTableStatus::toString() const {
    return getStatus() == COMPLETED ? logger + getStrStatus()
                                    : logger + getStrStatus() + getErrorMsg();
}

const int PrintTableStatus::getTableId() const {
    return tableId;
}

PrintActionsLog::PrintActionsLog() {

}

void PrintActionsLog::act(Restaurant &restaurant) {
    string out="";
//    for (auto it = restaurant.getActionsLog().rbegin(); it != restaurant.getActionsLog().rend(); ++it){
//        if ((*it)->getLogger().size() != 0)
//            out+=(*it)->getLogger()  + "\n";
//    }

    for(auto actionLog : restaurant.getActionsLog()){
        if (actionLog->getLogger().size() != 0)
            out+=actionLog->getLogger()  + "\n";
    }
    cout << out << endl;
}

std::string PrintActionsLog::toString() const {
    return logger;
}

BackupRestaurant::BackupRestaurant() {

}

void BackupRestaurant::act(Restaurant &restaurant) {
    backup = &restaurant;
}

std::string BackupRestaurant::toString() const {
    return "";
}

RestoreResturant::RestoreResturant() {
    setErrorMsg("No backup available");
}


void RestoreResturant::act(Restaurant &restaurant) {
    if (backup == nullptr) error(getErrorMsg());
    else {
        restaurant = *backup;
        complete();
    }
}

std::string RestoreResturant::toString() const {
    return logger;
}
