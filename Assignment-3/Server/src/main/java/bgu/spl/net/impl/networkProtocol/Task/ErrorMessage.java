package bgu.spl.net.impl.networkProtocol.Task;

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
