//
// Created by eyal on 10/31/18.
//

#include "../include/Table.h"

Table::Table(int t_capacity) : capacity(t_capacity){}

int Table::getCapacity() const { return capacity; }

std::vector<Customer*>& Table::getCustomers() { return customersList; }

bool Table::isOpen() { return open; }

void Table::openTable() { open = true; }

void Table::closeTable() { open = false; }

void Table::addCustomer(Customer *customer) { customersList.push_back(customer); }

Table::~Table() { }

Table::Table(const Table &other) {}