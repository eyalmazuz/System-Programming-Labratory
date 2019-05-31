//
// Created by itay on 27/12/18.
//

#include <string>
#include <map>
#include <vector>

#ifndef CLIENT_MESSAGEDECODER_H
#define CLIENT_MESSAGEDECODER_H

#endif //CLIENT_MESSAGEDECODER_H

enum serverOpcodes{
    NOTIFICATION=9,
    ACK=10,
    ERROR=11
};

inline const char* ToString(serverOpcodes v)
{
    switch (v)
    {
        case NOTIFICATION: return "NOTIFICATION";
        case ACK:   return "ACK";
        case ERROR: return "ERROR";
        default: return "[Unknown OP_type]";
    }
}

class messageDecoder {
private:
    char bytes[1024];
    int len;
    int zeroCounter;
    char zeroByte = '\0';
    char byte;
    short bytesToShort(char* bytesArr,int start);
    std::string decodeNotification();
    std::string decodeAck();
    std::string decodeError();
    //int bytesLength(char *bytesArr);
public:
    messageDecoder();
    std::string decodeNextByte(char byte);
};