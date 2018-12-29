package bgu.spl.net.impl.networkProtocol;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.Notification;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;
import bgu.spl.net.impl.networkProtocol.Task.*;

public class NetworkProtocol<T> implements BidiMessagingProtocol<NetworkMessage> {

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
        ReplyMessage replay = null;
        System.out.println("message received: " + msg);
        if(msg instanceof PMMessage){
            replay = ((PMMessage)msg).run(database, connections, connectionId);
            connections.send(connectionId,replay);

        }else if(msg instanceof PostMessage){
            replay = ((PostMessage)msg).run(database, connections, connectionId);
            connections.send(connectionId,replay);
        }else if(msg instanceof LoginMessage){
            replay = ((LoginMessage) msg).run(database,connectionId);
            User user = database.getUserByConnectionID(connectionId);
            connections.send(connectionId,replay);
            user.getMessages().stream()
                    .filter(m -> m.getTime() > user.getLogoutTime())
                    .forEach( m-> connections.send(connectionId, new Notification( NotificationType.PUBLIC, m.getUserName(), m.getMessage())));
        }else{
            replay = ((Task)msg).run(database, connectionId);
            connections.send(connectionId,replay);
        }
        System.out.println("sending replay: " + replay);

        //ToDo: change it after creating logout task class
        if(replay.toString().equals("ACK 3"))
            this.connections.disconnect(this.connectionId);

    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
