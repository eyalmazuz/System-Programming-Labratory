package bgu.spl.net.impl.networkProtocol;

public enum MessageType {

    REGISTER(1),
    LOGIN(2),
    LOGOUT(3),
    FOLLOW_UNFOLLOW(4),
    POST(5),
    PM(6),
    USERLIST(7),
    STAT(8);

    private final int optcode;
    //private
    MessageType(int optcode) {
        this.optcode = optcode;
    }

    public int getOptcode() {
        return optcode;
    }

    public static MessageType fromInteger(int optcode) {
        for (MessageType messageType : values())
            if (messageType.getOptcode() == optcode)
                return messageType;
        return null;
    }

}
