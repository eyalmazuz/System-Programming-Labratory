cmake_minimum_required(VERSION 3.12)
project(Assignment_1)

set(CMAKE_CXX_STANDARD 11)

add_executable(Assignment_1 src/Main.cpp
        include/Action.h
        include/Customer.h
        include/Dish.h
        include/Restaurant.h
        include/Table.h
        src/Customer.cpp
        src/Dish.cpp
        src/Table.cpp
        src/Restaurant.cpp
        src/Action.cpp include/Logger.h src/Logger.cpp)