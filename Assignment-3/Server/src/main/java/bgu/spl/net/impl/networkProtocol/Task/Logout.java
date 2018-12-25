package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Logout extends BaseTask {
    public Logout(Database database, int connectionId, int opCode) {
        super(database, connectionId, opCode);
    }

    @Override
    public String run() {
        ConcurrentHashMap<String, Integer> loggedInMap= database.getLoggedInMap();
        if(loggedInMap.containsValue(connectionId))
        {
            for(Map.Entry<String,Integer> user:loggedInMap.entrySet() )
                if(user.getValue() ==connectionId) {
                    database.getUserbyName(user.getKey()).updateTimeStamp();
                    loggedInMap.remove(user.getKey());
                }

            return new AckMessage(opCode).toString();
        }
        return new ErrorMessage(opCode).toString();
    }
}
