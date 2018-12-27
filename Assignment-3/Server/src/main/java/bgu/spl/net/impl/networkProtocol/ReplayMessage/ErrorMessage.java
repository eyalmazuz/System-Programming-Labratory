package bgu.spl.net.impl.networkProtocol.ReplayMessage;

public class ErrorMessage implements ReplyMessage {

    private int opCode;

    public ErrorMessage(int opCode){
        this.opCode = opCode;
    }

    public String toString() {
        return "Error " + opCode;
    }
}
