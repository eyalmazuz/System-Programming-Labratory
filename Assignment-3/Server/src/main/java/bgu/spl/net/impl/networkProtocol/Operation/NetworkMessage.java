package bgu.spl.net.impl.networkProtocol.Operation;

public abstract class NetworkMessage {
    protected int opCode;
    protected String messageStr;
    public abstract boolean checkIfMessageIsValid(String msg);
    protected abstract void updateFields(String msg);

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    @Override
    public String toString() {
        return messageStr;
    }


}
