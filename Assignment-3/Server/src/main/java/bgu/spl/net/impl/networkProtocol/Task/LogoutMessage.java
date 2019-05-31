package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.MessageType;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;

public class LogoutMessage implements Task<Database> {

    private String messageStr;
    private int opCode;

    public LogoutMessage() {
        opCode = MessageType.LOGOUT.getOpcode();

    }

    @Override
    public ReplyMessage run(Database arg, int connectionId) {
        return arg.LogoutCommand(connectionId);
    }

    //assuming opcode is correct
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
        this.messageStr = msg;
    }
}
