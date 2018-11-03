//
// Created by itay on 02/11/18.
//

#ifndef ASSIGNMENT_1_LOGGER_H
#define ASSIGNMENT_1_LOGGER_H

#include <string>

class Logger{
public:
    Logger(const std::string &text);
    Logger();
    void append(const std:: string &text);
    void clear();
    const std::string &getText() const;

private:
    std::string text;

};

#endif //ASSIGNMENT_1_LOGGER_H
