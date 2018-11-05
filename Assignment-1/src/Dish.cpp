//
// Created by eyal on 10/31/18.
//

#include "../include/Dish.h"

Dish::Dish(int d_id, std::string d_name, int d_price, DishType d_type) : id(d_id), name(d_name), price(d_price), type(d_type){}

int Dish::getId() const { return id; }

std::string Dish::getName() const { return name; }

int Dish::getPrice() const {return price; }

DishType Dish::getType() const {return type; }

/*
Dish::~Dish() {

}

Dish &Dish::operator=(const Dish &other) {
    return *this;
}
*/

//Dish::Dish(const Dish &other) : id(other.id), name(other.name), price(other.price), type(other.type) {
//
//}

//Dish::Dish(Dish &&other) : id(other.id), name(other.name), price(other.price), type(other.type) {
//
//}

//Dish &Dish::operator=(Dish &&other) {
//    return *this;
//}

