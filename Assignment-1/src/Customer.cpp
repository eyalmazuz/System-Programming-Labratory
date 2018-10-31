//
// Created by eyal on 10/31/18.
//
#include "../include/Customer.h"


//Customer abstract class

Customer::Customer(std::string c_name, int c_id) : name(c_name), id(c_id){}

std::string Customer::getName() const { return  name; }

int Customer::getId() const { return id; }

//Vegetarian Customer
VegetarianCustomer::VegetarianCustomer(std::string name, int id) :Customer(name, id){}



//Cheap Customer
CheapCustomer::CheapCustomer(std::string name, int id) :Customer(name, id){}



//Spicy Customer
SpicyCustomer::SpicyCustomer(std::string name, int id) :Customer(name, id){}


//Alcoholic Customer
AlchoholicCustomer::AlchoholicCustomer(std::string name, int id) :Customer(name, id){}
