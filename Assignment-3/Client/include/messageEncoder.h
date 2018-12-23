//
// Created by eyal on 12/23/18.
//

#ifndef CLIENT_MESSAGEENCODER_H
#define CLIENT_MESSAGEENCODER_H


#include <string>
#include <map>


enum opcodes{
    REGISTER=1,
    LOGIN=2,
    LOGOUT=3,
    FOLLOW_UNFOLLOW=4,
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
        this->operator[]("FOLLOW_UNFOLLOW") = FOLLOW_UNFOLLOW;
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
        std::string encode(std::string &line);
        std::string encodeLogin(std::string &line);
        std::string encodeRegister(std::string &line);
        std::string encodeLogout(std::string &line);
        std::string encodeFollow(std::string &line);
        std::string encodePost(std::string &line);
        std::string encodeUserList(std::string &line);
        std::string encodeStats(std::string &line);
        std::string encodePM(std::string &line);
};


#endif //CLIENT_MESSAGEENCODER_H
