//
// Created by eyal on 12/23/18.
//

#ifndef CLIENT_MESSAGEENCODER_H
#define CLIENT_MESSAGEENCODER_H


#include <string>
#include <map>
#include <vector>

enum opcodes{
    REGISTER=1,
    LOGIN=2,
    LOGOUT=3,
    FOLLOW=4,
    POST=5,
    PM=6,
    USERLIST=7,
    STAT=8
};

struct opcodesMap : public std::map<std::string,opcodes>{
    opcodesMap(){
        this->operator[]("REGISTER") = REGISTER;
        this->operator[]("LOGIN") = LOGIN;
        this->operator[]("LOGOUT") = LOGOUT;
        this->operator[]("FOLLOW") = FOLLOW;
        this->operator[]("POST") = POST;
        this->operator[]("PM") = PM;
        this->operator[]("USERLIST") = USERLIST;
        this->operator[]("STAT") = STAT;

    }
    ~opcodesMap(){};
};


class messageEncoder {

    public:
        messageEncoder();
        void shortToBytes(short num, char *bytesArr);
    std::vector<char> encode(std::string &line);
    std::vector<char> encodeLogin(std::string &line);
    std::vector<char> encodeRegister(std::string &line);
    std::vector<char> encodeLogout(std::string &line);
    std::vector<char> encodeFollow(std::string &line);
    std::vector<char> encodePost(std::string &line);
    std::vector<char> encodeUserList(std::string &line);
    std::vector<char> encodeStats(std::string &line);
    std::vector<char> encodePM(std::string &line);
};


#endif //CLIENT_MESSAGEENCODER_H
