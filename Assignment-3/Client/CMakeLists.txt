cmake_minimum_required(VERSION 3.12)
project(Client)

set(CMAKE_CXX_STANDARD 11)

add_executable(include/connectionHandler.h
        src/connectionHandler.cpp
        src/echoClient.cpp)
