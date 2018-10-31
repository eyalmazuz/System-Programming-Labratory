# define some Makefile variables for the compiler and compiler flags
# to use Makefile variables later in the Makefile: $()
CC = g++
CFLAGS  = -g -Wall -Weffc++ -std=c++11
LFLAGS  = -L/usr/lib

# All Targets
all: rest

# Tool invocations
# Executable "hello" depends on the files hello.o and run.o.
rest: 
	@echo 'Building target: hello'
	@echo 'Invoking: C++ Linker'
	$(CC) -o bin/rest  $(LFLAGS)
	@echo 'Finished building target: hello'
	@echo ' '

# Depends on the source and header files

	$(CC) $(CFLAGS) -c -Iinclude -o 

# Depends on the source and header files 

	$(CC) $(CFLAGS) -c -Iinclude -o

#Clean the build directory
clean: 
	rm -f bin/*
