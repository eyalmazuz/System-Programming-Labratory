//
// Created by eyal on 10/31/18.
//

#include "../include/Dish.h"

Dish::Dish(int d_id, std::string d_name, int d_price, DishType d_type) : id(d_id), name(d_name), price(d_price), type(d_type){}

int Dish::getId() const { return id; }

std::string Dish::getName() const { return name; }

int Dish::getPrice() const {return price; }

DishType Dish::getType() const {return type; }

std::string Dish::getStrType() const {
    std::string s;
    switch (getType()) {
        case VEG:
            s = "VEG";
            break;
        case SPC:
            s = "SPC";
            break;
        case BVG:
            s = "BVG";
            break;
        case ALC:
            s = "ALC";
            break;

    }
    return s;
}
