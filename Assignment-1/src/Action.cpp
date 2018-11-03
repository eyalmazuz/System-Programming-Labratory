//
// Created by eyal on 10/31/18.
//

#include "../include/Action.h"
#include "../include/Restaurant.h"
extern Restaurant* backup;

BaseAction::BaseAction() :status(PENDING) { }
ActionStatus BaseAction::getStatus() const { return status; }
std::string BaseAction::getErrorMsg() const { return errorMsg; }
BaseAction::~BaseAction() {}
void BaseAction::complete() { status = COMPLETED; }
void BaseAction::error(std::string errorMsg) { this->errorMsg =  errorMsg; }
void BaseAction::setError() { status = ERROR; }


OpenTable::OpenTable(int id, std::vector<Customer *> &customersList) : tableId(id), customers(customersList) {
    error("Table is already open");
}


void OpenTable::act(Restaurant &restaurant) {
    complete();
    restaurant.execute(*this);
}

std::string BaseAction::convertStatus(ActionStatus actionStatus) const {
    if (actionStatus == COMPLETED) return "Completed";
    else if (actionStatus == ERROR ) return  "Error: ";
    else if (actionStatus == PENDING) return "Pending";
}

std::string OpenTable::toString() const {
    std::string s ="open " +  std::to_string(tableId) + " ";
    for (int i = 0; i < customers.size(); i++){
        s += customers[i]->toString() + " ";
    }
    if(getStatus() == COMPLETED){
        s += convertStatus(getStatus());
    }else if( getStatus() == ERROR){
        s += convertStatus(getStatus()) + getErrorMsg();
    }
    return s;
}

int OpenTable::getId() const { return tableId; }

std::vector<Customer*> OpenTable::getCustomers() const { return customers; }

OpenTable::~OpenTable() {
    for (int i = 0; i < customers.size(); ++i) {
        delete customers[i];
    }
}

Order::Order(int id) : tableId(id) {}
void Order::act(Restaurant &restaurant) {
    complete();
    restaurant.execute(*this);
}

std::string Order::toString() const {
    std::string s = "order " + std::to_string(tableId+1 ) + " " + convertStatus(getStatus());
    return  s;

}

int Order::getTableID() { return tableId; }

Order::~Order() {}

MoveCustomer::MoveCustomer(int src, int dst, int customerId) : srcTable(src), dstTable(dst), id(customerId){
    error("Cannot move customers");
}

int MoveCustomer::getDst() { return  dstTable; }

int MoveCustomer::getID() {return id;}

int MoveCustomer::getSrc() { return srcTable;}

void MoveCustomer::act(Restaurant &restaurant) {
    complete();
    restaurant.execute(*this);
}
std::string MoveCustomer::toString() const {
    std::string s = "move " + std::to_string(srcTable+1) + " " + std::to_string(dstTable+1)+ " " + std::to_string(id)+ " ";
    if(getStatus() == COMPLETED){
        s+= convertStatus(getStatus());
    }else if (getStatus() == ERROR){
        s+= convertStatus(getStatus()) + getErrorMsg();
    }
    return s;
}

PrintTableStatus::PrintTableStatus(int id) :tableId(id){}

int PrintTableStatus::getId() { return tableId; }

void PrintTableStatus::act(Restaurant &restaurant) {
    complete();
    restaurant.execute(*this);
}
std::string PrintTableStatus::toString() const { return "status " + std::to_string(tableId+1) +" "+ convertStatus(getStatus());}

Close::Close(int id) : tableId(id) {}

std::string Close::toString() const {return "";}

int Close::getId() {return tableId;}

void Close::act(Restaurant &restaurant) {
    complete();
    restaurant.execute(*this);
}

CloseAll::CloseAll() {}

void CloseAll::act(Restaurant &restaurant) {
    complete();
    restaurant.execute(*this);
}

std::string CloseAll::toString() const { return ""; }

PrintMenu::PrintMenu() {}

std::string PrintMenu::toString() const {return ""; }

void PrintMenu::act(Restaurant &restaurant) {
    complete();
    restaurant.execute(*this);
}


BackupRestaurant::BackupRestaurant() {}
std::string BackupRestaurant::toString() const { return "backup " + convertStatus(getStatus());}
void BackupRestaurant::act(Restaurant &restaurant) {
    complete();
    restaurant.execute(*this);
}

RestoreResturant::RestoreResturant() {}
std::string RestoreResturant::toString() const { return "restore " + convertStatus(getStatus());}
void RestoreResturant::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}

PrintActionsLog::PrintActionsLog() {}

std::string PrintActionsLog::toString() const { return "";}

void PrintActionsLog::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}