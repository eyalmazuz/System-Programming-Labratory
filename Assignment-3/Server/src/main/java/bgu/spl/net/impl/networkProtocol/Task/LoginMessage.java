package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.MessageType;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;

import java.util.regex.Pattern;

public class LoginMessage implements Task<Database> {

    private String username;
    private String password;
    private String messageStr;
    private int opCode;

    public LoginMessage() {
        opCode = MessageType.LOGIN.getOpcode();
    }

    @Override
    public ReplyMessage run(Database arg, int connectionId) {
        return arg.loginCommand(connectionId, username, password);
    }

    //assuming opcode is correct
    @Override
    public boolean checkIfMessageIsValid(String msg) {
        if (msg.length() > 2 &&
                Pattern.compile("([\\w+].*[\0]){2}$").matcher(msg.substring(2)).find()){
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
        password = tokens[1];
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
