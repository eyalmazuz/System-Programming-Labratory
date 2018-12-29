package bgu.spl.net.impl.networkProtocol.ReplayMessage;

import bgu.spl.net.impl.Utils;

public class ErrorMessage implements ReplyMessage {

    private int opCode;

    public ErrorMessage(int opCode){
        this.opCode = opCode;
    }
    @Override
    public String toString() {
        return "Error " + opCode;
    }

    @Override
    public byte[] encode() {
        byte[] bytes = new byte[4];
        Utils.setShortToBytes(bytes, (short) ReplyType.ERROR.getOpcode(),0);
        Utils.setShortToBytes(bytes, (short) opCode,2);
        return bytes;
    }
}
