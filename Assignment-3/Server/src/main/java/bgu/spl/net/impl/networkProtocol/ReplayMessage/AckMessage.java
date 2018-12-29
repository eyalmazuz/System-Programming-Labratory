package bgu.spl.net.impl.networkProtocol.ReplayMessage;

import bgu.spl.net.impl.Utils;
import bgu.spl.net.impl.networkProtocol.MessageType;

public class AckMessage implements ReplyMessage {

    private int opCode;
    private String[] data;
    public AckMessage(int opCode, String...data){
        this.opCode = opCode;
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder("ACK " + opCode);
        for (String item :data) {
            message.append(" ").append(item);

        }
        return message.toString().substring(0, message.length());
    }

    @Override
    public byte[] encode() {
        //ArrayList<Byte> bytes = new ArrayList<>();
        //bytes.add()
        byte zeroByte = '\0';
        byte[] bytes = null;
        switch (MessageType.fromInteger(opCode)) {
            case REGISTER: case LOGIN: case LOGOUT: case POST: case PM:
                //2 ack opcode, 2 message opCode
                bytes = new byte[4];
                Utils.setShortToBytes(bytes, (short) ReplyType.ACK.getOpcode(),0);
                Utils.setShortToBytes(bytes, (short) opCode,2);
                break;
            case FOLLOW: case USERLIST:
                //2 ack opcode, 2 message opCode, 2 NumOfUsers,string-length UserNameList, 1 zeroByte
                bytes = new byte[7+data[1].length()];
                Utils.setShortToBytes(bytes, (short) ReplyType.ACK.getOpcode(),0);
                Utils.setShortToBytes(bytes, (short) opCode,2);
                Utils.setShortToBytes(bytes, Short.parseShort(data[0]),4);
                Utils.setStringToBytes(bytes,data[1].replace(" ","\0"),6);
                bytes[bytes.length-1] = zeroByte;
                break;
            case STAT:
                bytes = new byte[10];
                Utils.setShortToBytes(bytes, (short) ReplyType.ACK.getOpcode(),0);
                Utils.setShortToBytes(bytes, (short) opCode,2);
                Utils.setShortToBytes(bytes, Short.parseShort(data[0]),4);
                Utils.setShortToBytes(bytes, Short.parseShort(data[1]),6);
                Utils.setShortToBytes(bytes, Short.parseShort(data[2]),8);
                break;
        }
        return bytes;
    }
}
