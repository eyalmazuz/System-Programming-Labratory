package bgu.spl.net.impl.networkProtocol.Operation;

import java.util.regex.Pattern;

public class PostMessage extends ClientMessage {
    private String content;
    @Override
    public boolean checkIfMessageIsValid(String msg) {
        if (msg.length() > 2 && Pattern.compile("([\\w+].*[0]){1}$").matcher(msg.substring(2)).find()){
            updateFields(msg);
            return true;
        }
        return false;
    }

    @Override
    protected void updateFields(String msg) {
        this.messageStr = msg;
        content = messageStr.substring(3,messageStr.length() - 1);
        setOpCode(Integer.valueOf(msg.substring(0,2)));
    }
}
