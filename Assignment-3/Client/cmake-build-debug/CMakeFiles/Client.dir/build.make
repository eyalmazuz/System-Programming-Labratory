# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.12

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /users/studs/bsc/2019/mazuze/Desktop/CLion-2018.2.5/clion-2018.2.5/bin/cmake/linux/bin/cmake

# The command to remove a file.
RM = /users/studs/bsc/2019/mazuze/Desktop/CLion-2018.2.5/clion-2018.2.5/bin/cmake/linux/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/Client.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/Client.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/Client.dir/flags.make

CMakeFiles/Client.dir/src/connectionHandler.cpp.o: CMakeFiles/Client.dir/flags.make
CMakeFiles/Client.dir/src/connectionHandler.cpp.o: ../src/connectionHandler.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/Client.dir/src/connectionHandler.cpp.o"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/Client.dir/src/connectionHandler.cpp.o -c /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/connectionHandler.cpp

CMakeFiles/Client.dir/src/connectionHandler.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Client.dir/src/connectionHandler.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/connectionHandler.cpp > CMakeFiles/Client.dir/src/connectionHandler.cpp.i

CMakeFiles/Client.dir/src/connectionHandler.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Client.dir/src/connectionHandler.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/connectionHandler.cpp -o CMakeFiles/Client.dir/src/connectionHandler.cpp.s

CMakeFiles/Client.dir/src/echoClient.cpp.o: CMakeFiles/Client.dir/flags.make
CMakeFiles/Client.dir/src/echoClient.cpp.o: ../src/echoClient.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object CMakeFiles/Client.dir/src/echoClient.cpp.o"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/Client.dir/src/echoClient.cpp.o -c /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/echoClient.cpp

CMakeFiles/Client.dir/src/echoClient.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Client.dir/src/echoClient.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/echoClient.cpp > CMakeFiles/Client.dir/src/echoClient.cpp.i

CMakeFiles/Client.dir/src/echoClient.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Client.dir/src/echoClient.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/echoClient.cpp -o CMakeFiles/Client.dir/src/echoClient.cpp.s

CMakeFiles/Client.dir/src/Task.cpp.o: CMakeFiles/Client.dir/flags.make
CMakeFiles/Client.dir/src/Task.cpp.o: ../src/Task.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building CXX object CMakeFiles/Client.dir/src/Task.cpp.o"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/Client.dir/src/Task.cpp.o -c /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/Task.cpp

CMakeFiles/Client.dir/src/Task.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Client.dir/src/Task.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/Task.cpp > CMakeFiles/Client.dir/src/Task.cpp.i

CMakeFiles/Client.dir/src/Task.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Client.dir/src/Task.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/Task.cpp -o CMakeFiles/Client.dir/src/Task.cpp.s

CMakeFiles/Client.dir/src/messageEncoder.cpp.o: CMakeFiles/Client.dir/flags.make
CMakeFiles/Client.dir/src/messageEncoder.cpp.o: ../src/messageEncoder.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building CXX object CMakeFiles/Client.dir/src/messageEncoder.cpp.o"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/Client.dir/src/messageEncoder.cpp.o -c /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/messageEncoder.cpp

CMakeFiles/Client.dir/src/messageEncoder.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Client.dir/src/messageEncoder.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/messageEncoder.cpp > CMakeFiles/Client.dir/src/messageEncoder.cpp.i

CMakeFiles/Client.dir/src/messageEncoder.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Client.dir/src/messageEncoder.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/src/messageEncoder.cpp -o CMakeFiles/Client.dir/src/messageEncoder.cpp.s

# Object files for target Client
Client_OBJECTS = \
"CMakeFiles/Client.dir/src/connectionHandler.cpp.o" \
"CMakeFiles/Client.dir/src/echoClient.cpp.o" \
"CMakeFiles/Client.dir/src/Task.cpp.o" \
"CMakeFiles/Client.dir/src/messageEncoder.cpp.o"

# External object files for target Client
Client_EXTERNAL_OBJECTS =

Client: CMakeFiles/Client.dir/src/connectionHandler.cpp.o
Client: CMakeFiles/Client.dir/src/echoClient.cpp.o
Client: CMakeFiles/Client.dir/src/Task.cpp.o
Client: CMakeFiles/Client.dir/src/messageEncoder.cpp.o
Client: CMakeFiles/Client.dir/build.make
Client: /usr/lib/x86_64-linux-gnu/libboost_filesystem.so
Client: /usr/lib/x86_64-linux-gnu/libboost_thread.so
Client: /usr/lib/x86_64-linux-gnu/libboost_system.so
Client: /usr/lib/x86_64-linux-gnu/libboost_chrono.so
Client: /usr/lib/x86_64-linux-gnu/libboost_date_time.so
Client: /usr/lib/x86_64-linux-gnu/libboost_atomic.so
Client: CMakeFiles/Client.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Linking CXX executable Client"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Client.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/Client.dir/build: Client

.PHONY : CMakeFiles/Client.dir/build

CMakeFiles/Client.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/Client.dir/cmake_clean.cmake
.PHONY : CMakeFiles/Client.dir/clean

CMakeFiles/Client.dir/depend:
	cd /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/cmake-build-debug /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/cmake-build-debug /users/studs/bsc/2019/mazuze/Desktop/Programming/System-Programming-Labratory/Assignment-3/Client/cmake-build-debug/CMakeFiles/Client.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/Client.dir/depend
