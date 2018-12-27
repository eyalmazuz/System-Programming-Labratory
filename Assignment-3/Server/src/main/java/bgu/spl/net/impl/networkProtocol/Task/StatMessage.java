package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;

import java.util.regex.Pattern;

public class StatMessage implements Task<Database> {

    private String messageStr;
    private String username;

    public StatMessage() {
    }


    @Override
    public ReplyMessage run(Database arg, int connectionId) {
        return arg.statCommand(connectionId, username);
    }

    @Override
    public boolean checkIfMessageIsValid(String msg) {
        if (msg.length() > 2 &&
                Pattern.compile("([\\w].*[\0]){1}$").matcher(msg.substring(2)).find()){
            updateFields(msg);
            return true;
        }

        return false;
    }

    @Override
    public void updateFields(String msg) {
        this.messageStr = msg;
        String []tokens = messageStr.substring(2).split("\0");
        username = tokens[0];
    }
}
