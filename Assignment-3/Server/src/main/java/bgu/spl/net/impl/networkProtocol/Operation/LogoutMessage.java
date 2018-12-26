package bgu.spl.net.impl.networkProtocol.Operation;

import java.nio.charset.StandardCharsets;

public class LogoutMessage extends ClientMessage {
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
        setOpCode(Integer.valueOf(msg.substring(0,2)));

    }
}
