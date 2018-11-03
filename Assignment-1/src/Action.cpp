//
// Created by eyal on 10/31/18.
//

#include "../include/Action.h"
#include "../include/Restaurant.h"

BaseAction::BaseAction() { }
ActionStatus BaseAction::getStatus() const { return status; }
std::string BaseAction::getErrorMsg() const { return errorMsg; }

void BaseAction::complete() { status = COMPLETED; }

void BaseAction::error(std::string errorMsg) { std::cout << "Error:" << errorMsg << std::endl; }
void BaseAction::setError() {}

OpenTable::OpenTable(int id, std::vector<Customer *> &customersList) : tableId(id), customers(customersList) {}

void OpenTable::act(Restaurant &restaurant) {
}

std::string OpenTable::toString() const { return ""; }

Order::Order(int id) : tableId(id) {}

void Order::act(Restaurant &restaurant) {
}

std::string Order::toString() const {return ""; }

MoveCustomer::MoveCustomer(int src, int dst, int customerId) : srcTable(src), dstTable(dst), id(customerId){}

void MoveCustomer::act(Restaurant &restaurant) {
}
std::string MoveCustomer::toString() const {return "";}

PrintTableStatus::PrintTableStatus(int id) :tableId(id){}


void PrintTableStatus::act(Restaurant &restaurant) {
}
std::string PrintTableStatus::toString() const { return "";}

Close::Close(int id) : tableId(id) {}

std::string Close::toString() const {return "";}

void Close::act(Restaurant &restaurant) {
}

CloseAll::CloseAll() {}

void CloseAll::act(Restaurant &restaurant) {
}

std::string CloseAll::toString() const { return ""; }

PrintMenu::PrintMenu() {}

std::string PrintMenu::toString() const {return ""; }

void PrintMenu::act(Restaurant &restaurant) {
}

BackupRestaurant::BackupRestaurant() {}
std::string BackupRestaurant::toString() const { return "";}
void BackupRestaurant::act(Restaurant &restaurant) {
}

RestoreResturant::RestoreResturant() {}
std::string RestoreResturant::toString() const { return "";}
void RestoreResturant::act(Restaurant &restaurant) {
}