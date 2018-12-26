package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.networkProtocol.Operation.ClientMessage;

public class NetworkProtocolTester implements BidiMessagingProtocol<ClientMessage> {
    @Override
    public void start(int connectionId, Connections<ClientMessage> connections) {

    }

    @Override
    public void process(ClientMessage message) {

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
