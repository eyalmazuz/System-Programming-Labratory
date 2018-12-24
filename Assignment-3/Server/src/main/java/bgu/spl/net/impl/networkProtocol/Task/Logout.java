package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.UsersManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Logout extends BaseTask {
    public Logout(UsersManager userManager, int connectionId, int optCode) {
        super(userManager, connectionId, optCode);
    }

    @Override
    public String run() {
        ConcurrentHashMap<User, Integer> users= userManager.acquireUsersReadLock();
        if(users.keySet().stream().filter(f -> f.isLoggedIn()).findAny().isPresent())
        {
            userManager.releaseUsersReadLock();
            users= userManager.acquireUsersWriteLock();
            users.keySet().stream().filter(f -> f.compareTo(new User(userName, password)) == 0).findAny().get().setLoggedIn(false);
            userManager.releaseUsersWriteLock();
            return new AckMessage(optCode).toString();
        }
        else{
            userManager.releaseUsersReadLock();
            return new ErrorMessage(optCode).toString();
        }

    }
}
