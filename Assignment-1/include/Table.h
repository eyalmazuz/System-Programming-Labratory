#ifndef TABLE_H_
#define TABLE_H_

#include <vector>
#include "Customer.h"
#include "Dish.h"

typedef std::pair<int, Dish> OrderPair;

class Table{
public:
    Table(int t_capacity);
    int getCapacity() const;
    void addCustomer(Customer* customer);
    void addCustomers(const std::vector<Customer *> &vector);
    void removeCustomer(int id);
    Customer* getCustomer(int id);
    std::vector<Customer*>& getCustomers();
    std::vector<OrderPair>& getOrders();
    //std::vector<OrderPair>& getOrders(int id);
    void updateOrder(const std::vector<OrderPair> &otherOrderList);
    void order(const std::vector<Dish> &menu);
    void openTable();
    void closeTable();
    void calculateBill();
    int getBill();
    bool isOpen();
    int getCustomersNum() const;
    int getId() const;
    bool isFull();
    void setId(int id);

    //rule of 5
    virtual ~Table();
    Table & operator=(const Table &other);
    Table (const Table &other);
    Table & operator=(Table &&other);
    Table (Table &&other);


    void removeOrders(const std::vector<OrderPair> &otherOrderList);

private:
    int capacity;
    bool open;
    std::vector<Customer*> customersList;
    std::vector<OrderPair> orderList; //A list of pairs for each order in a table - (customer_id, Dish)
    int bill;
    int id;

    void clean() const;
    void copy(const Table &other);
    void steal(Table &other);
};


#endif