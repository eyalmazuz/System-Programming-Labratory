package bgu.spl.net.impl.networkProtocol.Operation;

public class ErrorMessage extends NetworkMessage {

    private int opCode;

    public ErrorMessage(int opCode){
        this.opCode = opCode;
    }

    @Override
    public boolean checkIfMessageIsValid(String msg) {
        return true;
    }

    @Override
    protected void updateFields(String msg) {

    }

    @Override
    public String toString() {
        return "Error " + opCode;
    }
}
