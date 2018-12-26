package bgu.spl.net.impl.networkProtocol.Operation;

import bgu.spl.net.impl.networkProtocol.MessageType;

public class LogoutMessage extends NetworkMessage {
    public LogoutMessage() {
        MessageType messageType = MessageType.LOGOUT;
        setOpCode(messageType.getOpcode());
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
