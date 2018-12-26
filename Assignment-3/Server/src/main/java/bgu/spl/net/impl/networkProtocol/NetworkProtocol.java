package bgu.spl.net.impl.networkProtocol;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.networkProtocol.Task.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class NetworkProtocol implements BidiMessagingProtocol<String> {

    private boolean shouldTerminate = false;
    private Connections<String> connections;
    private Database database;
    private int connectionId;

    public NetworkProtocol(Database database) {
        this.database = database;
    }

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId=connectionId;
        this.connections=connections;
    }

    @Override
    public void process(String msg) {
        System.out.println("message received: " + msg);
        String replay = (String)parseMessage(msg);
        System.out.println("sending replay: " + replay);
        connections.send(connectionId,replay);
        //ToDo: change it after creating logout task class
        if(replay == "ACK 3")
            this.connections.disconnect(this.connectionId);

    }

    private Object parseMessage(String msg) {
        Object ans = null;
        byte[] bytes = msg.substring(0,2).getBytes(StandardCharsets.UTF_8);
        short opCode = bytesToShort(bytes);
        String []tokens = msg.substring(2).replace("\n","").split("\0");
        MessageType messageType = MessageType.fromInteger(opCode);
        Task task=null;
        switch (messageType){

            case REGISTER:
                task = new Register(database,connectionId,opCode,new User(tokens[0],tokens[1]));
                ans = task.run();
                break;
            case LOGIN:
                task = new Login(database,connectionId,opCode,new User(tokens[0],tokens[1]));
                ans = task.run();
                break;
            case LOGOUT:
                task = new Logout(database,connectionId,opCode);
                ans = task.run();
                break;
            case FOLLOW:
                //04 1 3 ITAY EYAL DDD
                //[04, 1 3 ITAY, EYAL, DDD]
                int sign = tokens[0].charAt(0)-48;
                int size = tokens[0].charAt(2) - 48;
                tokens[0] = tokens[0].substring(4);
                ArrayList<String> list = new ArrayList<>();
                list.addAll(Arrays.asList(tokens));
                task = new Follow_Unfollow(database, connectionId, opCode, sign, size, list);
                ans = task.run();
                break;
            case POST:
                ArrayList<String> users = new Post(database, connectionId, opCode, tokens[0]).run();
                for(String user: users){
                    int connId = database.getConnetionIdByName(user);
                    Notification reply = new Notification(opCode, "Public", database.getUserByConnectionID(connectionId).getName(), tokens[0]);
                    connections.send(connId, reply.toString());
                }
                ans = new AckMessage(opCode).toString();
                break;
            case PM:
                break;
            case USERLIST:
                break;
            case STAT:
                break;
        }

        return ans;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }


    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

}
