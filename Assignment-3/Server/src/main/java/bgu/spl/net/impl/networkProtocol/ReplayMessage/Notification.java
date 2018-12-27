package bgu.spl.net.impl.networkProtocol.ReplayMessage;

import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;

public class Notification implements ReplyMessage {

    private int opCode;
    private String type;
    private String userName;
    private String content;

    public Notification(int opCode, String type, String userName, String content){
        this.opCode = opCode;
        this.type = type;
        this.userName = userName;
        this.content = content;
    }

    public String toString() {
        return "Notification " + type + " " + userName + '\0' + content + '\0';
    }
}
