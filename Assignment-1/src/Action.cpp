//
// Created by eyal on 10/31/18.
//

#include "../include/Action.h"
#include "../include/Restaurant.h"
extern Restaurant* backup;


//Base Action

BaseAction::BaseAction() :status(PENDING) { }
ActionStatus BaseAction::getStatus() const { return status; }
std::string BaseAction::getErrorMsg() const { return errorMsg; }
BaseAction::~BaseAction() {}
void BaseAction::complete() { status = COMPLETED; }
void BaseAction::error(std::string errorMsg) { this->errorMsg =  errorMsg; }
void BaseAction::setError() { status = ERROR; }
std::string BaseAction::convertStatus(ActionStatus actionStatus) const {
    if (actionStatus == COMPLETED) return "Completed";
    else if (actionStatus == ERROR ) return  "Error: ";
    else if (actionStatus == PENDING) return "Pending";
}

//Open Table

OpenTable::OpenTable(int id, std::vector<Customer *> &customersList) : tableId(id), customers(customersList) {
    error("Table is already open");
}

void OpenTable::act(Restaurant &restaurant) {
    restaurant.getTables()[tableId]->openTable();
    for (int i = 0; i < customers.size(); ++i) {
        restaurant.getTables()[tableId]->addCustomer(customers[i]);

    }
    complete();
}

std::string OpenTable::toString() const {
    std::string s ="open " +  std::to_string(tableId+1) + " ";
    for (int i = 0; i < customers.size(); i++){
        s += customers[i]->toString() + " ";
    }
    if(getStatus() == COMPLETED){
        s += convertStatus(getStatus());
    }else if( getStatus() == ERROR){
        s += convertStatus(getStatus()) + getErrorMsg();
    }
    return s;
}

int OpenTable::gettableId() const { return tableId; }

std::vector<Customer*> OpenTable::getCustomers() const {return customers; }

OpenTable::~OpenTable() {
/*   for (int i = 0; i < customers.size(); ++i) {
        delete customers[i];
    }*/
}

//Order

Order::Order(int id) : tableId(id) {}

void Order::act(Restaurant &restaurant) {
    Table &t = *restaurant.getTables()[tableId];
    std::vector<Customer *>::iterator it;
    for (it = t.getCustomers().begin(); it != t.getCustomers().end(); ++it) {
        std::vector<int> orders = (*it)->order(restaurant.getMenu());
        for (int i = 0; i < orders.size(); ++i) {
            OrderPair::first_type f = (*it)->getId();
            OrderPair::second_type s = restaurant.getMenu()[orders[i]];
            OrderPair order = std::make_pair(f, s);
            t.getOrders().push_back(order);
            std::cout << (*it)->getName() + " ordered " + restaurant.getMenu()[orders[i]].getName() << std::endl;
        }
    }
    complete();
}

std::string Order::toString() const {
    std::string s = "order " + std::to_string(tableId+1 ) + " " + convertStatus(getStatus());
    return  s;

}

Order::~Order() {}

int Order::getTableId() const {return tableId; }

//Move

MoveCustomer::MoveCustomer(int src, int dst, int customerId) : srcTable(src), dstTable(dst), id(customerId){
    error("Cannot move customers");
}

void MoveCustomer::act(Restaurant &restaurant) {
    int customerId = id;
    std::vector<OrderPair> newOrderList;
    Table &src = *restaurant.getTables()[srcTable];
    Table &dest = *restaurant.getTables()[dstTable];
    dest.addCustomer(src.getCustomer(customerId));
    std::vector<OrderPair>::iterator it;
    for (it = src.getOrders().begin(); it != src.getOrders().end(); ++it) {
        if ((*it).first == customerId) {
            dest.getOrders().push_back((*it));

        } else {
            newOrderList.push_back(*it);
        }
    }
    src.removeCustomer(customerId);
    src.changeOrderList(newOrderList);
    if (src.getCustomers().size() == 0) {
        src.closeTable();
    }
    complete();

}

std::string MoveCustomer::toString() const {
    std::string s = "move " + std::to_string(srcTable+1) + " " + std::to_string(dstTable+1)+ " " + std::to_string(id)+ " ";
    if(getStatus() == COMPLETED){
        s+= convertStatus(getStatus());
    }else if (getStatus() == ERROR){
        s+= convertStatus(getStatus()) + getErrorMsg();
    }
    return s;
}

int MoveCustomer::getId() const {return id;}

int MoveCustomer::getdstTable() const {return dstTable; }

