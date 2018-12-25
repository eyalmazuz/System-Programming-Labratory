#ifndef ACTION_H_
#define ACTION_H_

#include <string>
#include <iostream>
#include <sstream>
#include "Customer.h"

enum ActionStatus{
    PENDING, COMPLETED, ERROR
};


//Forward declaration
class Restaurant;

extern Restaurant* backup;


class BaseAction{
public:
    BaseAction();
    ActionStatus getStatus() const;
    std::string getStrStatus() const;
    virtual void act(Restaurant& restaurant)=0;
    virtual std::string toString() const=0;
    void setError(const std::string &errorMessage);
    void setErrorMsg(const std::string &errorMsg);
    const std::string &getLogger() const;
    virtual ~BaseAction();
    void setLogger(const std::string &logger);
    void appendLogger(const std::string &data);
    void changeStatus(const ActionStatus &actionStatus);

protected:
    void complete();
    //void complete(std::string out);
    void error(std::string errorMsg);
    std::string getErrorMsg() const;
private:
    std::string logger;
    std::string errorMsg;
    ActionStatus status;
};


class OpenTable : public BaseAction {
public:
    OpenTable(int id, std::vector<Customer *> &customersList);
    void act(Restaurant &restaurant);
    std::string toString() const;

    const int getTableId() const;

    const std::vector<Customer *> &getCustomers() const;

private:
    const int tableId;
    std::vector<Customer *> customers;
};


class Order : public BaseAction {
public:
    Order(int id);
    void act(Restaurant &restaurant);
    std::string toString() const;

    const int getTableId() const;

private:
    const int tableId;
};


class MoveCustomer : public BaseAction {
public:
    MoveCustomer(int src, int dst, int customerId);
    void act(Restaurant &restaurant);
    std::string toString() const;

    const int getSrcTable() const;
    const int getDstTable() const;
    const int getId() const;

private:
    const int srcTable;
    const int dstTable;
    const int id;
};


class Close : public BaseAction {
public:
    Close(int id);
    void act(Restaurant &restaurant);
    std::string toString() const;

    const int getTableId() const;

private:
    const int tableId;
};


class CloseAll : public BaseAction {
public:
    CloseAll();
    void act(Restaurant &restaurant);
    std::string toString() const;
private:
};


class PrintMenu : public BaseAction {
public:
    PrintMenu();
    void act(Restaurant &restaurant);
    std::string toString() const;
private:
};


class PrintTableStatus : public BaseAction {
public:
    PrintTableStatus(int id);
    void act(Restaurant &restaurant);
    std::string toString() const;

    const int getTableId() const;

private:
    const int tableId;
};


class PrintActionsLog : public BaseAction {
public:
    PrintActionsLog();
    void act(Restaurant &restaurant);
    std::string toString() const;
private:
};


class BackupRestaurant : public BaseAction {
public:
    BackupRestaurant();
    void act(Restaurant &restaurant);
    std::string toString() const;
private:
};


class RestoreResturant : public BaseAction {
public:
    RestoreResturant();
    void act(Restaurant &restaurant);
    std::string toString() const;

};


#endif