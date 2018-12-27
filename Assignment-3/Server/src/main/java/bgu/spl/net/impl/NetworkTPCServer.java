package bgu.spl.net.impl;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.DatabaseImpl;
import bgu.spl.net.impl.networkProtocol.NetworkProtocol;
import bgu.spl.net.srv.Server;

public class NetworkTPCServer {


    public static void main(String[] args) {
        Database database = new DatabaseImpl();
        // you can use any server...
        Server.threadPerClient(7777/*Integer.valueOf(args[0])*/,
                () -> new NetworkProtocol(database),
                () -> new NetworkEncoderDecoder()).serve();

    }

}
