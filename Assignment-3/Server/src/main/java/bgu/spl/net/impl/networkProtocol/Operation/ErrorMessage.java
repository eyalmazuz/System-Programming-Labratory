package bgu.spl.net.impl.networkProtocol.Operation;

public class ErrorMessage {

    private int opCode;

    public ErrorMessage(int opCode){
        this.opCode = opCode;
    }

    @Override
    public String toString() {
        return "Error " + opCode;
    }
}
