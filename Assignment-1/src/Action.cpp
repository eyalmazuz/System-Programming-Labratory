//
// Created by eyal on 10/31/18.
//

#include "../include/Action.h"
#include "../include/Table.h"
#include "../include/Restaurant.h"
#include <typeinfo>
#include <algorithm>


//ToDo: check if neccesarry to put pending as default value
BaseAction::BaseAction():logger(""), errorMsg(""), status(COMPLETED)  {  }
ActionStatus BaseAction::getStatus() const { return status; }
std::string BaseAction::getErrorMsg() const { return errorMsg; }
BaseAction::~BaseAction() = default;

std::string BaseAction::getStrStatus() const {
    std::string s;
    switch (getStatus()) {
        case PENDING:
            s = "Pending";
            break;
        case COMPLETED:
            s = "Completed";
            break;
        case ERROR:
            s = "Error";
            break;
    }
    return s;
}

void BaseAction::complete() {
    status = COMPLETED;
}
void BaseAction::error(std::string errorMsg) {
    status = ERROR;
    this-> errorMsg = errorMsg;
    logger.append(getStrStatus() + ": " + errorMsg);
    std::cout << "Error: " << errorMsg << std::endl;
}


void BaseAction::setError(const std::string &errorMessage) {
    if (errorMessage != "")
        setErrorMsg(errorMessage);
    error(errorMsg);
}

const std::string &BaseAction::getLogger() const {
    return logger;
}

void BaseAction::setErrorMsg(const std::string &errorMsg) {
    BaseAction::errorMsg = errorMsg;
}

void BaseAction::setLogger(const std::string &logger) {
    BaseAction::logger = logger;
}

void BaseAction::appendLogger(const std::string &data) {
    logger.append(data);
}

void OpenTable::act(Restaurant &restaurant) {
    Table *t = restaurant.getTable(tableId);
    if (t == nullptr){
        setErrorMsg("Table does not exist");
        error(getErrorMsg());
    }else if (t->isOpen()){
        for(auto c : customers)
            appendLogger(c->toString() + " ");
        setErrorMsg("Table is already open");
        error(getErrorMsg());
    }else if ((int)customers.size() + t->getCustomersNum() <= t->getCapacity() ){
        t->openTable();
        t->setId(tableId);
        t->addCustomers(customers);
        for(auto c : customers)
            appendLogger(c->toString() + " ");
        appendLogger("Completed");
        complete();
    }
}



std::string OpenTable::toString() const {
    return getStatus() == COMPLETED ? getLogger() + getStrStatus()
                                    : getLogger() + getStrStatus() + getErrorMsg();
}

OpenTable::OpenTable(int id, std::vector<Customer *> &customersList) :
        tableId(id) ,
        customers(customersList) {
    appendLogger("open " + std::to_string(tableId + 1) +" ");
    setErrorMsg("Table is already open");
}

const int OpenTable::getTableId() const {
    return tableId;
}

const std::vector<Customer *> &OpenTable::getCustomers() const {
    return customers;
}

Order::Order(int id) :
        tableId(id) {
    appendLogger("order " + std::to_string(tableId + 1)  + " ");
    setErrorMsg("Table is not open");
}

void Order::act(Restaurant &restaurant) {
    Table *t = restaurant.getTable(tableId);
    if (t == nullptr){
        setErrorMsg("Table does not exist");
        error(getErrorMsg());
    }else if (!t->isOpen()) {
        setErrorMsg("Table is not open");
        error(getErrorMsg());
    }else{
        t->order(restaurant.getMenu());
        appendLogger("Completed");
        complete();
    }
}

std::string Order::toString() const {
    return getStatus() == COMPLETED ? getLogger() + getStrStatus()
                                    : getLogger() + getStrStatus() + getErrorMsg();

}

const int Order::getTableId() const {
    return tableId;
}

MoveCustomer::MoveCustomer(int src, int dst, int customerId) :
        srcTable(src), dstTable(dst), id(customerId) {
    //logger = "Table " + to_string(customerId) + ": ";
    appendLogger("move " + std::to_string(srcTable+1) + " " + std::to_string(dstTable+1) + " " + std::to_string(id) +" ");
    setErrorMsg("Cannot move customer");
}

void MoveCustomer::act(Restaurant &restaurant) {
    Table *t_src = restaurant.getTable(srcTable);
    Table *t_dst = restaurant.getTable(dstTable);

    if (t_src == nullptr || !t_src->isOpen() || t_dst == nullptr || !t_dst->isOpen() || !t_dst->isFull())
        error(getErrorMsg());
    else{
        Customer *c = t_src->getCustomer(id);
        if (c == nullptr)
            error(getErrorMsg());
        else {
            //gather all customer orders
            std::vector<OrderPair>order;
            std::vector<OrderPair>srcOrder;
            for(auto o : t_src->getOrders())
                if (o.first == id)
                    order.push_back(o);
                else
                    srcOrder.push_back(o);

            //if (order.size() == 0)
                //error(getErrorMsg());
            //else {
                t_src->removeCustomer(id);
                t_src->replaceOrders(srcOrder);
                t_dst->addCustomer(c);
                t_dst->addOrder(order);
                //close the table if necessary
                if (t_src->getCustomers().size() == 0)
                    t_src->closeTable();
                appendLogger("Completed");
                complete();
            //}
        }
    }
}

