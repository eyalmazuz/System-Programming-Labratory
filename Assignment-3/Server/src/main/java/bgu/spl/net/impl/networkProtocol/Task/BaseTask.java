package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.UsersManager;

import java.util.concurrent.ConcurrentHashMap;

public abstract  class BaseTask implements Task {

    protected UsersManager userManager;
    protected int connectionId;
    protected int optCode;

    public BaseTask(UsersManager userManager, int connectionId, int optCode) {
        this.userManager = userManager;
        this.connectionId = connectionId;
        this.optCode = optCode;
    }

    public abstract String run();
}
