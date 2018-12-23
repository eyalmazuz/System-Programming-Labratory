#include <stdlib.h>
#include <thread>
#include "../include/connectionHandler.h"
#include "../include/Task.h"
#include "../include/message.h"
#include <boost/thread.hpp>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    //TODO add support to read messages from the socket
    // add 2 thread client support
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    Task listen(&connectionHandler);
    boost::thread listen_toServer_thread{listen};

    while (1){
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        messageEncoder *encoder = messageSelector::getClientMessage(line);
        if (encoder == nullptr){
            std::cout << "Invalid Command !!!\n" << std::endl;
            continue;
        }
        std::string out = encoder->encode(line);
        if (!connectionHandler.sendLine(out)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
        std::cout << "Sent " << out.length()+1 << " bytes to server" << std::endl;

    }

    listen_toServer_thread.join();

    return 0;
}
