package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.MessageType;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;

public class UserListMessage implements Task<Database> {

    private int opCode;

    public UserListMessage() {
        opCode = MessageType.USERLIST.getOpcode();

    }

    @Override
    public ReplyMessage run(Database arg, int connectionId) {

        return arg.userListCommand(connectionId);
    }


    @Override
    public boolean checkIfMessageIsValid(String msg) {
        if (msg.length() == 2){
            updateFields(msg);
            return true;
        }
        return false;
    }

    @Override
    public void updateFields(String msg) {

    }

    public String getUsers() {

        return null;
    }
}
