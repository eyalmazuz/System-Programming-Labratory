CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -fPIC

all: EchoClient
	g++ -o bin/echoClient bin/connectionHandler.o bin/echoClient.o bin/Task.o bin/messageEncoder.o bin/messageDecoder.o $(LDFLAGS)

EchoClient: bin/connectionHandler.o bin/echoClient.o bin/Task.o bin/messageEncoder.o bin/messageDecoder.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/Task.o: src/Task.cpp
	g++ $(CFLAGS) -o bin/Task.o src/Task.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/messageEncoder.o: src/messageEncoder.cpp
	g++ $(CFLAGS) -o bin/messageEncoder.o src/messageEncoder.cpp

bin/messageDecoder.o: src/messageDecoder.cpp
	g++ $(CFLAGS) -o bin/messageDecoder.o src/messageDecoder.cpp

.PHONY: clean
clean:
	rm -f bin/*
