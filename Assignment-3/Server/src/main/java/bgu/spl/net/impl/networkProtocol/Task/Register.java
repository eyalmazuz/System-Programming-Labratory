package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.UsersManager;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Register extends BaseTask {

    String userName;
    String password;

    public Register(UsersManager userManager,int connectionId, int optCode, String userName, String password) {
        super(userManager, connectionId, optCode);
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String run() {

        ConcurrentHashMap<User, Integer> users = userManager.acquireUsersReadLock();

        if(users.keySet().stream().filter(f -> f.compareTo(new User(userName, password)) == 0).findAny().isPresent()) {
            userManager.releaseUsersReadLock();
            return new ErrorMessage(optCode).toString();
        }
        else {
            userManager.releaseUsersReadLock();
            users = userManager.acquireUsersWriteLock();
            User user = new User(userName, password);
            users.put(user, connectionId);
            this.userManager.releaseUsersWriteLock();
            return new AckMessage(optCode).toString();
        }
    }
}
