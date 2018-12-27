package bgu.spl.net.impl.networkProtocol.Operation;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.MessageType;
import bgu.spl.net.impl.networkProtocol.Task.BaseTask;

import java.util.regex.Pattern;

public class RegisterMessage extends BaseTask<Database> {

    private String username;
    private String password;
    private String messageStr;

    public RegisterMessage(){
        super();
        MessageType messageType = MessageType.REGISTER;
        setOpCode(messageType.getOpcode());
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

    @Override
    public NetworkMessage run(Database database) {
        return database.regsiterCommand(connectionId, username, password);
    }
}
