package bgu.spl.net.impl.networkProtocol;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

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
        return users.stream().filter(u -> u.getName().equals(userName)).count() > 0 ? users.stream().filter(u -> u.getName().equals(userName)).findFirst().get() : null ;
    }

    public User getUserByConnectionID(int connectionId){
        for(Map.Entry<String,Integer> user:loggedInMap.entrySet() )
            if(user.getValue() ==connectionId) {
                return getUserbyName(user.getKey());
            }
        return null;
    }

    public int getConnetionIdByName(String name){
        return loggedInMap.keySet().stream().filter(u -> u.equals(name)).count() > 0 ? loggedInMap.get(name) : 0;
    }

    public void addUser(User user){
        users.add(user);
    }

    public String getNumOfUsers() {

        return String.valueOf(users.stream().count());
    }

    public ArrayList<User> getUsers() {

        return new ArrayList<>(users);
    }

    public boolean isLoggedInbyConnId(int connectionId){
        return loggedInMap.containsValue(connectionId);
    }

    public boolean isLoggedInByName(String user) {
        return loggedInMap.containsKey(user);
    }

    public void putNewLogin(String name, int connectionId) {
        loggedInMap.put(name, connectionId);
    }

    public void removeUser(int connectionId) {
        loggedInMap.remove(getUserByConnectionID(connectionId));
    }
}
