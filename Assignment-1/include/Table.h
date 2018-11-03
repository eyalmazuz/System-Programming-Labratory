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
    void order(const std::vector<Dish> &menu);
    void openTable();
    void closeTable();
    void calculateBill();
    int getBill();
    bool isOpen();
    int getCustomersNum() const;
    int getId() const;
    bool isFull();

    //rule of 5
    virtual ~Table();
    Table & operator=(const Table &other);
    Table (const Table &other);
    Table & operator=(Table &&other);
    Table (Table &&other);


private:
    int bill;
    int id;
    int capacity;
    bool open;
    std::vector<Customer*> customersList;
    std::vector<OrderPair> orderList; //A list of pairs for each order in a table - (customer_id, Dish)
};


#endif