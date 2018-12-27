package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;

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
        return false;
    }

    @Override
    public void updateFields(String msg) {

    }
}
