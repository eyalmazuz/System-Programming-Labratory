package bgu.spl.net.impl;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.networkProtocol.MessageType;
import bgu.spl.net.impl.networkProtocol.Operation.*;
import bgu.spl.net.impl.networkProtocol.Task.BaseTask;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NetworkEncoderDecoder implements MessageEncoderDecoder<NetworkMessage> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private int start = -1;
    private NetworkMessage networkMessage = null;
    short opCode=-1;

    @Override
    public NetworkMessage decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (start == -1 && len >= 2){
            start = len - 2;
            //opCode = bytesToShort(new byte[]{bytes[start],bytes[start+1]});
            opCode = Utils.twoBytesToShort(bytes,start);
        }

        if (start != -1 && opCode != -1)
            networkMessage = getClientMessage(opCode);

        if (networkMessage != null) {
            return popMessage();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(NetworkMessage message) {
        return (message + "\n").getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private NetworkMessage popMessage() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        //String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        start = -1;
        NetworkMessage copy = networkMessage;
        networkMessage = null;
        return copy;
    }

    private BaseTask getClientMessage(short opCode){
        MessageType messageType = MessageType.fromInteger(opCode);
        String ans = new String(bytes, start , len, StandardCharsets.UTF_8);
        NetworkMessage networkMessage = null;
        switch (messageType){

            case REGISTER:
                networkMessage = new RegisterMessage();
                if (networkMessage.checkIfMessageIsValid(ans))
                    return networkMessage;
                break;
            case LOGIN:
                networkMessage = new LoginMessage();
                if (networkMessage.checkIfMessageIsValid(ans))
                    return networkMessage;
                break;
            case LOGOUT:
                networkMessage = new LogoutMessage();
                if (networkMessage.checkIfMessageIsValid(ans))
                    return networkMessage;
                break;
            case FOLLOW:
                networkMessage = new Follow_Unfollow_Message();
                if (networkMessage.checkIfMessageIsValid(ans))
                    return networkMessage;
                break;
            case POST:
                networkMessage = new PostMessage();
                if (networkMessage.checkIfMessageIsValid(ans))
                    return networkMessage;
                break;
            case PM:
                break;
            case USERLIST:
                break;
            case STAT:
                break;
        }

        return null;

    }

    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
}