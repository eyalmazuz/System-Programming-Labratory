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


std::vector<char> messageEncoder::encode(std::string &line) {

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
        case FOLLOW:{
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

    }

}

std::vector<char> messageEncoder::encodeLogin(std::string &line) {
    char bytesArr[2];
    char zeroByte = '\0';

    shortToBytes(LOGIN, bytesArr);

    std::vector<char> bytes;
    bytes.insert(bytes.begin(), bytesArr[1]);
    bytes.insert(bytes.begin(), bytesArr[0]);

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    tokens.erase(tokens.begin());

    for(auto t : tokens){
        bytes.insert(bytes.end(), t.begin(), t.end());
        bytes.push_back(zeroByte);
    }

    return bytes;

}

std::vector<char> messageEncoder::encodeRegister(std::string &line) {

    char bytesArr[2];
    char zeroByte = '\0';

    shortToBytes(REGISTER, bytesArr);

    std::vector<char> bytes;
    bytes.insert(bytes.begin(), bytesArr[1]);
    bytes.insert(bytes.begin(), bytesArr[0]);

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};
    tokens.erase(tokens.begin());

    for(auto t : tokens){
      bytes.insert(bytes.end(), t.begin(), t.end());
      bytes.push_back(zeroByte);
    }


    return bytes;

}

std::vector<char> messageEncoder::encodeLogout(std::string &line) {
    char bytesArr[2];
    char zeroByte = '\0';

    shortToBytes(LOGOUT, bytesArr);

    std::vector<char> bytes;
    bytes.insert(bytes.begin(), bytesArr[1]);
    bytes.insert(bytes.begin(), bytesArr[0]);

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};
    tokens.erase(tokens.begin());

    for(auto t : tokens){
        bytes.insert(bytes.end(), t.begin(), t.end());
        bytes.push_back('\0');
    }

    return bytes;
}

std::vector<char> messageEncoder::encodeFollow(std::string &line) {
    char bytesArr[2];
    char zeroByte = '\0';

    shortToBytes(FOLLOW, bytesArr);

    std::vector<char> bytes;
    bytes.insert(bytes.begin(), bytesArr[1]);
    bytes.insert(bytes.begin(), bytesArr[0]);

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};
    tokens.erase(tokens.begin());
    std::string sign = tokens[0];
    tokens.erase(tokens.begin());
    bytes.insert(bytes.end(), sign.begin(), sign.end());
    bytes.push_back(' ');
    std::string size = tokens[0];
    tokens.erase(tokens.begin());
    bytes.insert(bytes.end(), size.begin(), size.end());
    bytes.push_back(' ');
    //FOLLOW 1 2 RICK BIRDPERSON
    //TOKENS = [1, 2 , RICK, BIRDPERSON]
    //BYTES = [1 2 RICK BIRDPERSON ]

    for(auto t : tokens){
        bytes.insert(bytes.end(), t.begin(), t.end());
        bytes.push_back(zeroByte);
    }


    return bytes;

}

std::vector<char> messageEncoder::encodePost(std::string &line) {
    char bytesArr[2];
    char zeroByte = '\0';

    shortToBytes(POST, bytesArr);

    std::vector<char> bytes;
    bytes.insert(bytes.begin(), bytesArr[1]);
    bytes.insert(bytes.begin(), bytesArr[0]);

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};
    tokens.erase(tokens.begin());

    //FOLLOW 1 2 RICK BIRDPERSON
    //TOKENS = [1, 2 , RICK, BIRDPERSON]
    //BYTES = [1 2 RICK BIRDPERSON ]

    for(auto t : tokens){
        bytes.insert(bytes.end(), t.begin(), t.end());
        bytes.push_back(' ');
    }

    bytes.pop_back();
    bytes.push_back(zeroByte);

    return bytes;
}

std::vector<char> messageEncoder::encodePM(std::string &line) {

    char bytesArr[2];
    char zeroByte = '\0';

    shortToBytes(PM, bytesArr);

    std::vector<char> bytes;
    bytes.insert(bytes.begin(), bytesArr[1]);
    bytes.insert(bytes.begin(), bytesArr[0]);

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};
    tokens.erase(tokens.begin());
    std::string userName = tokens[0];
    tokens.erase(tokens.begin());
    bytes.insert(bytes.end(), userName.begin(), userName.end());
    bytes.push_back(zeroByte);
    for(auto t : tokens){
        bytes.insert(bytes.end(), t.begin(), t.end());
        bytes.push_back(' ');
    }

    bytes.pop_back();
    bytes.push_back(zeroByte);

    return bytes;


}

std::vector<char> messageEncoder::encodeUserList(std::string &line) {
    char bytesArr[2];
    char zeroByte = '\0';

    shortToBytes(USERLIST, bytesArr);

    std::vector<char> bytes;
    bytes.insert(bytes.begin(), bytesArr[1]);
    bytes.insert(bytes.begin(), bytesArr[0]);

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};
    tokens.erase(tokens.begin());

    for(auto t : tokens){
        bytes.insert(bytes.end(), t.begin(), t.end());
        bytes.push_back('\0');
    }

    return bytes;

}

std::vector<char> messageEncoder::encodeStats(std::string &line) {

    char bytesArr[2];
    char zeroByte = '\0';

    shortToBytes(STAT, bytesArr);

    std::vector<char> bytes;
    bytes.insert(bytes.begin(), bytesArr[1]);
    bytes.insert(bytes.begin(), bytesArr[0]);

    std::istringstream iss(line);
    std::vector<std::string> tokens{std::istream_iterator<std::string>{iss},
                                    std::istream_iterator<std::string>{}};

    tokens.erase(tokens.begin());

    for(auto t : tokens){
        bytes.insert(bytes.end(), t.begin(), t.end());
        bytes.push_back(zeroByte);
    }

    return bytes;

}