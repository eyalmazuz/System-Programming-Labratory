package bgu.spl.net.impl.networkProtocol;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class User implements Comparable<User> {
    private String name;
    private String password;
    private ConcurrentLinkedQueue<String> followerList;
    private ConcurrentLinkedQueue<String> followingList;
    private long timeStamp;
    private ConcurrentLinkedQueue<Message> messageList;
    private ConcurrentLinkedQueue<Message> postList;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        followerList = new ConcurrentLinkedQueue<>();
        followingList = new ConcurrentLinkedQueue<>();
        timeStamp = 0;
        messageList = new ConcurrentLinkedQueue<>();
        postList = new ConcurrentLinkedQueue<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int compareTo(User o) {
       int out = getName().compareTo(o.name);
       if (out == 0) out = getPassword().compareTo(o.password);
       return out;
    }

    public void updateTimeStamp(){
        timeStamp = System.currentTimeMillis();
    }

    public void addFollowers(ArrayList<String> userNameList) {
        followingList.addAll(userNameList);
    }

    public void unfollowUsers(ArrayList<String> userNameList){
        followingList.removeAll(userNameList);
    }

    public boolean isFollow(String u) {
        return followingList.stream().filter(user -> user.equals(u)).count() > 0;
    }

    public void removeFollower(String user){
        followerList.remove(user);
    }

    public void addFollower(String name) {
        followerList.add(name);
    }

    public ArrayList<String> getFollowers() {
        return followerList.stream().collect(Collectors.toCollection(ArrayList::new));
    }

    public void addPost(Message message) {
        postList.add(message);

    }

    public void addMessage(Message message){
        messageList.add(message);
    }

    public String getNumOfPost() {

        return String.valueOf(postList.stream().count());
    }

    public String getNumOfFollowers() {

        return String.valueOf(followerList.stream().count());
    }

    public String getNumOfFollowing() {

        return String.valueOf(followingList.stream().count());
    }

    public ArrayList<Message> getMessages() {

       return messageList.stream().collect(Collectors.toCollection(ArrayList::new));
    }

    public long getLogoutTime() {

        return timeStamp;
    }
}
