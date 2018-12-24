package bgu.spl.net.impl.networkProtocol.Task;

public class ErrorMessage {

    private int optCode;

    public ErrorMessage(int optCode){
        this.optCode  = optCode;
    }

    @Override
    public String toString() {
        return "Error " + optCode;
    }
}
