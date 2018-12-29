package bgu.spl.net.impl.networkProtocol;

public enum NotificationType {
    PM(0),
    PUBLIC(1);

    private final int opcode;
    //private
    NotificationType(int opcode) {
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }

    public static NotificationType fromInteger(int opcode) {
        for (NotificationType messageType : values())
            if (messageType.getOpcode() == opcode)
                return messageType;
        return null;
    }
}
