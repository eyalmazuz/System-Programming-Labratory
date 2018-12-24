package bgu.spl.net.impl.networkProtocol.Task;

import java.util.ArrayList;

public class AckMessage {

    int optCode;
    Object[] optional;
    public AckMessage(int optCode, Object... optional){
        this.optCode = optCode;
        this.optional = optional;
    }
    //TODO TOSTRING

    @Override
    public String toString() {
        return "ACK " + optCode;
    }
}


