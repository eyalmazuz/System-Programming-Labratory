package bgu.spl.net.impl.networkProtocol;

public enum MessageType {

    REGISTER(1),
    LOGIN(2),
    LOGOUT(3),
    FOLLOW(4),
    POST(5),
    PM(6),
    USERLIST(7),
    STAT(8);

    private final int opcode;
    //private
    MessageType(int opcode) {
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }

    public static MessageType fromInteger(int opcode) {
        for (MessageType messageType : values())
            if (messageType.getOpcode() == opcode)
                return messageType;
        return null;
    }

}
