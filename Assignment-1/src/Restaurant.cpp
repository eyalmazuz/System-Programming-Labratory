//
// Created by eyal on 10/31/18.
//

#include "../include/Restaurant.h"

Restaurant::Restaurant() { }
Restaurant::Restaurant(const std::string &configFilePath) { }

Restaurant::~Restaurant() { }

int Restaurant::getNumOfTables() const { return tables.size(); }
std::vector<Dish>& Restaurant::getMenu() { return menu; }
const std::vector<BaseAction*>& Restaurant::getActionsLog() const { return actionsLog; }

void Restaurant::start() {

    std::string Action;
    std::cin >> Action;

    while(Action != "closeall"){

    }
}