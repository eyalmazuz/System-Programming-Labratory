//
// Created by itay on 19/12/18.
//

#ifndef CLIENT_TASK_H
#define CLIENT_TASK_H

#endif //CLIENT_TASK_H

#include "connectionHandler.h"

class Task {
private:
    ConnectionHandler* connectionHandler;
    std::atomic<bool> *isConnected;
public:
    Task(ConnectionHandler* connectionHandler, std::atomic<bool> *isConnected);
    void operator()();

};



