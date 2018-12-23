//
// Created by itay on 20/12/18.
//

#include <string>
#include <map>

#ifndef CLIENT_MESSAGE_H
#define CLIENT_MESSAGE_H

#endif //CLIENT_MESSAGE_H


enum messageClientToServerType{
    REGISTER=1,
    LOGIN=2,
    LOGOUT=3,
    FOLLOW_UNFOLLOW=4,
    POST=5,
    PM=6,
    USERLIST=7,
    STAT=8
};

//enum messageServerToClientType{
//    NOTIFICATIONS=9,
//    ACK=10,
//    ERROR=11
//};

struct messageClientTypeMap : public std::map<std::string,messageClientToServerType>{
    messageClientTypeMap(){
        this->operator[]("REGISTER") = REGISTER;
        this->operator[]("LOGIN") = LOGIN;
        this->operator[]("LOGOUT") = LOGOUT;
        this->operator[]("FOLLOW_UNFOLLOW") = FOLLOW_UNFOLLOW;
        this->operator[]("POST") = POST;
        this->operator[]("PM") = PM;
        this->operator[]("USERLIST") = USERLIST;
        this->operator[]("STAT") = STAT;

    }
    ~messageClientTypeMap(){};
};

//struct messageServerTypeMap : public std::map<std::string,messageServerToClientType>{
//    messageServerTypeMap(){
//        this->operator[]("NOTIFICATIONS") = NOTIFICATIONS;
//        this->operator[]("ACK") = ACK;
//        this->operator[]("ERROR") = ERROR;
//    }
//    ~messageServerTypeMap(){};
//};

class messageEncoder{
private:
protected:
    void shortToBytes(short num, char* bytesArr);
public:
    messageEncoder();
    virtual std::string encode(std::string &in)=0;
};

//class messageDecoder{
//private:
//protected:
//    short bytesToShort(char* bytesArr);
//public:
//    messageDecoder();
//    virtual std::string decode(std::string &out)=0;
//};


class messageSelector{
private:
    static bool checkValidInput(std::string input);
    static bool checkValidOutput(std::string output);
public:
    static messageEncoder * getClientMessage(std::string &txt);
    //static messageDecoder * getServerMessage(std::string txt);
};

class loginMessage : public messageEncoder{
public:
    loginMessage();
    std::string encode(std::string &in) override;
};

class registerMessage : public messageEncoder{
public:
    registerMessage();
    std::string encode(std::string &in) override;
};

//class ackMessage : public messageDecoder{
//public:
//    ackMessage();
//    std::string decode(std::string &out) override;
//};



