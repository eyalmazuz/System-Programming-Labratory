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
    int price = 0;
    int index = 0;
    std::vector<Dish, std::allocator<Dish>>::const_iterator it;
    for (it = menu.begin(); it != menu.end(); ++it) {
        if ((*it).getType() == VEG) {
            index = (*it).getId();
        }
    }
    orders.push_back(index);
    for (unsigned int i = 0; i < menu.size(); i++) {
        if (menu[i].getPrice() > price && menu[i].getType() == BVG) {
            index = i;
        }
    }
    orders.push_back(index);
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
    int price = 0;
    int index = 0;
    if(firstorder){
        std::vector<Dish, std::allocator<Dish>>::const_iterator it;
        for (it = menu.begin(); it != menu.end(); ++it) {
            if ((*it).getPrice() > price && (*it).getType() == SPC) {
                index = (*it).getId();
            }
        }
        orders.push_back(index);
        firstorder = false;
    }
    else{
        int index = 0;
        std::vector<Dish, std::allocator<Dish>>::const_iterator it;
        for (it = menu.begin(); it != menu.end(); ++it) {
            if ((*it).getPrice() < menu[index].getPrice()  && (*it).getType() == BVG) {
                index = (*it).getId();
            }
        }
        orders.push_back(index);
    }
    return orders;
}

std::string SpicyCustomer::toString() const {return getName()+",spc";}

//Alcoholic Customer
AlchoholicCustomer::AlchoholicCustomer(std::string name, int id) :Customer(name, id), index(0), dishId(-1){}

std::vector<int> AlchoholicCustomer::order(const std::vector<Dish> &menu) {
    std::vector<int> orders;
    std::vector<int> alcMenu;
    for (auto d: menu) {
        if (d.getType() == ALC) {
            alcMenu.push_back(d.getPrice());
        }
    }
    std::sort(alcMenu.begin(), alcMenu.end());
    int price = alcMenu[index];
    for (unsigned int i = 0; i < menu.size(); ++i) {
        if(price == menu[i].getPrice() && menu[i].getType() == ALC && dishId != menu[i].getId()){
            dishId = menu[i].getId();
            index++;
            orders.push_back(dishId);
            break;
        }
    }
    return orders;
}


std::string AlchoholicCustomer::toString() const {return getName()+",alc";}