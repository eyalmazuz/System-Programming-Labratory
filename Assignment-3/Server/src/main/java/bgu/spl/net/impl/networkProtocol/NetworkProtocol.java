package bgu.spl.net.impl.networkProtocol;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.networkProtocol.Operation.*;
import bgu.spl.net.impl.networkProtocol.Task.*;

import java.util.ArrayList;
import java.util.Arrays;

public class NetworkProtocol implements BidiMessagingProtocol<NetworkMessage> {

    private boolean shouldTerminate = false;
    private Connections<NetworkMessage> connections;
    private Database database;
    private int connectionId;

    public NetworkProtocol(Database database) {
        this.database = database;
    }

    @Override
    public void start(int connectionId, Connections<NetworkMessage> connections) {
        this.connectionId=connectionId;
        this.connections=connections;
    }

    @Override
    public void process(NetworkMessage msg) {
        System.out.println("message received: " + msg);
        NetworkMessage replay = parseMessage(msg);
        System.out.println("sending replay: " + replay);
        connections.send(connectionId,replay);
        //ToDo: change it after creating logout task class
        if(replay.toString().equals("ACK 3"))
            this.connections.disconnect(this.connectionId);

    }

    private NetworkMessage parseMessage(NetworkMessage msg) {
        MessageType messageType = MessageType.fromInteger(msg.getOpCode());
        NetworkMessage ans = null;
        Task task=null;
        switch (messageType){
            case REGISTER:
                task = new Register(database,connectionId,msg.getOpCode(),(RegisterMessage)msg);
                ans = task.run();
                break;
            case LOGIN:
                task = new Login(database,connectionId,msg.getOpCode(),(LoginMessage)msg);
                ans = (NetworkMessage) task.run();
                if (database.getUserByConnectionID(connectionId) != null) {
                    ArrayList<Message> messages = database.getUserByConnectionID(connectionId).getMessages();
                    messages.stream()
                            .filter(m -> m.getTime() > database.getUserByConnectionID(connectionId).getLogoutTime())
                            .forEach(m -> connections.send(connectionId, new Notification(msg.getOpCode(), "Public", m.getUserName(), m.getMessage())));
                }
                break;
            case LOGOUT:
                if(database.isLoggedInbyConnId(connectionId)) {
                    task = new Logout(database, connectionId, msg.getOpCode());
                    ans = (NetworkMessage) task.run();
                }else{
                    ans =  new ErrorMessage(msg.getOpCode());
                }
                break;
            case FOLLOW:
                if(database.isLoggedInbyConnId(connectionId)) {
                    task = new Follow_Unfollow(database, connectionId, msg.getOpCode(), (Follow_Unfollow_Message)msg);
                    ans = (NetworkMessage) task.run();
                }
                else{
                    ans = new ErrorMessage(msg.getOpCode());
                }
                break;
            case POST:
                if(database.isLoggedInbyConnId(connectionId)) {
                    long time = System.currentTimeMillis();
                    task = new Post(database, connectionId, msg.getOpCode(),(PostMessage) msg);
                    PostMessage post = (PostMessage)task.run();
                    ArrayList<String> users = post.getUsers();
                    String content = post.getContent();
                    for (String user : users) {
                        synchronized (user) {
                            if (database.isLoggedInByName(user)) {
                                int connId = database.getConnetionIdByName(user);
                                Notification reply = new Notification(msg.getOpCode(), "Public", database.getUserByConnectionID(connectionId).getName(), content);
                                connections.send(connId, reply);
                            }
                            database.getUserbyName(user).addMessage(new Message(content, time, database.getUserByConnectionID(connectionId).getName()));
                        }
                    }
                    database.getUserByConnectionID(connectionId).addPost(new Message(content, time, database.getUserByConnectionID(connectionId).getName()));
                    ans = new AckMessage(msg.getOpCode());
                }
                else{
                    ans = new ErrorMessage(msg.getOpCode());
                }
                break;
            case PM:
//                if(database.isLoggedInbyConnId(connectionId)) {
//                    long timeStamp = System.currentTimeMillis();
//
//                    int connId = database.getConnetionIdByName(tokens[0]);
//                    if (connId != 0) {
//                        Notification reply = new Notification(msg.getOpCode(), "Private", database.getUserByConnectionID(connectionId).getName(), tokens[1]);
//                        connections.send(connId, reply.toString());
//                    }
//                    database.getUserbyName(tokens[0]).addMessage(new Message(tokens[0], timeStamp, database.getUserByConnectionID(connectionId).getName()));
//
//                    database.getUserByConnectionID(connectionId).addPost(new Message(tokens[0], timeStamp, database.getUserByConnectionID(connectionId).getName()));
//                    ans = new AckMessage(msg.getOpCode());
//                }
//                else{
//                    ans = new ErrorMessage(msg.getOpCode());
//                }
                break;
            case USERLIST:
                if(database.isLoggedInbyConnId(connectionId)) {
                    String userList = new UserList(database.getUsers()).toString();
                    ans = new AckMessage(msg.getOpCode(), database.getNumOfUsers(), userList);
                }else{
                    ans = new ErrorMessage(msg.getOpCode());
                }
                break;
            case STAT:
//                if(database.isLoggedInbyConnId(connectionId)) {
//                    User user = database.getUserbyName(tokens[0]);
//                    ans = new AckMessage(msg.getOpCode(), user.getNumOfPost(), user.getNumOfFollowers(), user.getNumOfFollowing()).toString();
//                }
//                else{
//                    ans = new ErrorMessage(msg.getOpCode());
//                }
                break;
        }

        return ans;
//        return answer == 0 ?
//                new AckMessage(msg.getOpCode(), data) : answer == 1 ?
//                new ErrorMessage(msg.getOpCode()) : new Notification(msg.getOpCode(), "public", database.getUserByConnectionID(connectionId).getName(), msg.toString()); //TODO CHANGE THIS
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
