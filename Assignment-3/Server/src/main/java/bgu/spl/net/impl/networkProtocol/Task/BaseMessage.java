package bgu.spl.net.impl.networkProtocol.Task;

public abstract class BaseMessage {
    private int connectionID;

    public int getConnectionID() {
        return connectionID;
    }

    public BaseMessage(int connectionID) {
        this.connectionID = connectionID;
    }
}
