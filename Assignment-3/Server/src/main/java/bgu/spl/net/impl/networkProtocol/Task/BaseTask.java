package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Database;
import bgu.spl.net.impl.networkProtocol.Operation.BaseMessage;
import bgu.spl.net.impl.networkProtocol.Operation.NetworkMessage;

public abstract  class BaseTask <T> implements Task {

    protected Database database;
    protected int connectionId;
    protected int opCode;

    public abstract boolean checkIfMessageIsValid(String msg);
    protected abstract void updateFields(String msg);

    public BaseTask() {
        this.database = database;
        this.connectionId = connectionId;
        this.opCode = opCode;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public abstract NetworkMessage run();
}
