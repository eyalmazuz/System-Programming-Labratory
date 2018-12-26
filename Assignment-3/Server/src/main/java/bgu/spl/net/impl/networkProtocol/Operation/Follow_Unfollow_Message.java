package bgu.spl.net.impl.networkProtocol.Operation;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Follow_Unfollow_Message extends ClientMessage {
    private int sign;
    private int numOfUsers;
    private ArrayList<String> userNameList;

    @Override
    public boolean checkIfMessageIsValid(String msg) {
        //String msg = new String(bytes, StandardCharsets.UTF_8);
        if (msg.length() > 5 &&
                (msg.charAt(2) == 0 || msg.charAt(2) == 1) &&
                Character.isDigit(msg.charAt(3)) &&
                Pattern.compile("([\\w+].*[0]){"+Integer.valueOf(msg.charAt(3))+"}$").matcher(msg.substring(2)).find()){
            updateFields(msg);
            return true;
        }
        return false;
    }

    @Override
    public void updateFields(String msg) {
        this.messageStr = msg;
        setOpCode(Integer.valueOf(msg.substring(0,2)));
        String []tokens = messageStr.substring(2).split("\0");
        int sign = tokens[0].charAt(0) - 48;
        int size = tokens[0].charAt(2) - 48;
        tokens[0] = tokens[0].substring(4);
        this.sign = sign;
        this.numOfUsers = size;
        userNameList = new ArrayList<>();
        userNameList.addAll(Arrays.asList(tokens));
    }

    public int getSign() {
        return sign;
    }

    public int getNumOfUsers() {
        return numOfUsers;
    }

    public ArrayList<String> getUserNameList() {
        return userNameList;
    }
}
