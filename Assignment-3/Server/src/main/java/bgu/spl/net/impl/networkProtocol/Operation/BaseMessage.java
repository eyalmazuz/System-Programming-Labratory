package bgu.spl.net.impl.networkProtocol.Operation;

public abstract class BaseMessage {
    private int connectionID;

    public int getConnectionID() {
        return connectionID;
    }

    public BaseMessage(int connectionID) {
        this.connectionID = connectionID;
    }
}
