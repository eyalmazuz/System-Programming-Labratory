package bgu.spl.net.impl.networkProtocol.Operation;

public abstract class ClientMessage {
    protected int opCode;
    protected String messageStr;
    public abstract boolean checkIfMessageIsValid(String msg);
    protected abstract void updateFields(String msg);

    @Override
    public String toString() {
        return messageStr;
    }
}
