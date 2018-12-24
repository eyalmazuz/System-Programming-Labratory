package bgu.spl.net.impl;

import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.impl.networkProtocol.NetworkProtocol;
import bgu.spl.net.impl.networkProtocol.UsersManager;
import bgu.spl.net.srv.Server;

import java.util.concurrent.ConcurrentHashMap;

public class NetworkTPCServer {

    static UsersManager manager = new UsersManager();

    public static void main(String[] args) {
        // you can use any server...
        Server.threadPerClient(7777/*Integer.valueOf(args[0])*/,
                ()-> new NetworkProtocol(manager),
                () -> new LineMessageEncoderDecoder()).serve();

    }

}
