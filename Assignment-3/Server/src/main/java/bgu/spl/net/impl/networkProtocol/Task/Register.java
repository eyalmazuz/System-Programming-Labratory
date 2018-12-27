package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Operation.NetworkMessage;
import bgu.spl.net.impl.networkProtocol.Operation.RegisterMessage;
import bgu.spl.net.impl.networkProtocol.Database;

public class Register extends BaseTask <NetworkMessage> {
    private RegisterMessage user;

    public Register(Database database, int connectionId, int opCode, RegisterMessage userRegisterMessage) {
        super();
        this.user = userRegisterMessage;
    }

    @Override
    public NetworkMessage run(Database database) {
        database.regsiterCommand(connectionId, username, password);
    }
}
