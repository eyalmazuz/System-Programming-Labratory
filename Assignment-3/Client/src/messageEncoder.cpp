//
// Created by eyal on 12/23/18.
//

#include <vector>
#include <iterator>
#include <sstream>
#include "../include/messageEncoder.h"



void messageEncoder::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}


messageEncoder::messageEncoder() {}


std::string messageEncoder::encode(std::string &line) {

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    opcodesMap map;
    opcodes type = map[tokens[0]];
    char *opcode;
    switch (type){
        case LOGIN: {
            return encodeLogin(line);
            break;
        }
        case REGISTER: {
            return encodeRegister(line);
            break;
        }
        case LOGOUT:{
            return encodeLogout(line);
        }
        case FOLLOW_UNFOLLOW:{
            return encodeFollow(line);
        }
        case POST:{
            return encodePost(line);
        }
        case PM:{
            return encodePM(line);
        }
        case USERLIST: {
            return encodeUserList(line);
        }
        case STAT: {
            return encodeStats(line);
        }
        default:return nullptr;
    }

}

std::string messageEncoder::encodeLogin(std::string &line) {
    char bytesArr[2];
    char zeroByte = '\0';

    opcodes type = LOGIN;
    shortToBytes(type, bytesArr);

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    std::string ans (bytesArr, sizeof(bytesArr));
    ans.append(tokens[1]+zeroByte);
    ans.append(tokens[2]+zeroByte);
    return ans;

}

std::string messageEncoder::encodeRegister(std::string &line) {

    char bytesArr[2];
    char zeroByte = '\0';
    opcodes type = REGISTER;
    shortToBytes(type, bytesArr);

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    std::string ans (bytesArr, sizeof(bytesArr));
    ans.append(tokens[1]+zeroByte);
    ans.append(tokens[2]+zeroByte);
    return ans;

}

std::string messageEncoder::encodeLogout(std::string &line) {}

std::string messageEncoder::encodeFollow(std::string &line) {}

std::string messageEncoder::encodePost(std::string &line) {}

std::string messageEncoder::encodePM(std::string &line) {}

std::string messageEncoder::encodeUserList(std::string &line) {}

std::string messageEncoder::encodeStats(std::string &line) {}