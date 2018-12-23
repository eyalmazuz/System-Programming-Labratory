package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.UsersManager;

import java.util.concurrent.ConcurrentHashMap;

public abstract  class BaseTask implements Task {
    protected static String fail=null;
    protected static String success =null;

    protected UsersManager userManager;
    protected ConcurrentHashMap<String,Integer> loggedInMap;
    protected int connectionId;
    protected int optCode;

    public BaseTask(UsersManager userManager, ConcurrentHashMap<String, Integer> loggedInMap, int connectionId, int optCode) {
        this.userManager = userManager;
        this.loggedInMap = loggedInMap;
        this.connectionId = connectionId;
        this.optCode = optCode;
        if (fail == null) fail = "ERROR " + optCode;
        if (success == null) success = "ACK " + optCode;
    }

    public abstract String run();
}
