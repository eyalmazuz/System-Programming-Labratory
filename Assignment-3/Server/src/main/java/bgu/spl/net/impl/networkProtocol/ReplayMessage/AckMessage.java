package bgu.spl.net.impl.networkProtocol.ReplayMessage;

public class AckMessage implements ReplyMessage {

    private int opCode;
    private String[] data;
    public AckMessage(int opCode, String...data){
        this.opCode = opCode;
        this.data = data;
    }


    public String toString() {
        StringBuilder message = new StringBuilder("ACK " + opCode);
        for (String item :data) {
            message.append(" ").append(item);

        }
        return message.toString().substring(0, message.length());
    }
}
