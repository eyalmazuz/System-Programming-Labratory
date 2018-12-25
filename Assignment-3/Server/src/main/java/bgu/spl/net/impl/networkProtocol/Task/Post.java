package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;

public class Post extends BaseTask {

    private String post;
    public Post(Database database, int connectionId, int opCode, String post) {
        super(database, connectionId, opCode);
        this.post = post;
    }

    @Override
    public String run() {

    }
}
