package bgu.spl.net.impl;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.NetworkProtocol;
import bgu.spl.net.srv.Server;

public class NetworkTPCServer {

    static Database database = new Database();

    public static void main(String[] args) {
        // you can use any server...
        Server.threadPerClient(7777/*Integer.valueOf(args[0])*/,
                () -> new NetworkProtocol(database),
                () -> new NetworkEncoderDecoder()).serve();

    }

}
