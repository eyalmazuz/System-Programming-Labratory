package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.NetworkEncoderDecoder;
import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.DatabaseImpl;
import bgu.spl.net.impl.networkProtocol.NetworkProtocol;
import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args) {
        Database database = new DatabaseImpl();
        Server.reactor(Runtime.getRuntime().availableProcessors(),
                Integer.valueOf(args[0]),
                () -> new NetworkProtocol(database),
                () -> new NetworkEncoderDecoder()).serve();

    }
}
