package bgu.spl.net.impl.networkProtocol;

public class Message {

    private String content;
    private long timeStamp;
    private String userName;

    public Message(String content, long timeStamp, String userName){
        this.userName = userName;
        this.content = content;
        this.timeStamp = timeStamp;
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
