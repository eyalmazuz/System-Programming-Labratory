package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.networkProtocol.Operation.NetworkMessage;

public class NetworkProtocolTester implements BidiMessagingProtocol<NetworkMessage> {
    @Override
    public void start(int connectionId, Connections<NetworkMessage> connections) {

    }

    @Override
    public void process(NetworkMessage message) {

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
