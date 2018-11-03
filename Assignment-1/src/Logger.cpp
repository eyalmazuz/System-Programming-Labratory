//
// Created by itay on 02/11/18.
//

#include "../include/Logger.h"



Logger::Logger() {

}

Logger::Logger(const std::string &text) : text(text) {}

void Logger::append(const std::string &txt) {
    text.append(txt);
}

void Logger::clear() {text.clear();}

const std::string &Logger::getText() const {
    return text;
}

