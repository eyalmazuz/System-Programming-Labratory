package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.Operation.NetworkMessage;
import bgu.spl.net.impl.networkProtocol.Operation.PostMessage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post extends BaseTask <NetworkMessage> {
    private PostMessage postMessage;
    public Post(Database database, int connectionId, int opCode, PostMessage postMessage) {
        super(database, connectionId, opCode);
        this.postMessage = postMessage;

    }

    @Override
    public NetworkMessage run() {
        ArrayList<String> user = new ArrayList<>();
        Matcher m = Pattern.compile("(?=@([^\\s]+))").matcher(postMessage.getContent());
        while(m.find()){
            user.add(m.group(1));
        }
        postMessage.addUsers(database.getUserByConnectionID(connectionId).getFollowers());
        return postMessage;
    }
}
