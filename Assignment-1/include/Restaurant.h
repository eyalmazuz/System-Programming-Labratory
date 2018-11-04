#ifndef RESTAURANT_H_
#define RESTAURANT_H_

#include <vector>
#include <string>
#include "Dish.h"
#include "Table.h"
#include "Action.h"


class Restaurant{		
public:
	Restaurant();
    Restaurant(const std::string &configFilePath);
    void start();
    int getNumOfTables() const;
    Table* getTable(int ind);
	const std::vector<BaseAction*>& getActionsLog() const; // Return a reference to the history of actions
    std::vector<Dish>& getMenu();

    //rule of 5
    virtual ~Restaurant();
    Restaurant(Restaurant &other);
    Restaurant & operator=(Restaurant &other);
    Restaurant(Restaurant &&other);
    Restaurant &operator=(Restaurant &&other);

    //helper methods
    std::vector<Table*> getTables() const;

    void openCommand(std::vector<std::string> tokens);
    void orderCommand(std::vector<std::string> tokens);
    void moveCommand(std::vector<std::string> tokens);
    void closeCommand(std::vector<std::string> tokens);
    void printTableStatusCommand(std::vector<std::string> tokens);
    void printLogCommand(std::vector<std::string> tokens);
    void closeallCommand(std::vector<std::string> tokens);
    void printMenuCommand(std::vector<std::string> tokens);
    void backupCommand(std::vector<std::string> tokens);
    void restoreCommand(std::vector<std::string> tokens);

private:


	bool open;
	std::vector<Table*> tables;
	std::vector<Dish> menu;
	std::vector<BaseAction*> actionsLog;
	int custIndex;

	void readFile(const std::string &configFilePath);
    void readNumOfTables(std::ifstream &myReadFile);
	void readTables(std::ifstream &myReadFile);
	void readMenu(std::ifstream &myReadFile);


	DishType convert(std::string type);

	void copy(Restaurant &other);
	void steal(Restaurant &other);
	void clean();

	bool checkOpenValid(std::vector<std::string> tokens, Table &table);
	bool checkMoveValid(std::vector<std::string> tokens);
	bool checkValidInput(std::string input);
	bool checkValidCommand(std::vector<std::string> tokens);
	bool checkOrderValid(std::string index);
	bool checkCloseValid(std::string id);
};

#endif