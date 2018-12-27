package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.Operation.AckMessage;
import bgu.spl.net.impl.networkProtocol.Operation.ErrorMessage;
import bgu.spl.net.impl.networkProtocol.Operation.NetworkMessage;

public class Logout extends BaseTask <NetworkMessage>{
    public Logout(Database database, int connectionId, int opCode) {
        super();
    }

    @Override
    public NetworkMessage run() {
        if(database.isLoggedInbyConnId(connectionId)) {
            database.getUserByConnectionID(connectionId).updateTimeStamp();
            database.removeUser(connectionId);


            return new AckMessage(opCode);
        }
        return new ErrorMessage(opCode);
    }
}
