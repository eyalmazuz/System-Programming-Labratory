//
// Created by eyal on 10/31/18.
//

#include "../include/Table.h"

//constructor

Table::Table(int t_capacity) : capacity(t_capacity){}

Table::Table(const Table &other) {
    copy(other);
}

Table& Table::operator=(const Table &other) {
    if(this != &other){
        clean();
        copy(other);
    }
    return *this;
}

Table::Table(Table &&other) {
    steal(other);
}

Table& Table::operator=(Table &&other) {
    if(this != &other){
        clean();
        steal(other);
    }
    return *this;
}

//destructor


Table::~Table() {

}

int Table::getCapacity() const { return capacity; }

std::vector<Customer*>& Table::getCustomers() { return customersList; }

bool Table::isOpen() { return open; }

void Table::openTable() { open = true; }

void Table::closeTable() { open = false; }

void Table::addCustomer(Customer *customer) { customersList.push_back(customer); }

Customer* Table::getCustomer(int id) {return customersList[id]; }

//helper function

void Table::clean(){
    capacity = 0;
    open = false;
    std::vector<Customer*>::iterator it;
    for(it = customersList.begin(); it != customersList.end(); ++it){
        delete (*it);
    }
}

void Table::copy(const Table &other){
    capacity = other.capacity;
    open = other.open;
    for(int i = 0; i <other.customersList.size(); i++){
//        customersList.push_back(new Customer(*other.tables[i]));
    }
}

void Table::steal(const Table &other) {
    capacity = other.capacity;
    open = other.open;
    customersList = other.customersList;
    for(int i = 0; i < other.orderList.size(); i++){
        orderList.push_back(other.orderList[i]);
    }
}