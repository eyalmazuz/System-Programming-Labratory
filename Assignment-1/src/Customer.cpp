//
// Created by eyal on 10/31/18.
//
#include <algorithm>
#include "../include/Customer.h"


//Customer abstract class

Customer::Customer(std::string c_name, int c_id) : name(c_name), id(c_id){}


std::string Customer::getName() const { return  name; }

int Customer::getId() const { return id; }

//Vegetarian Customer
VegetarianCustomer::VegetarianCustomer(std::string name, int id) : Customer(name, id){}

std::vector<int> VegetarianCustomer::order(const std::vector<Dish> &menu) {
    std::vector<int> orders;
    int index = -1;
    bool order = false;
    std::vector<Dish, std::allocator<Dish>>::const_iterator it;
    for (it = menu.begin(); it != menu.end(); ++it) {
        if ((*it).getType() == VEG) {
            orders.push_back((*it).getId());
            order = true;
            break;
        }
    }
    if (order) {
    int price = -1;
    for (unsigned int i = 0; i < menu.size(); i++) {
        if (menu[i].getType() == BVG && (price == -1 || menu[i].getPrice() > price)) {
            index = i;
            price = menu[i].getPrice();
        }
    }
    if (index != -1)
        orders.push_back(index);
    }
    return orders;
}

std::string VegetarianCustomer::toString() const {return getName()+",veg";}


//Cheap Customer
CheapCustomer::CheapCustomer(std::string name, int id) : Customer(name, id), ordered(false){}

std::vector<int> CheapCustomer::order(const std::vector<Dish> &menu) {
    std::vector<int> orders;
    if(!ordered) {
        int index = 0;
        std::vector<Dish, std::allocator<Dish>>::const_iterator it;
        for (it = menu.begin(); it != menu.end(); ++it) {
            if ((*it).getPrice() < menu[index].getPrice()) {
                index = (*it).getId();
            }
        }
        orders.push_back(index);
        ordered = true;
    }
    return orders;
}


std::string CheapCustomer::toString() const {return getName()+",chp";}

//Spicy Customer
SpicyCustomer::SpicyCustomer(std::string name, int id) :Customer(name, id), firstorder(true){}

std::vector<int> SpicyCustomer::order(const std::vector<Dish> &menu) {
    std::vector<int> orders;
    //int price = 0;
    //int index = -1;
    int index=0,price=-1;
    bool found=false;

    if(firstorder){
        for(auto d : menu){
            if (d.getType() == SPC && (price == -1 || d.getPrice() > price)) {
                found = true;
                index = d.getId();
                price = d.getPrice();
            }
        }

        if(found) {
            orders.push_back(index);
            firstorder = false;
        }
    }
    else{
        for(auto d : menu){
            if (d.getType() == BVG && (price == -1 || d.getPrice() < price))  {
                found = true;
                index = d.getId();
                price = d.getPrice();
            }
        }

        if(found)
            orders.push_back(index);

    }
    return orders;
}

std::string SpicyCustomer::toString() const {return getName()+",spc";}

//Alcoholic Customer
AlchoholicCustomer::AlchoholicCustomer(std::string name, int id) :Customer(name, id), index(-1){}

std::vector<int> AlchoholicCustomer::order(const std::vector<Dish> &menu) {
    std::vector<int> orders;
    std::vector<Dish*> alcMenu;
    for (auto d: menu) {
        if (d.getType() == ALC) {
            alcMenu.push_back(new Dish(d.getId(), d.getName(), d.getPrice(), d.getType()));
        }
    }
    std::stable_sort(alcMenu.begin(), alcMenu.end(), [ ](const Dish* lhs, const Dish* rhs) -> bool {
        return lhs->getPrice() < rhs->getPrice();
    });

    if(index < (int)alcMenu.size()-1) {
        index++;
        orders.push_back(alcMenu[index]->getId());
    }
    for (auto d: alcMenu){
        delete d;
    }
    return orders;
}


std::string AlchoholicCustomer::toString() const {return getName()+",alc";}