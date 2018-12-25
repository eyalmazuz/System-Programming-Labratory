package bgu.spl.net.impl.networkProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//ToDo: check if necessary to lock/unblock ConcurrentHashMap<String, Integer> loggedInMap;
public class Database {
    private ReentrantReadWriteLock usersRWLock;
    private ConcurrentLinkedQueue<User> users;
    private ConcurrentHashMap<String, Integer> loggedInMap;


    public Database() {
        this.usersRWLock = new ReentrantReadWriteLock();
        this.users = new ConcurrentLinkedQueue<>();
        this.loggedInMap = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, Integer> getLoggedInMap() {
        return loggedInMap;
    }

    public User getUserbyName(String userName){
        return users.stream().filter(u -> u.getName().equals(userName)).findFirst().get();
    }

    public User getUserByConnectionID(int connectionId){
        for(Map.Entry<String,Integer> user:loggedInMap.entrySet() )
            if(user.getValue() ==connectionId) {
                return getUserbyName(user.getKey());
            }
        return null;
    }

    public void addUser(User user){
        users.add(user);
    }
}
