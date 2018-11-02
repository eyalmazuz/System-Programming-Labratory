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