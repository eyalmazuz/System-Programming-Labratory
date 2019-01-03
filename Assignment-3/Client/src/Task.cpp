//
// Created by itay on 19/12/18.
//

#include "../include/Task.h"
#include "../include/messageDecoder.h"

Task::Task(ConnectionHandler *connectionHandler, std::atomic<bool>* isConnected) :connectionHandler(connectionHandler), isConnected(isConnected){
}

void Task::operator()() {
    messageDecoder messageDecode;
    while(1){
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        //isConnected->store(false);
        char tmp[1024];
        if (!connectionHandler->getBytes(tmp,1)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
        // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        //answer.resize(answer.length()-1);
        std::string answer = messageDecode.decodeNextByte(tmp[0]);
        if(answer=="ACK 2") {
            isConnected->store(true);
        }

        if (answer.length() > 0) {
            std::cout << answer << std::endl;
        }
        if (answer == "ACK 3") {
            connectionHandler->close();
            std::cout << "Exiting...\n" << std::endl;
            break;

        }

    }
}







