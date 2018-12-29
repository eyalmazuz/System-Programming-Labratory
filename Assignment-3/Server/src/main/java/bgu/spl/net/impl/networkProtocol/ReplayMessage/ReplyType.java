package bgu.spl.net.impl.networkProtocol.ReplayMessage;

public enum ReplyType {
    NOTIFICATION(9),
    ACK(10),
    ERROR(11);

    private final int opcode;
    //private
    ReplyType(int opcode) {
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }

    public static ReplyType fromInteger(int opcode) {
        for (ReplyType replyType : values())
            if (replyType.getOpcode() == opcode)
                return replyType;
        return null;
    }
}
