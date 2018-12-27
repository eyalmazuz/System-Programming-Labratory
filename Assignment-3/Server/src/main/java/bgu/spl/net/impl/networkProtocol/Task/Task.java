package bgu.spl.net.impl.networkProtocol.Task;


import bgu.spl.net.impl.networkProtocol.NetworkMessage;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;

public interface Task <T> extends NetworkMessage {

    public ReplyMessage run(T arg, int connectionId);

    public abstract boolean checkIfMessageIsValid(String msg);
    public abstract void updateFields(String msg);

}
