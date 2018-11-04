//
// Created by eyal on 10/31/18.
//

#include <iostream>
#include "../include/Table.h"

//constructor

Table::Table(int t_capacity) : capacity(t_capacity), open(false){ }

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
    clean();

}


int Table::getCapacity() const { return capacity; }

std::vector<Customer*>& Table::getCustomers() { return customersList; }

bool Table::isOpen() { return open; }

void Table::openTable() { open = true; }

void Table::closeTable() {
    open = false;
}

void Table::order(const std::vector<Dish> &menu){
    std::vector<Customer *>::iterator it;
    for(it = customersList.begin(); it != customersList.end(); ++it) {
        std::vector<int> orders = (*it)->order(menu);
        for (int i = 0; i < orders.size(); ++i) {
            OrderPair::first_type f = (*it)->getId();
            OrderPair::second_type s = menu[orders[i]];
            OrderPair order = std::make_pair(f, s);
            orderList.push_back(order);
            std::cout << (*it)->getName() + " ordered " + menu[orders[i]].getName() << std::endl;
        }
    }
}

int Table::getBill() {
    int sum = 0;
    std::vector<OrderPair>::iterator it;
    for (it = orderList.begin(); it != orderList.end(); ++it) {
        sum += (*it).second.getPrice();
    }
    return sum;
}

void Table::addCustomer(Customer *customer) { customersList.push_back(customer); }

void Table::removeCustomer(int id) {
    for(int i = 0; i < customersList.size(); i++){
        if(customersList[i]->getId() == id){
            customersList.erase(customersList.begin()+i);
        }
    }
}

std::vector<OrderPair>& Table::getOrders() { return orderList; }

Customer* Table::getCustomer(int id) {
    for(int i = 0; i < customersList.size(); i++){
        if(customersList[i]->getId() == id){
            return customersList[i];
        }
    }

}

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
        if(typeid(other.customersList[i]) == typeid(VegetarianCustomer)) {
            customersList.push_back(
                    new VegetarianCustomer(other.customersList[i]->getName(), other.customersList[i]->getId()));
        }else if(typeid(other.customersList[i]) == typeid(CheapCustomer)){
            customersList.push_back(
                    new CheapCustomer(other.customersList[i]->getName(), other.customersList[i]->getId()));
        }else if(typeid(other.customersList[i]) == typeid(AlchoholicCustomer)){
            customersList.push_back(
                    new AlchoholicCustomer(other.customersList[i]->getName(), other.customersList[i]->getId()));
        }else if(typeid(other.customersList[i]) == typeid(SpicyCustomer)){
            customersList.push_back(
                    new SpicyCustomer(other.customersList[i]->getName(), other.customersList[i]->getId()));
        }
    }
    for(int i = 0; i <other.orderList.size(); i++){
        orderList.push_back(other.orderList[i]);
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

void Table::changeOrderList(std::vector<OrderPair> newOrderList) {
    orderList.clear();
    for (int i = 0; i < newOrderList.size(); ++i) {
        orderList.push_back(newOrderList[i]);
    }
}
