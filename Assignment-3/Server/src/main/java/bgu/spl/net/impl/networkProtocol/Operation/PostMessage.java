package bgu.spl.net.impl.networkProtocol.Operation;

import bgu.spl.net.impl.networkProtocol.MessageType;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class PostMessage extends NetworkMessage {
    private ArrayList<String> users;
    private String content;
    public PostMessage(){
        MessageType messageType = MessageType.POST;
        setOpCode(messageType.getOpcode());
        users = new ArrayList<>();
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void addUser(String user){
        users.add(user);
    }

    public void addUsers(ArrayList<String> users){
        users.addAll(users);
    }

    @Override
    public boolean checkIfMessageIsValid(String msg) {
        if (msg.length() > 2 && Pattern.compile("([\\w+].*[\0]){1}$").matcher(msg.substring(2)).find()){
            updateFields(msg);
            return true;
        }
        return false;
    }

    //ToDo: change updateFields here
    @Override
    protected void updateFields(String msg) {
        this.messageStr = msg;
        content = messageStr.substring(2,messageStr.length() - 1);
    }

    public String getContent() {
        return content;
    }
}
