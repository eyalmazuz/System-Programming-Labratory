package bgu.spl.net.impl;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.networkProtocol.MessageType;
import bgu.spl.net.impl.networkProtocol.Operation.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NetworkEncoderDecoder implements MessageEncoderDecoder<ClientMessage> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private int start = -1;
    private ClientMessage clientMessage = null;

    @Override
    public ClientMessage decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (start == -1 && len >= 2){
            start = len -1;
            short opCode = bytesToShort(new byte[]{bytes[len],bytes[len-1]});
            clientMessage = getClientMessage(opCode);
        }

        if (clientMessage != null) {
            return popString();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(ClientMessage message) {
        return message.toString().getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private ClientMessage popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        //String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        start = -1;
        return clientMessage;
    }

    private ClientMessage getClientMessage(short opCode){
        MessageType messageType = MessageType.fromInteger(opCode);
        String ans = new String(bytes, start , len, StandardCharsets.UTF_8);
        ClientMessage clientMessage = null;
        switch (messageType){

            case REGISTER:
                clientMessage = new RegisterMessage();
                if (clientMessage.checkIfMessageIsValid(ans))
                    return clientMessage;
                break;
            case LOGIN:
                clientMessage = new LoginMessage();
                if (clientMessage.checkIfMessageIsValid(ans))
                    return clientMessage;
                break;
            case LOGOUT:
                clientMessage = new LogoutMessage();
                if (clientMessage.checkIfMessageIsValid(ans))
                    return clientMessage;
                break;
            case FOLLOW:
                clientMessage = new Follow_Unfollow_Message();
                if (clientMessage.checkIfMessageIsValid(ans))
                    return clientMessage;
                break;
            case POST:
                clientMessage = new PostMessage();
                if (clientMessage.checkIfMessageIsValid(ans))
                    return clientMessage;
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