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
    for (it = menu.begin(); it != menu.end(); ++it) {
        if ((*it).getPrice() > price && (*it).getType() == BVG) {
            index = (*it).getId();
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
AlchoholicCustomer::AlchoholicCustomer(std::string name, int id) :Customer(name, id), ordered(false), prevPrice(0), done(false){}

std::vector<int> AlchoholicCustomer::order(const std::vector<Dish> &menu) {
    if(!done) {
        std::vector<int> orders;
        int index = 0;
        std::vector<Dish, std::allocator<Dish>>::const_iterator it;
        for (it = menu.begin(); it != menu.end(); ++it) {
            if ((*it).getPrice() > prevPrice && (*it).getType() == ALC) {
                index = (*it).getId();
                ordered = true;
                break;
            }
        }
        if (ordered && prevPrice != menu[index].getPrice()) {
            prevPrice = menu[index].getPrice();
            orders.push_back(index);

        }else{
            done = true;
        }
        ordered = false;
        return orders;
    }
}

std::string AlchoholicCustomer::toString() const {return getName()+",alc";}
