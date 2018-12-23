//
// Created by itay on 20/12/18.
//

#include <string>
#include <map>

#ifndef CLIENT_MESSAGE_H
#define CLIENT_MESSAGE_H

#endif //CLIENT_MESSAGE_H


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

class messageEncoder{
private:
protected:
    void shortToBytes(short num, char* bytesArr);
public:
    messageEncoder();
    std::string encode(std::string &line);
};

class message{
    private: 
        char* opcode;

    public:
        message(char *opcode);
        virtual std::string encode(std::string &line)=0;
        virtual ~message();
};

class loginMessage : public message{
    private:
    public:
        loginMessage(char* opcode);
        std::string encode(std::string &line);
        ~loginMessage();
};

class registerMessage : public message{
    private:
    public:
        registerMessage(char *opcode);
        std::string encode(std::string &line);
        ~registerMessage();
};