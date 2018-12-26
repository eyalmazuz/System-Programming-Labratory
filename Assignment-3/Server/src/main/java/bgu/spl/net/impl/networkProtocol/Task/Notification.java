package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Operation.NetworkMessage;

public class Notification extends NetworkMessage {

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

    @Override
    public boolean checkIfMessageIsValid(String msg) {
        return true;
    }

    @Override
    protected void updateFields(String msg) {

    }

    @Override
    public String toString() {
        return "Notification " + type + " " + userName + '\0' + content + '\0';
    }
}
