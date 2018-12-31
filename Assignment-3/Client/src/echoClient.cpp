#include <stdlib.h>
#include <thread>
#include "../include/connectionHandler.h"
#include "../include/Task.h"
#include "../include/messageEncoder.h"
#include <boost/thread.hpp>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/

//TODO DELETE THIS
struct HexCharStruct
{
    unsigned char c;
    HexCharStruct(unsigned char _c) : c(_c) { }
};

inline std::ostream& operator<<(std::ostream& o, const HexCharStruct& hs)
{
    return (o << std::hex << (int)hs.c);
}

inline HexCharStruct hex(unsigned char _c)
{
    return HexCharStruct(_c);
}

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    //TODO Check about client termination when sending LOGOUT message
    // add 2 thread client support
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    std::atomic<bool> isConnected(false);
    Task listen(&connectionHandler, &isConnected);
    boost::thread listen_toServer_thread{listen};
    messageEncoder encoder;

    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);

        if (line == "") break;
        std::vector<char> bytes(encoder.encode(line));
        char c[bytes.size()];
        std::copy(bytes.begin(), bytes.end(), c);
        if(!connectionHandler.sendBytes(c,bytes.size())){
            std::cout<< "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
        std::cout << "Sent " << bytes.size()+1 << " bytes to server" << std::endl;
        if(isConnected.load() && line=="LOGOUT") {
            break;
        }

    }

    listen_toServer_thread.join();

    return 0;
}
