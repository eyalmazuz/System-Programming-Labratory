package bgu.spl.net.impl.networkProtocol.Task;

public class Notification {

    private int opCode;
    private String type;
    private String userName;
    private String content;

    public Notification(int opCode, String type, String userName, String content){
        this.opCode = opCode;
        this.type = type;
        this.userName = userName;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Notifiction" + opCode + " " + type + " " + userName + '\0' + content + '\0';
    }
}
