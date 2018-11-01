//
// Created by eyal on 10/31/18.
//

#include "../include/Action.h"


BaseAction::BaseAction() { }
ActionStatus BaseAction::getStatus() const { return status; }
std::string BaseAction::getErrorMsg() const { return errorMsg; }

void BaseAction::complete() { status = COMPLETED; }
void BaseAction::error(std::string errorMsg) { std::cout << "Error:" << errorMsg << std::endl; }
