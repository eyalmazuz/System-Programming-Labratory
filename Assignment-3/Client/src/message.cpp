//
// Created by itay on 21/12/18.
//

#include <sstream>
#include <vector>
#include <iterator>
#include <cstring>
#include "../include/message.h"

messageEncoder::messageEncoder() {}

void messageEncoder::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

//messageDecoder::messageDecoder() {}

//short messageDecoder::bytesToShort(char *bytesArr) {
//    short result = (short)((bytesArr[0] & 0xff) << 8);
//    result += (short)(bytesArr[1] & 0xff);
//    return result;
//}


std::string loginMessage::encode(std::string &in) {

    char bytesArr[2];
    char zeroByte = '0';

    messageClientToServerType type = LOGIN;
    shortToBytes((short) type,bytesArr);

    std::istringstream iss(in);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    std::string ans;
    ans.append(std::to_string(bytesArr[0]));
    ans.append(std::to_string(bytesArr[1]));
    ans.append(tokens[1]+zeroByte);
    ans.append(tokens[2]+zeroByte);
    return ans;
}

loginMessage::loginMessage() {

}

//std::string ackMessage::decode(std::string &out) {
//    //2 bytes - optcode. 1 - end of username. 1 - end of password.
//
//
//}

//ackMessage::ackMessage() {
//
//}


messageEncoder * messageSelector::getClientMessage(std::string &txt) {
    if (!checkValidInput(txt))
        return nullptr;

    std::istringstream iss(txt);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    messageClientTypeMap map;
    auto type = map[tokens[0]];
    switch (type){
        case LOGIN:return new loginMessage();
        case REGISTER:return new registerMessage();
        case LOGOUT:break;
        case FOLLOW_UNFOLLOW:break;
        case POST:break;
        case PM:break;
        case USERLIST:break;
        case STAT:break;
        default:return nullptr;
    }

    return nullptr;
}

//messageDecoder * messageSelector::getServerMessage(std::string txt) {
//    if (checkValidOutput(txt))
//        return nullptr;
//
//    std::istringstream iss(txt);
//    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
//                                    std::istream_iterator<std::string>{}};
//
//    messageServerTypeMap map;
//    auto type = map[tokens[0]];
//    switch (type){
//        case NOTIFICATIONS:break;
//        case ACK:break;
//        case ERROR:break;
//    }
//
//    return nullptr;
//}

bool messageSelector::checkValidInput(std::string input) {
    std::istringstream iss(input);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    messageClientTypeMap map;
    auto type = map[tokens[0]];
    switch (type){
        case LOGIN: return tokens.size() == 3;
        case REGISTER: return tokens.size() == 3;
        case LOGOUT:break;
        case FOLLOW_UNFOLLOW:break;
        case POST:break;
        case PM:break;
        case USERLIST:break;
        case STAT:break;
        default:return false;
    }

    return false;
}

bool messageSelector::checkValidOutput(std::string output) {
    return true;
}


registerMessage::registerMessage() {

}

std::string registerMessage::encode(std::string &in) {
    char bytesArr[2];
    char zeroByte = '0';

    messageClientToServerType type = REGISTER;
    shortToBytes((short) type,bytesArr);

    std::istringstream iss(in);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    std::string ans;
    ans.append(std::to_string(bytesArr[0]));
    ans.append(std::to_string(bytesArr[1]));
    ans.append(tokens[1]+zeroByte);
    ans.append(tokens[2]+zeroByte);
    return ans;
}
