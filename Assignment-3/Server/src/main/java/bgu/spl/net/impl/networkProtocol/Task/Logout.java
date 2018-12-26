package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Logout extends BaseTask <String>{
    public Logout(Database database, int connectionId, int opCode) {
        super(database, connectionId, opCode);
    }

    @Override
    public String run() {
        if(database.isLoggedInbyConnId(connectionId)) {
            database.getUserByConnectionID(connectionId).updateTimeStamp();
            database.removeUser(connectionId);


            return new AckMessage(opCode).toString();
        }
        return new ErrorMessage(opCode).toString();
    }
}
