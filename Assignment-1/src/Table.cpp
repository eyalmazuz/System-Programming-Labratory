//
// Created by eyal on 10/31/18.
//

#include "../include/Table.h"
#include <algorithm>


using namespace std;

Table::Table(int t_capacity) : capacity(t_capacity){}

int Table::getCapacity() const { return capacity; }

std::vector<Customer*>& Table::getCustomers() { return customersList; }

bool Table::isOpen() { return open; }

void Table::openTable() { open = true; }

void Table::closeTable() { open = false; }

void Table::addCustomer(Customer *customer) {
    Customer *c = getCustomer(customer->getId());
    if (c != nullptr) {
        customersList.push_back(customer);
        delete c;
    }
}

Table::~Table() { }

void Table::addCustomers(const std::vector<Customer *> &vec) {
    for(Customer *const & customer : vec)
        addCustomer(customer);

}

Customer *Table::getCustomer(int id) {
    //iterate through the vector and find a matching file name
    auto iterator = find_if(customersList.begin(), customersList.end(),
                            [id](const Customer *c) -> bool { return c->getId() == id; });
    return iterator.operator*();
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
    return customersList.size() < capacity;
}

void Table::removeCustomer(int id) {
    auto iterator = find_if(customersList.begin(), customersList.end(),
                            [id](const Customer *c) -> bool { return c->getId() == id; });
    //remove the customer from the vector
    if (!customersList.empty() && (iterator != customersList.end())) {
        delete customersList.at(static_cast<unsigned long>(distance(customersList.begin(), iterator)));
        customersList.erase(iterator);
    }

    //remove the customer from the orderList
    orderList.erase(remove_if(orderList.begin(),orderList.end(),
                             [id](const OrderPair &o) -> bool { return o.first == id; }));

    calculateBill();

}

int Table::getBill() {
    return 0;
}

void Table::order(const std::vector<Dish> &menu) {
    for(Customer *& c : getCustomers()){
        vector<int>order(c->order(menu));
        for(const int id : order){
            //adding the order of each customer to orderList vector
            auto iterator = find_if(menu.begin(), menu.end(),
                                    [id](const Dish &d) -> bool { return d.getId() == id; });
            const Dish &d = iterator.operator*();
            OrderPair o(c->getId(),d);
            orderList.push_back(o);
            bill+= d.getPrice();
        }
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

