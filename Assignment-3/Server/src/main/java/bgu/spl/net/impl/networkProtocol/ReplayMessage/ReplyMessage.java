package bgu.spl.net.impl.networkProtocol.ReplayMessage;

import bgu.spl.net.impl.networkProtocol.NetworkMessage;

public interface ReplyMessage extends NetworkMessage {
    public byte[] encode();
}
