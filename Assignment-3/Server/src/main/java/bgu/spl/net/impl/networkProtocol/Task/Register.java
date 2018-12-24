package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.UsersManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Register extends BaseTask {
    private User user;

    public Register(UsersManager userManager, int connectionId, int optCode, User user) {
        super(userManager, connectionId, optCode);
        this.user = user;
    }

    @Override
    public String run() {
        ConcurrentHashMap<String, Integer> loggedInMap= userManager.getLoggedInMap();
        if(loggedInMap.containsKey(user.getName()) || loggedInMap.containsValue(this.connectionId)) {
            return fail;
        }
        ConcurrentLinkedQueue<User> users = userManager.acquireUsersWriteLock();
        for (User user : users){
            if(this.user.compareTo(user) == 0){
                this.userManager.releaseUsersWriteLock();
                return fail;
            }
        }
        User user = new User(this.user.getName(),this.user.getPassword());
        users.add(user);
        this.userManager.releaseUsersWriteLock();
        return success;
    }
}
