package bgu.spl.net.impl.networkProtocol.Operation;

import bgu.spl.net.impl.Utils;
import bgu.spl.net.impl.networkProtocol.MessageType;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

public class Follow_Unfollow_Message extends NetworkMessage {
    private int sign;
    private int numOfUsers;
    private ConcurrentLinkedQueue<String> userNameList;

    public Follow_Unfollow_Message() {
        MessageType messageType = MessageType.FOLLOW;
        setOpCode(messageType.getOpcode());
    }

    @Override
    public boolean checkIfMessageIsValid(String msg) {
        //String msg = new String(bytes, StandardCharsets.UTF_8);
        if (msg.length() > 5 &&
                (Integer.valueOf(msg.charAt(2)) == 0 || Integer.valueOf(msg.charAt(2)) == 1) &&
                //(Character.isDigit(msg.charAt(3)) && Character.isDigit(msg.charAt(4))) &&
                Pattern.compile("([\\w+].*[\0]){"+ Utils.twoBytesToShort(msg,3) +"}$").matcher(msg.substring(5)).find()){
            updateFields(msg);
            return true;
        }
        return false;
    }

    @Override
    public void updateFields(String msg) {
        this.messageStr = msg;
        String []tokens = messageStr.substring(5).split("\0");
        int sign = Integer.valueOf(msg.charAt(2));
        int size = Utils.twoBytesToShort(msg,3);
        this.sign = sign;
        this.numOfUsers = size;
        userNameList = new ConcurrentLinkedQueue<>();
        userNameList.addAll(Arrays.asList(tokens));
    }

    public int getSign() {
        return sign;
    }

    public int getNumOfUsers() {
        return numOfUsers;
    }

    public ConcurrentLinkedQueue<String> getUserNameList() {
        return userNameList;
    }

    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
}
