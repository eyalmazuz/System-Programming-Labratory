cmake_minimum_required(VERSION 3.12)
project(Client)

set(CMAKE_CXX_STANDARD 11)

find_package(Boost 1.58.0 REQUIRED COMPONENTS
         filesystem thread)

if(Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIRS})
    add_executable(Client include/connectionHandler.h
            src/connectionHandler.cpp
            src/echoClient.cpp include/Task.h src/Task.cpp src/messageEncoder.cpp include/messageEncoder.h)
    target_link_libraries(Client ${Boost_LIBRARIES})
endif()


