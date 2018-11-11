//
// Created by eyal on 10/31/18.
//

#include "../include/Table.h"
#include <algorithm>
#include <typeinfo>
#include <iostream>


Table::Table(int t_capacity) : capacity(t_capacity), open(false), customersList{}, orderList{}, bill(0), id(0){ }
int Table::getCapacity() const { return capacity; }

std::vector<Customer*>& Table::getCustomers() { return customersList; }

bool Table::isOpen() { return open; }

void Table::openTable() { open = true; }

void Table::closeTable() { open = false; clean(); orderList.clear();customersList.clear(); bill = 0;}

void Table::addCustomer(Customer *customer) {
    //check if customer is exist
    Customer *c = getCustomer(customer->getId());
    if (c == nullptr) {
        customersList.push_back(customer);
    }
}

Table::~Table() {
    clean();
}

void Table::addCustomers(const std::vector<Customer *> &vec) {
    for(auto customer : vec)
        addCustomer(customer);

}

Customer *Table::getCustomer(int id) {
    if (customersList.size() == 0)
        return nullptr;

    //iterate through the vector and find a matching file name
    auto iterator = find_if(customersList.begin(), customersList.end(),
                            [id](const Customer *c) -> bool { return c->getId() == id; });

    if (iterator == customersList.end())
        return nullptr;

    return (*iterator);
}

int Table::getCustomersNum() const {
    return customersList.size();
}

int Table::getId() const {
    return id;
}

std::vector<OrderPair> &Table::getOrders() {
    return orderList;
}

bool Table::isFull() {
    return (int)customersList.size() < capacity;
}

void Table::removeCustomer(int id) {
    auto iterator = find_if(customersList.begin(), customersList.end(),
                            [id](const Customer *c) -> bool { return c->getId() == id; });
    //remove the customer from the vector
    if (!customersList.empty() && (iterator != customersList.end())) {
        //delete customersList.at(static_cast<unsigned long>(distance(customersList.begin(), iterator)));
        customersList.erase(iterator);
    }


}

int Table::getBill() {
    return bill;
}

void Table::order(const std::vector<Dish> &menu) {
    for(auto customer : getCustomers()){
        std::vector<int>order(customer->order(menu));
        for(auto id : order){
            //adding the order of each customer to orderList vector
            auto iterator = find_if(menu.begin(), menu.end(),
                                    [id](const Dish &d) -> bool { return d.getId() == id; });
            if (iterator != menu.end()) {
                const Dish &d = (*iterator);
                OrderPair o(customer->getId(), d);
                orderList.push_back(o);
                bill += d.getPrice();
                std::cout<< customer->getName() + " ordered " +d.getName() << std::endl;
            }
        }
    }
}

void Table::addOrder(const std::vector<OrderPair> &otherOrderList) {
    for (auto o : otherOrderList){
        orderList.push_back(o);
        bill += o.second.getPrice();
    }
}


void Table::calculateBill() {
    if (isOpen()) {
        bill = 0;
        for (const OrderPair &o :getOrders()) {
            bill += o.second.getPrice();
        }
    }
}

Table &Table::operator=(const Table &other) {
    if (this != &other) {
        clean();
        copy(other);
    }
    return *this;
}

Table::Table(const Table &other): capacity(other.capacity), open(other.open), customersList{}, orderList{}, bill(other.bill), id(other.id) {
    copy(other);

}

Table &Table::operator=(Table &&other) {
    clean();
    steal(other);
    return *this;
}

Table::Table(Table &&other): capacity(other.capacity), open(other.open), customersList{}, orderList{}, bill(other.bill), id(other.id){
    steal(other);
}

//ToDo: verify that orderList element is stored on heap
void Table::clean() const {
    for (auto c :customersList) {
        if (c!= nullptr)
            delete c;
    }
}

void Table::copy(const Table &other) {
    this->bill = other.bill;
    this->id = other.id;
    this->capacity = other.capacity;
    this->open = other.open;

    for(auto c: other.customersList){
        if (typeid(*c) ==  typeid(VegetarianCustomer))
            customersList.push_back(new VegetarianCustomer(c->getName(),c->getId()));
        else if (typeid(*c) ==  typeid(SpicyCustomer))
            customersList.push_back(new SpicyCustomer(c->getName(),c->getId()));
        else if(typeid(*c) ==  typeid(AlchoholicCustomer))
            customersList.push_back(new AlchoholicCustomer(c->getName(),c->getId()));
        else if(typeid(*c) ==  typeid(CheapCustomer))
            customersList.push_back(new CheapCustomer(c->getName(),c->getId()));
    }

    for (const OrderPair &order:other.orderList) {
        orderList.push_back(order);
    }

}

void Table::steal(Table &other) {
    this->bill = other.bill;
    this->id = other.id;
    this->capacity = other.capacity;
    this->open = other.open;
    this->customersList = other.customersList;
    //this->orderList = other.orderList;
    for (const auto &i : other.orderList) {
        OrderPair::first_type f = i.first;
        OrderPair::second_type s = i.second;
        OrderPair order = std::make_pair(f, s);
        orderList.push_back(order);
    }
    other.customersList.erase(other.customersList.begin(), other.customersList.end());
    //other.orderList.erase(other.orderList.begin(), other.orderList.end());
}

void Table::setId(int id) {
    Table::id = id;
}

void Table::replaceOrders(const std::vector<OrderPair> &otherOrderList) {
    orderList.clear();
    for (const auto &i : otherOrderList) {
        OrderPair::first_type f = i.first;
        OrderPair::second_type s = i.second;
        OrderPair order = std::make_pair(f, s);
        orderList.push_back(order);
    }
    calculateBill();

}



