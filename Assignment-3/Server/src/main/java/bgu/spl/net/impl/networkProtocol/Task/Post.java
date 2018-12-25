package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.User;

import java.util.ArrayList;

public class Post extends BaseTask <ArrayList<String>> {

    private String post;
    public Post(Database database, int connectionId, int opCode, String post) {
        super(database, connectionId, opCode);
        this.post = post;
    }

    @Override
    public ArrayList<String> run() {
        return database.getUserByConnectionID(connectionId).getFollowers();
    }
}