std::string MoveCustomer::toString() const {
    return getStatus() == COMPLETED ? getLogger() + getStrStatus()
                                    : getLogger() + getStrStatus() + getErrorMsg();
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
    appendLogger("close " + std::to_string(tableId+1) + " ");
}

void Close::act(Restaurant &restaurant) {
    Table *t = restaurant.getTable(tableId);
    if (t == nullptr) {
        setErrorMsg("Table does not exist");
        error(getErrorMsg());
    }else if (!t->isOpen()) {
        setErrorMsg("Table is not open");
        error(getErrorMsg());
    }else{
        int bill = t->getBill();
        t->closeTable();
        std::string s=("Table " + std::to_string(tableId+1)  + " was closed. Bill " + std::to_string(bill) +"NIS");
        std::cout << s << std::endl;
        appendLogger("Completed");
        complete();
    }
}

std::string Close::toString() const {
    return getLogger();
}

const int Close::getTableId() const {
    return tableId;
}

CloseAll::CloseAll() {

}


void CloseAll::act(Restaurant &restaurant) {
    std::string out;
    for(auto t :restaurant.getTables()){
        if (t->isOpen()){
            //cout << "Table " << to_string(t->getId() + 1)  << " was closed. Bill " << to_string(t->getBill())+"NIS" << endl;
            out+= "Table " + std::to_string(t->getId() + 1)  + " was closed. Bill " + std::to_string(t->getBill())+"NIS\n";
        }
    }
    std::cout << out;
    complete();
}

std::string CloseAll::toString() const {
    return "";
}

PrintMenu::PrintMenu() {

}


void PrintMenu::act(Restaurant &restaurant) {
    std::string out="";
    for(const Dish & d :restaurant.getMenu()){
        out+= d.getName() + " " + std::to_string(d.getType()) + std::to_string (d.getPrice()) + "\n";
    }
    std::cout << out << std::endl;
}

std::string PrintMenu::toString() const {
    return "";
}

PrintTableStatus::PrintTableStatus(int id) :
        tableId(id) {
    setErrorMsg("Table is not exist");
}


void PrintTableStatus::act(Restaurant &restaurant) {
    std::string out="";
    //for(auto t :restaurant.getTables()){
    Table *t = restaurant.getTable(tableId);
    if (t == nullptr)
        error(getErrorMsg());
    else {
        out += "Table " + std::to_string(tableId + 1) + " status: ";
        out += t->isOpen() ? "open\n" : "closed\n";
        if (t->isOpen()) {
            out += "Customers:\n";
            for (auto c : t->getCustomers())
                out += std::to_string(c->getId()) + " " + c->getName() + "\n";
            out += "Orders:\n";
            for (auto o : t->getOrders())
                out += o.second.getName() + " " + std::to_string(o.second.getPrice()) + "NIS " + std::to_string(o.first) + "\n";
            out+="Current Bill: " + std::to_string(t->getBill()) + "NIS\n";
        }
        std::cout << out;

    }
    complete();
    appendLogger("status " + std::to_string(tableId + 1) + " " + getStrStatus());


}

std::string PrintTableStatus::toString() const {
    return getStatus() == COMPLETED ? getLogger() + getStrStatus()
                                    : getLogger() + getStrStatus() + getErrorMsg();
}

const int PrintTableStatus::getTableId() const {
    return tableId;
}

PrintActionsLog::PrintActionsLog() {

}

void PrintActionsLog::act(Restaurant &restaurant) {
    std::string out="";
    for(auto actionLog : restaurant.getActionsLog()){
        if (actionLog->getLogger().size() != 0)
            out+=actionLog->getLogger()  + "\n";
    }
    std::cout << out;
}

std::string PrintActionsLog::toString() const {
    return getLogger();
}

BackupRestaurant::BackupRestaurant() {
    appendLogger("backup ");
}

void BackupRestaurant::act(Restaurant &restaurant) {
    if (backup != nullptr)
        delete(backup);
    backup = new Restaurant(restaurant);
    complete();
    appendLogger(getStrStatus());
}

std::string BackupRestaurant::toString() const {
    return getLogger();
}

RestoreResturant::RestoreResturant() {
    setErrorMsg("No backup available");
    appendLogger("restore ");
}


void RestoreResturant::act(Restaurant &restaurant) {
    if (backup == nullptr) error(getErrorMsg());
    else {
        restaurant = *backup;
        complete();
        appendLogger(getStrStatus());

    }
}

std::string RestoreResturant::toString() const {
    return  getStatus() == COMPLETED ? getLogger() + getStrStatus()
                                     : getLogger() + getStrStatus() + getErrorMsg();
}
