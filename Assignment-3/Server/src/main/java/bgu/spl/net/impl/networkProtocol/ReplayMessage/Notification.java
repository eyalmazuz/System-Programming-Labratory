package bgu.spl.net.impl.networkProtocol.ReplayMessage;

import bgu.spl.net.impl.Utils;
import bgu.spl.net.impl.networkProtocol.NotificationType;

public class Notification implements ReplyMessage {


    private NotificationType type;
    private String userName;
    private String content;

    public Notification(NotificationType type, String userName, String content){
        this.type = type;
        this.userName = userName;
        this.content = content;
    }
    @Override
    public String toString() {
        return "Notification " + type.toString() + " " + userName + '\0' + content + '\0';
    }

    @Override
    public byte[] encode() {
        byte zeroByte = '\0';
        //2 opcode, 1 notification type, string-length user,string-length content, 1 zeroByte
        byte[] bytes = new byte[5+userName.getBytes().length + content.getBytes().length];
        Utils.setShortToBytes(bytes, (short) ReplyType.NOTIFICATION.getOpcode(),0);
        bytes[2] = (byte) type.getOpcode();
        Utils.setStringToBytes(bytes,userName,3);
        bytes[3+userName.length()] = zeroByte;
        Utils.setStringToBytes(bytes,content,4+userName.length());
        bytes[bytes.length-1] = zeroByte;
        return bytes;
    }
}
