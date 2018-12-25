package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;

public abstract  class BaseTask implements Task {

    protected Database database;
    protected int connectionId;
    protected int opCode;

    public BaseTask(Database database, int connectionId, int opCode) {
        this.database = database;
        this.connectionId = connectionId;
        this.opCode = opCode;
    }

    public abstract String run();
}
