package bgu.spl.net.impl.networkProtocol;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.networkProtocol.Operation.AckMessage;
import bgu.spl.net.impl.networkProtocol.Operation.ClientMessage;
import bgu.spl.net.impl.networkProtocol.Operation.ErrorMessage;
import bgu.spl.net.impl.networkProtocol.Operation.RegisterMessage;
import bgu.spl.net.impl.networkProtocol.Task.*;
import com.sun.security.ntlm.Client;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class NetworkProtocol implements BidiMessagingProtocol<ClientMessage> {

    private boolean shouldTerminate = false;
    private Connections<ClientMessage> connections;
    private Database database;
    private int connectionId;

    public NetworkProtocol(Database database) {
        this.database = database;
    }

    @Override
    public void start(int connectionId, Connections<ClientMessage> connections) {
        this.connectionId=connectionId;
        this.connections=connections;
    }

    @Override
    public void process(ClientMessage msg) {
        System.out.println("message received: " + msg);
        String replay = (String)parseMessage(msg);
        System.out.println("sending replay: " + replay);
        connections.send(connectionId,replay);
        //ToDo: change it after creating logout task class
        if(replay == "ACK 3")
            this.connections.disconnect(this.connectionId);

    }

    private Object parseMessage(ClientMessage msg) {
        int answer = 0;
//        byte[] bytes = msg.substring(0,2).getBytes(StandardCharsets.UTF_8);
//        short opCode = bytesToShort(bytes);
//        String []tokens = msg.substring(2).replace("\n","").split("\0");
        MessageType messageType = MessageType.fromInteger(msg.getOpCode());
        Task task=null;
        switch (messageType){
            RegisterMessage registerMessage = (RegisterMessage)msg;
            case REGISTER:
                task = new Register(database,connectionId,msg.getOpCode(),new User(tokens[0],tokens[1]));
                ans = task.run();
                break;
            case LOGIN:
                task = new Login(database,connectionId,msg.getOpCode(),new User(tokens[0],tokens[1]));
                ans = task.run();
                ArrayList<Message> messages = database.getUserByConnectionID(connectionId).getMessages();
                messages.stream()
                        .filter(m -> m.getTime() > database.getUserByConnectionID(connectionId).getLogoutTime())
                        .forEach( m-> connections.send(connectionId, new Notification(opCode, "Public", m.getUserName(), m.getMessage()).toString()));
                break;
            case LOGOUT:
                if(database.isLoggedInbyConnId(connectionId)) {
                    task = new Logout(database, connectionId, msg.getOpCode());
                    ans = task.run();
                }else{
                    ans =  new ErrorMessage(msg.getOpCode());
                }
                break;
            case FOLLOW:
                if(database.isLoggedInbyConnId(connectionId)) {
                    int sign = tokens[0].charAt(0) - 48;
                    int size = tokens[0].charAt(2) - 48;
                    tokens[0] = tokens[0].substring(4);
                    ArrayList<String> list = new ArrayList<>();
                    list.addAll(Arrays.asList(tokens));
                    task = new Follow_Unfollow(database, connectionId, msg.getOpCode(), sign, size, list);
                    ans = task.run();
                }
                else{
                    ans = new ErrorMessage(msg.getOpCode());
                }
                break;
            case POST:
                if(database.isLoggedInbyConnId(connectionId)) {
                    long time = System.currentTimeMillis();
                    ArrayList<String> users = new Post(database, connectionId, msg.getOpCode(), tokens[0]).run();
                    for (String user : users) {
                        synchronized (user) {
                            if (database.isLoggedInByName(user)) {
                                int connId = database.getConnetionIdByName(user);
                                Notification reply = new Notification(msg.getOpCode(), "Public", database.getUserByConnectionID(connectionId).getName(), tokens[0]);
                                connections.send(connId, reply.toString());
                            }
                            database.getUserbyName(user).addMessage(new Message(tokens[0], time, database.getUserByConnectionID(connectionId).getName()));
                        }
                    }
                    database.getUserByConnectionID(connectionId).addPost(new Message(tokens[0], time, database.getUserByConnectionID(connectionId).getName()));
                    ans = new AckMessage(msg.getOpCode()).toString();
                }
                else{
                    ans = new ErrorMessage(msg.getOpCode());
                }
                break;
            case PM:
                if(database.isLoggedInbyConnId(connectionId)) {
                    long timeStamp = System.currentTimeMillis();

                    int connId = database.getConnetionIdByName(tokens[0]);
                    if (connId != 0) {
                        Notification reply = new Notification(msg.getOpCode(), "Private", database.getUserByConnectionID(connectionId).getName(), tokens[1]);
                        connections.send(connId, reply.toString());
                    }
                    database.getUserbyName(tokens[0]).addMessage(new Message(tokens[0], timeStamp, database.getUserByConnectionID(connectionId).getName()));

                    database.getUserByConnectionID(connectionId).addPost(new Message(tokens[0], timeStamp, database.getUserByConnectionID(connectionId).getName()));
                    ans = new AckMessage(msg.getOpCode()).toString();
                }
                else{
                    ans = new ErrorMessage(msg.getOpCode());
                }
                break;
            case USERLIST:
                if(database.isLoggedInbyConnId(connectionId)) {
                    String userList = new UserList(database.getUsers()).toString();
                    ans = new AckMessage(msg.getOpCode(), database.getNumOfUsers(), userList).toString();
                }else{
                    ans = new ErrorMessage(msg.getOpCode());
                }
                break;
            case STAT:
                if(database.isLoggedInbyConnId(connectionId)) {
                    User user = database.getUserbyName(tokens[0]);
                    ans = new AckMessage(msg.getOpCode(), user.getNumOfPost(), user.getNumOfFollowers(), user.getNumOfFollowing()).toString();
                }
                else{
                    ans = new ErrorMessage(msg.getOpCode());
                }
                break;
        }

        return answer == 0 ?
                new AckMessage(msg.getOpCode(), data) : answer == 1 ?
                new ErrorMessage(msg.getOpCode()) : new Notification(msg.getOpCode(), "public", database.getUserByConnectionID(connectionId).getName(), msg.toString()); //TODO CHANGE THIS
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
