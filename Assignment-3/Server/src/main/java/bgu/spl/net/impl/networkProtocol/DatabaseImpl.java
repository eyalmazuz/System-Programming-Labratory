package bgu.spl.net.impl.networkProtocol;

import bgu.spl.net.impl.networkProtocol.ReplayMessage.AckMessage;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ErrorMessage;
import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;
import bgu.spl.net.impl.networkProtocol.Task.UserListMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//ToDo: check if necessary to lock/unblock ConcurrentHashMap<String, Integer> loggedInMap;
public class DatabaseImpl implements Database{
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
        User check = getUserByConnectionID(connectionId);
        if(check != null) {
            loggedInMap.remove(check.getName());
        }
    }

    @Override
    public ReplyMessage regsiterCommand(int connectionId, String username, String password) {
        int opCode = MessageType.REGISTER.getOpcode();
        synchronized (loggedInMap) {
            if (loggedInMap.containsKey(username) || loggedInMap.containsValue(connectionId)) {
                return new ErrorMessage(opCode);
            }
            User check = getUserbyName(username);
            if (check != null) {
                return new ErrorMessage(opCode);
            }

            User user = new User(username, password);
            addUser(user);
        }
        return new AckMessage(opCode);
    }

    @Override
    public ReplyMessage loginCommand(int connectionId, String username, String password) {
        int opCode = MessageType.LOGIN.getOpcode();
        boolean login=false;
        if (loggedInMap.containsKey(username) || loggedInMap.containsValue(connectionId)) {
            return new ErrorMessage(opCode);
        }
        User check = users.stream().filter(u -> u.getName().equals(username)).count() > 0 ? users.stream().filter(u -> u.getName().equals(username)).findFirst().get() : null ;
        if(check != null && check.compareTo(new User(username,password)) == 0){
            loggedInMap.put(username,connectionId);
            login = true;
        }
        if(login)
            return new AckMessage(opCode);
        else
            return new ErrorMessage(opCode);
    }

    @Override
    public ReplyMessage LogoutCommand(int connectionId) {
        int opCode = MessageType.LOGOUT.getOpcode();
        if(isLoggedInbyConnId(connectionId)) {
            if (loggedInMap.containsValue(connectionId)) {
                getUserByConnectionID(connectionId).updateTimeStamp();
                removeUser(connectionId);

                return new AckMessage(opCode);
            }
            return new ErrorMessage(opCode);
        }else{
            return new ErrorMessage(opCode);
        }
    }

    @Override
    public ArrayList<String> postCommand(String content, int connectionId) {
        ArrayList<String> users = new ArrayList<>();
        Matcher m = Pattern.compile("(?=@([^\\s]+))").matcher(content);
        while(m.find()){
            String user = m.group(1);
            if(getUserbyName(user) != null)
                users.add(user);
        }
        users.addAll(getUserByConnectionID(connectionId).getFollowers());
        return users;
    }

    @Override
    public ReplyMessage followCommand(int connectionId, ArrayList<String> users, int sign) {
        int opCode = MessageType.FOLLOW.getOpcode();
        if(isLoggedInbyConnId(connectionId)) {
            int succesfull = 0;
            ArrayList<String> userList = null;
            if (sign == 0) {
                if (loggedInMap.containsValue(connectionId)) {
                    User user = getUserByConnectionID(connectionId);
                    if (user != null) {
                        userList = users.stream()
                                .filter(u -> getUserbyName(u) != null && !getUserByConnectionID(connectionId).isFollow(u))
                                .collect(Collectors.toCollection(ArrayList::new));
                        userList.stream().forEach(u -> getUserbyName(u).addFollower(user.getName()));
                        succesfull = userList.size();
                        if (succesfull > 0) {
                            user.addFollowers(userList);
                        } else if (succesfull == 0) {
                            return new ErrorMessage(opCode);
                        }
                    } else {
                        return new ErrorMessage(opCode);
                    }
                }
            } else if (sign == 1) {
                if (loggedInMap.containsValue(connectionId)) {
                    User user = getUserByConnectionID(connectionId);
                    if (user != null) {
                        userList = users.stream()
                                .filter(u -> getUserbyName(u) != null && getUserByConnectionID(connectionId).isFollow(u))
                                .collect(Collectors.toCollection(ArrayList::new));
                        userList.stream().forEach(u -> getUserbyName(u).removeFollower(user.getName()));
                        succesfull = userList.size();
                        if (succesfull > 0) {
                            user.unfollowUsers(userList);
                            //return new AckMessage(opCode);
                        } else if (succesfull == 0) {
                            return new ErrorMessage(opCode);
                        }
                    } else {
                        return new ErrorMessage(opCode);
                    }
                }

            }
            StringBuilder userlistSB = new StringBuilder("");
            userList.stream().forEach(u -> userlistSB.append(u + " "));
            return new AckMessage(opCode,succesfull+"", userlistSB.toString().trim());
        }
        else{
            return new ErrorMessage(opCode);
        }
    }

    @Override
    public String pmCommand(int connectionId, String username) {

        if (getUserbyName(username) != null){
            return username;
        }
        return null;
    }

    @Override
    public ReplyMessage userListCommand(int connectionId) {
        int opCode = MessageType.USERLIST.getOpcode();
        if(isLoggedInbyConnId(connectionId)) {
            StringBuilder userlist = new StringBuilder("");
            users.stream().forEach(u -> userlist.append(u.getName()).append(" "));
            return new AckMessage(opCode, getNumOfUsers(), userlist.toString().trim());
        }else{
            return new ErrorMessage(opCode);
        }    }

    @Override
    public ReplyMessage statCommand(int connectionId, String username) {
        int opCode = MessageType.STAT.getOpcode();
        if(isLoggedInbyConnId(connectionId)) {
            return new AckMessage(opCode, getUserbyName(username).getNumOfPost(), getUserbyName(username).getNumOfFollowers(), getUserbyName(username).getNumOfFollowing());
        }
        else{
            return new ErrorMessage(opCode);
        }
    }

    public static interface Message{

    }
}