int MoveCustomer::getsrcTable() const { return srcTable; }

//Print Table Status

PrintTableStatus::PrintTableStatus(int id) :tableId(id){}

void PrintTableStatus::act(Restaurant &restaurant) {
    int sum = 0;
    int id = tableId;
    std::string status = "closed";
    if (restaurant.getTables()[tableId]->isOpen()) {
        status = "open";
        std::cout << "Table " + std::to_string(tableId + 1) + " status: " + status << std::endl;
        std::cout << "Customers:" << std::endl;
        for (int i = 0; i < restaurant.getTables()[tableId]->getCustomers().size(); i++) {
            std::cout << std::to_string(restaurant.getTables()[tableId]->getCustomers()[i]->getId()) + " " +
                    restaurant.getTables()[tableId]->getCustomers()[i]->getName() << std::endl;
        }
        std::cout << "Orders:" << std::endl;
        for (int j = 0; j < restaurant.getTables()[tableId]->getOrders().size(); ++j) {
            std::cout << restaurant.getTables()[tableId]->getOrders()[j].second.getName() + " " +
                         std::to_string(restaurant.getTables()[tableId]->getOrders()[j].second.getPrice()) + "NIS " +
                         std::to_string(restaurant.getTables()[tableId]->getOrders()[j].first) << std::endl;
            sum += restaurant.getTables()[tableId]->getOrders()[j].second.getPrice();
        }
        std::cout << "Current Bill: " + std::to_string(sum) + "NIS" << std::endl;
    } else {
        std::cout << "Table " + std::to_string(tableId + 1) + " status: " + status << std::endl;
    }
    complete();
}

std::string PrintTableStatus::toString() const { return "status " + std::to_string(tableId+1) +" "+ convertStatus(getStatus());}

int PrintTableStatus::gettableId() const { return tableId;}
//Close table

Close::Close(int id) : tableId(id) {}

std::string Close::toString() const {return "";}

void Close::act(Restaurant &restaurant) {
    int sum = 0;
    int id = tableId;
    for (int j = 0; j < restaurant.getTables()[id]->getOrders().size(); ++j) {
        sum += restaurant.getTables()[id]->getOrders()[j].second.getPrice();
    }
    std::cout << "Table " + std::to_string(tableId + 1) + " was closed. Bill " + std::to_string(sum) + "NIS"
              << std::endl;
    restaurant.getTables()[id]->closeTable();
    complete();
}

int Close::gettableId() const {return tableId;}

//Close All

CloseAll::CloseAll() {}

void CloseAll::act(Restaurant &restaurant) {
    for (int i = 0; i < restaurant.getTables().size(); ++i) {
        if (restaurant.getTables()[i]->isOpen()) {
            restaurant.getTables()[i]->closeTable();
        }
    }
    complete();
/*    delete backup;
    backup = nullptr;*/
}

std::string CloseAll::toString() const { return ""; }

//Print Menu

PrintMenu::PrintMenu() {}

std::string PrintMenu::toString() const {return ""; }

std::string PrintMenu::reverseConvert(DishType type) {
    if (type == ALC) return "ALC";
    else if (type == BVG) return "BVG";
    else if (type == VEG) return "VEG";
    else if (type == SPC) return "SPC";
}

void PrintMenu::act(Restaurant &restaurant) {
    for (int i = 0; i < restaurant.getMenu().size(); i++) {
        std::cout << restaurant.getMenu()[i].getName() + " " + reverseConvert(restaurant.getMenu()[i].getType()) + " " +
                     std::to_string(restaurant.getMenu()[i].getPrice()) + "NIS" << std::endl;
    }
    complete();
}

//Backup

BackupRestaurant::BackupRestaurant() {}

std::string BackupRestaurant::toString() const { return "backup " + convertStatus(getStatus());}

void BackupRestaurant::act(Restaurant &restaurant) {
    backup = &restaurant;
    complete();
}

//Restore

RestoreResturant::RestoreResturant() {}

std::string RestoreResturant::toString() const { return "restore " + convertStatus(getStatus());}

void RestoreResturant::act(Restaurant &restaurant) {
    restaurant = *backup;
}

//Print Action Log

PrintActionsLog::PrintActionsLog() {}

std::string PrintActionsLog::toString() const { return "";}

void PrintActionsLog::act(Restaurant &restaurant) {
    for (int i = 0; i < restaurant.getActionsLog().size(); i++) {
        std::cout << restaurant.getActionsLog()[i]->toString() << std::endl;
    }
    complete();
}