package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post extends BaseTask <ArrayList<String>> {

    private String post;
    public Post(Database database, int connectionId, int opCode, String post) {
        super(database, connectionId, opCode);
        this.post = post;
    }

    @Override
    public ArrayList<String> run() {
        ArrayList<String> users = new ArrayList<>();
        Matcher m = Pattern.compile("(?=@([^\\s]+))").matcher(post);
        while(m.find()){
            users.add(m.group(1));
        }
        users.addAll(database.getUserByConnectionID(connectionId).getFollowers());
        return users;
    }
}
