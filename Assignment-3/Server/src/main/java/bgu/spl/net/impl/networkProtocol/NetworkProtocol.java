package bgu.spl.net.impl.networkProtocol;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.networkProtocol.Task.Login;
import bgu.spl.net.impl.networkProtocol.Task.Register;
import bgu.spl.net.impl.networkProtocol.Task.Task;

import java.util.concurrent.ConcurrentHashMap;

public class NetworkProtocol implements BidiMessagingProtocol<String> {

    private boolean shouldTerminate = false;
    private Connections<String> connections;
    private ConcurrentHashMap<String,Integer> loggedInMap;
    private UsersManager usersManager;
    private int connectionId;

    public NetworkProtocol(ConcurrentHashMap<String, Integer> loggedIn, UsersManager usersManager) {
        this.loggedInMap = loggedIn;
        this.usersManager = usersManager;
    }

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId=connectionId;
        this.connections=connections;
    }

    @Override
    public void process(String msg) {
        String replay = parseMessage(msg);
        System.out.println("sending replay: " + replay);
        connections.send(connectionId,replay);
        //ToDo: change it after creating logout task class
        if(replay == "ACK 3")
            this.connections.disconnect(this.connectionId);

    }

    private String parseMessage(String msg) {
        String ans="";
        int optCode = Integer.valueOf(msg.substring(0,2));
        String []tokens = msg.substring(2).replace("\n","").split("0");
        MessageType messageType = MessageType.fromInteger(optCode);
        Task task=null;
        switch (messageType){

            case REGISTER:
                task = new Register(usersManager,loggedInMap,connectionId,optCode,new User(tokens[0],tokens[1]));
                ans = task.run();
                break;
            case LOGIN:
                task = new Login(usersManager,loggedInMap,connectionId,optCode,new User(tokens[0],tokens[1]));
                ans = task.run();
                break;
            case LOGOUT:
                break;
            case FOLLOW_UNFOLLOW:
                break;
            case POST:
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


}
