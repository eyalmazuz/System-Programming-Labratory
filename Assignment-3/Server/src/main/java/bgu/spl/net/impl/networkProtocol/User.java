package bgu.spl.net.impl.networkProtocol;

import java.util.concurrent.ConcurrentLinkedQueue;

public class User implements Comparable<User> {
    private String name;
    private String password;
    private boolean loggedIn;
    private ConcurrentLinkedQueue<Integer> followerList;
    private ConcurrentLinkedQueue<Integer> followingList;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        loggedIn = false;
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

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
