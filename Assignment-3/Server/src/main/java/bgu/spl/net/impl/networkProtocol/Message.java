package bgu.spl.net.impl.networkProtocol;

public class Message {

    private String content;
    private long timeStamp;
    private String userName;
    private NotificationType notificationType;

    public Message(String content, long timeStamp, String userName, NotificationType notificationType){
        this.userName = userName;
        this.content = content;
        this.timeStamp = timeStamp;
        this.notificationType = notificationType;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public long getTime(){
        return timeStamp;
    }

    public String getMessage(){
        return content;
    }

    public String getUserName(){
        return  userName;
    }
}
