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

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        followerList = new ConcurrentLinkedQueue<>();
        followingList = new ConcurrentLinkedQueue<>();
        timeStamp = 0;
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
}
