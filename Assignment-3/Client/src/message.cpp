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


std::string loginMessage::encode(std::string &line) {

    char bytesArr[2];
    char zeroByte = '\0';

    opcodes type = LOGIN;

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    std::string ans;
    ans.append(std::to_string(bytesArr[0]));
    ans.append(std::to_string(bytesArr[1]));
    ans.append(tokens[1]+zeroByte);
    ans.append(tokens[2]+zeroByte);
    return ans;
}

loginMessage::loginMessage(char* _opcode) : message(_opcode){ }

loginMessage::~loginMessage() {
}

std::string messageEncoder::encode(std::string &line){

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    opcodesMap map;
    opcodes type = map[tokens[0]];
    char *opcode;
    switch (type){
        case LOGIN: {
            shortToBytes(type, opcode);
            loginMessage loginM(opcode);
            return loginM.encode(line);
            break;
        }
        case REGISTER: {
            shortToBytes(type, opcode);
            registerMessage registerM(opcode);
            return registerM.encode(line);
            break;
        }
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


registerMessage::registerMessage(char *_opcode) : message(_opcode){

}

std::string registerMessage::encode(std::string &in) {
    char bytesArr[2];
    char zeroByte = '\0';


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

registerMessage::~registerMessage() {

}

message::~message() {

}

message::message(char *opcode) {

}
