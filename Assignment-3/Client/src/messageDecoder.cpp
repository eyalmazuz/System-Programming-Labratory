//
// Created by itay on 27/12/18.
//

#include <cstring>
#include "../include/messageDecoder.h"
#include "../include/messageEncoder.h"

messageDecoder::messageDecoder() :len(0), zeroCounter(0), byte(0) {}

std::string messageDecoder::decodeNextByte(char byte) {
    bytes[len++] = byte;
    this->byte = byte;

    if (len < 4)return "";

    serverOpcodes op = static_cast<serverOpcodes>(bytesToShort(bytes,0));
    std::string ans;
    switch (op){

        case NOTIFICATION:
            ans = decodeNotification();
            break;
        case ACK:
            ans = decodeAck();
            break;
        case ERROR:
            ans = decodeError();
            break;
    }

    if (ans.length() > 0) {
        memset(bytes, 0, sizeof(bytes));
        len = 0;
        zeroCounter = 0;
    }

    return ans;
}

short messageDecoder::bytesToShort(char *bytesArr,int start) {
    short result = (short)((bytesArr[start] & 0xff) << 8);
    result += (short)(bytesArr[start+1] & 0xff);
    return result;
}

std::string messageDecoder::decodeNotification() {
    if (byte == zeroByte) zeroCounter++;
    if (zeroCounter < 2) return "";
    std::string ans(ToString(NOTIFICATION));
    ans.append(bytes[2] == 0 ? " PM " : " Public ");
    int i = 3;
    while (bytes[i] != zeroByte)
    {
        ans += bytes[i];
        ++i;
    }
    ans += " ";
    i++;
    while (bytes[i] != zeroByte)
    {
        ans += bytes[i];
        ++i;
    }
    return ans;
}

std::string messageDecoder::decodeAck() {
    std::string ans(ToString(ACK));
    clientOpcodes op = static_cast<clientOpcodes>(bytesToShort(bytes,2));
    ans.append(" " + std::to_string(bytesToShort(bytes,2)));
    switch (op){
        case REGISTER: case LOGIN: case LOGOUT: case POST: case PM:
            break;
        case STAT:
            if (len < 10)
                return "";
            ans.append(" " + std::to_string(bytesToShort(bytes,4))); //number of posts
            ans.append(" " + std::to_string(bytesToShort(bytes,6))); //number of followers
            ans.append(" " + std::to_string(bytesToShort(bytes,8))); //number of following
            break;
        case FOLLOW: case USERLIST:
            //if len lower than 6 then we don't know the size of user list
            if (len < 6)return "";
            if (byte == zeroByte) zeroCounter++;
            if (zeroCounter < bytesToShort(bytes,4))return "";

            ans.append(" " + std::to_string(bytesToShort(bytes,4))+ " "); //number of users
            int i = 0;
            while (zeroCounter > 0){
                if (bytes[6+i] != zeroByte)
                    ans += bytes[6+i];
                else {
                    ans += " ";
                    zeroCounter--;
                }
                i++;
            }
            break;
    }

    return ans;

}

std::string messageDecoder::decodeError() {
    std::string ans(ToString(ERROR));
    ans.append(" " + std::to_string(bytesToShort(bytes,2)));
    return ans;
}

//int messageDecoder::bytesLength(char *bytesArr) {
//    return  std::strlen(bytesArr);//sizeof(bytesArr) / sizeof(bytesArr[0]);
//}


