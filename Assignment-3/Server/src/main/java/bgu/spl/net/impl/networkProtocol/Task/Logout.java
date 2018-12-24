package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.UsersManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Logout extends BaseTask {
    public Logout(UsersManager userManager, int connectionId, int optCode) {
        super(userManager, connectionId, optCode);
    }

    @Override
    public String run() {
        ConcurrentHashMap<String, Integer> loggedInMap= userManager.getLoggedInMap();
        if(loggedInMap.containsValue(connectionId))
        {
            for(Map.Entry<String,Integer> user:loggedInMap.entrySet() )
                if(user.getValue() ==connectionId) {
                    loggedInMap.remove(user.getKey());
                }

            return success;
        }
        return fail;
    }
}
