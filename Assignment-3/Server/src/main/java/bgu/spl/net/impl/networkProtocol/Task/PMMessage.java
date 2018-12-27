package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.Message;
import bgu.spl.net.impl.networkProtocol.MessageType;
import bgu.spl.net.impl.networkProtocol.NetworkMessage;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.AckMessage;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ErrorMessage;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.Notification;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;

import java.util.ArrayList;

public class PMMessage implements Task<Database> {

    private String username;
    private String content;
    private int opCode;

    public PMMessage(){
        opCode = MessageType.PM.getOpcode();
    }

    @Override
    public ReplyMessage run(Database arg, int connectionId) {
        return null;
    }

    public ReplyMessage run(Database database, Connections<NetworkMessage> connections, int connectionId){
        ReplyMessage ans;
        ArrayList<String> users = database.postCommand(content, connectionId);
        if(database.isLoggedInbyConnId(connectionId)) {
            long time = System.currentTimeMillis();
                synchronized (username) {
                    if (database.isLoggedInByName(username)) {
                        int connId = database.getConnetionIdByName(username);
                        Notification reply = new Notification(opCode, "Private", database.getUserByConnectionID(connectionId).getName(), content);
                        connections.send(connId, reply);
                    }
                    database.getUserbyName(username).addMessage(new Message(content, time, database.getUserByConnectionID(connectionId).getName()));
                }
            database.getUserByConnectionID(connectionId).addPost(new Message(content, time, database.getUserByConnectionID(connectionId).getName()));
            ans = new AckMessage(opCode);
        }
        else{
            ans = new ErrorMessage(opCode);
        }
        return ans;
    }

    @Override
    public boolean checkIfMessageIsValid(String msg) {
        return false;
    }

    @Override
    public void updateFields(String msg) {

    }
}
