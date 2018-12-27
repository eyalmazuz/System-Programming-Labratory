package bgu.spl.net.impl.networkProtocol;

import bgu.spl.net.impl.networkProtocol.Operation.AckMessage;
import bgu.spl.net.impl.networkProtocol.Operation.ErrorMessage;
import bgu.spl.net.impl.networkProtocol.Operation.NetworkMessage;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//ToDo: check if necessary to lock/unblock ConcurrentHashMap<String, Integer> loggedInMap;
public class DatabaseImpl implements Database <NetworkMessage>{
    private ReentrantReadWriteLock usersRWLock;
    private ConcurrentLinkedQueue<User> users;
    private ConcurrentHashMap<String, Integer> loggedInMap;


    public DatabaseImpl() {
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

    @Override
    public NetworkMessage regsiterCommand(int connectionId, String username, String password) {
        int opCode = 1;
        if(loggedInMap.containsKey(username) || loggedInMap.containsValue(connectionId)) {
            return new ErrorMessage(opCode);
        }
        User check = getUserbyName(username);
        if(check != null){
            return new ErrorMessage(opCode);
        }

        User user = new User(username,password);
        addUser(user);
        return new AckMessage(opCode);
    }

    @Override
    public NetworkMessage loginCommand() {
        return null;
    }

    @Override
    public NetworkMessage LogoutCommand() {
        return null;
    }

    @Override
    public NetworkMessage followCommand() {
        return null;
    }

    @Override
    public ArrayList<String> postCommand(String content) {
        return null;
    }

    @Override
    public NetworkMessage pmCommand() {
        return null;
    }

    @Override
    public NetworkMessage userListCommand() {
        return null;
    }

    @Override
    public NetworkMessage statCommand() {
        return null;
    }
}
