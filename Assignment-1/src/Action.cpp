//
// Created by eyal on 10/31/18.
//

#include "../include/Action.h"
#include "../include/Restaurant.h"

BaseAction::BaseAction() { }
ActionStatus BaseAction::getStatus() const { return status; }
std::string BaseAction::getErrorMsg() const { return errorMsg; }
BaseAction::~BaseAction() {}

void BaseAction::complete() { status = COMPLETED; }
void BaseAction::error(std::string errorMsg) { std::cout << "Error:" << errorMsg << std::endl; }

OpenTable::OpenTable(int id, std::vector<Customer *> &customersList) : tableId(id), customers(customersList) {}

void OpenTable::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}

std::string OpenTable::toString() const { return ""; }

int OpenTable::getId() const { return tableId; }

std::vector<Customer*> OpenTable::getCustomers() const { return customers; }

OpenTable::~OpenTable() {
    for (int i = 0; i < customers.size(); ++i) {
        delete customers[i];
    }
}

Order::Order(int id) : tableId(id) {}
void Order::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}

std::string Order::toString() const {return ""; }

int Order::getTableID() { return tableId; }

Order::~Order() {}

MoveCustomer::MoveCustomer(int src, int dst, int customerId) : srcTable(src), dstTable(dst), id(customerId){}

int MoveCustomer::getDst() { return  dstTable; }

int MoveCustomer::getID() {return id;}

int MoveCustomer::getSrc() { return srcTable;}

void MoveCustomer::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}
std::string MoveCustomer::toString() const {return "";}

PrintTableStatus::PrintTableStatus(int id) :tableId(id){}

int PrintTableStatus::getId() { return tableId; }

void PrintTableStatus::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}
std::string PrintTableStatus::toString() const { return "";}

Close::Close(int id) : tableId(id) {}

std::string Close::toString() const {return "";}

int Close::getId() {return tableId;}
void Close::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}

CloseAll::CloseAll() {}

void CloseAll::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}

std::string CloseAll::toString() const { return ""; }

PrintMenu::PrintMenu() {}

std::string PrintMenu::toString() const {return ""; }

void PrintMenu::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}

BackupRestaurant::BackupRestaurant() {}
std::string BackupRestaurant::toString() const { return "";}
void BackupRestaurant::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}

RestoreResturant::RestoreResturant() {}
std::string RestoreResturant::toString() const { return "";}
void RestoreResturant::act(Restaurant &restaurant) {
    restaurant.execute(*this);
}