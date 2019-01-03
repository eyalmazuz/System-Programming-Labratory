package bgu.spl.net.impl.networkProtocol;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.ConnectionsImpl;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.Notification;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;
import bgu.spl.net.impl.networkProtocol.Task.*;

public class NetworkProtocol<T> implements BidiMessagingProtocol<NetworkMessage> {

    private boolean shouldTerminate = false;
    private Connections<NetworkMessage> connections;
    private Database database;
    private int connectionId;
    private boolean flag;
    public NetworkProtocol(Database database) {
        this.database = database;
    }

    @Override
    public void start(int connectionId, Connections<NetworkMessage> connections) {
        this.connectionId=connectionId;
        this.connections=connections;
        this.flag = false;
    }

    @Override
    public void process(NetworkMessage msg) {
        ReplyMessage replay = null;
        //System.out.println("message received: " + msg);
        flag = false;
        if(msg instanceof PMMessage){
            replay = ((PMMessage)msg).run(database, connections, connectionId);
            connections.send(connectionId,replay);
        }else if(msg instanceof PostMessage){
            replay = ((PostMessage)msg).run(database, connections, connectionId);
            connections.send(connectionId,replay);
        }else if(msg instanceof LoginMessage){
            replay = ((LoginMessage) msg).run(database,connectionId);
            connections.send(connectionId,replay);
            User user = database.getUserByConnectionID(connectionId);
            if (user != null) {
                user.getMessages().stream()
                        .filter(m -> m.getTime() > user.getLogoutTime())
                        .forEach(m -> connections.send(connectionId, new Notification(m.getNotificationType(), m.getUserName(), m.getMessage())));
            }
        }else{
            replay = ((Task)msg).run(database, connectionId);
            flag = connections.send(connectionId,replay);
        }
        //System.out.println("sending replay: " + replay);

        //ToDo: change it after creating logout task class
        if(replay.toString().equals("ACK 3") && flag) {
            this.connections.disconnect(this.connectionId);
        }

    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
