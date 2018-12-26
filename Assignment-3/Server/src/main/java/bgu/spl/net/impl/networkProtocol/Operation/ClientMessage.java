package bgu.spl.net.impl.networkProtocol.Operation;

public abstract class ClientMessage {
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

    protected short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
}
