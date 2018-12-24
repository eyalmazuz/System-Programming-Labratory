package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.UsersManager;

public abstract  class BaseTask implements Task {
    protected String fail=null;
    protected String success =null;

    protected UsersManager userManager;
    protected int connectionId;
    protected int optCode;

    public BaseTask(UsersManager userManager, int connectionId, int optCode) {
        this.userManager = userManager;
        this.connectionId = connectionId;
        this.optCode = optCode;
        this.fail = "ERROR " + optCode;
        this.success = "ACK " + optCode;
    }

    public abstract String run();
}
