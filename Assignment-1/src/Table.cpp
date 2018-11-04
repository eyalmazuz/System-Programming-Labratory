//
// Created by eyal on 10/31/18.
//

#include <iostream>
#include "../include/Table.h"

//constructor

Table::Table(int t_capacity) : capacity(t_capacity), open(false), customersList{}{ }

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
        for (int i : orders) {
            OrderPair::first_type f = (*it)->getId();
            OrderPair::second_type s = menu[i];
            OrderPair order = std::make_pair(f, s);
            orderList.push_back(order);
            std::cout << (*it)->getName() + " ordered " + menu[i].getName() << std::endl;
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
    std::vector<Customer*>::iterator it;
    for(it = customersList.begin(); it != customersList.end(); ++it){
        if((*it)->getId() == id){
            customersList.erase(it);
            break;
        }
    }
}

std::vector<OrderPair>& Table::getOrders() { return orderList; }

Customer* Table::getCustomer(int id) {
    std::vector<Customer*>::iterator it;
    for(it = customersList.begin(); it != customersList.end(); ++it){
        if((*it)->getId() == id){
            return (*it);
        }
    }
}

//helper function

void Table::clean(){
    capacity = 0;
    open = false;
    std::vector<Customer*>::iterator it;
    for (int i = 0; i < customersList.size(); ++i) {
        delete customersList[i];
    }
    customersList.clear();
    orderList.clear();
}

void Table::copy(const Table &other){
    capacity = other.capacity;
    open = other.open;
    std::vector<Customer*>::const_iterator it;
    for(it = other.customersList.begin(); it != other.customersList.end(); ++it){
        if(typeid(*(*it)) == typeid(VegetarianCustomer)) {
            int id = dynamic_cast<VegetarianCustomer*>((*it))->getId();
            std::string name = dynamic_cast<VegetarianCustomer*>((*it))->getName();
            customersList.push_back(
                    new VegetarianCustomer(name, id));
        }else if(typeid(*(*it)) == typeid(CheapCustomer)){
            int id = dynamic_cast<CheapCustomer*>((*it))->getId();
            std::string name = dynamic_cast<CheapCustomer*>((*it))->getName();
            customersList.push_back(
                    new CheapCustomer(name, id));
        }else if(typeid(*(*it)) == typeid(AlchoholicCustomer)){
            int id = dynamic_cast<AlchoholicCustomer*>((*it))->getId();
            std::string name = dynamic_cast<AlchoholicCustomer*>((*it))->getName();
            customersList.push_back(
                    new AlchoholicCustomer(name, id));
        }else if(typeid(*(*it)) == typeid(SpicyCustomer)){
            int id = dynamic_cast<SpicyCustomer*>((*it))->getId();
            std::string name = dynamic_cast<SpicyCustomer*>((*it))->getName();
            customersList.push_back(
                    new SpicyCustomer(name, id));
        }
    }
    for (const auto &i : other.orderList) {
        OrderPair::first_type f = i.first;
        OrderPair::second_type s = i.second;
        OrderPair order = std::make_pair(f, s);
        orderList.push_back(order);
    }

}

void Table::steal(const Table &other) {
    capacity = other.capacity;
    open = other.open;
    customersList = other.customersList;
    for (const auto &i : other.orderList) {
        orderList.push_back(i);
    }
}

void Table::changeOrderList(std::vector<OrderPair> newOrderList) {
    orderList.clear();
    for (const auto &i : newOrderList) {
        orderList.push_back(i);
    }
}
