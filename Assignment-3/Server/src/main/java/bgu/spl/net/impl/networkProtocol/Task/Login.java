package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.UsersManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Login extends BaseTask {
    private String userName;
    private String password;
    public Login(UsersManager userManager, int connectionId, int optCode, String userName, String password) {
        super(userManager, connectionId, optCode);
        this.userName = userName;
        this.password = password;

    }

    @Override
    public String run() {
        boolean login = false;
        ConcurrentHashMap<User, Integer> users = userManager.acquireUsersReadLock();
        if (users.keySet().stream().filter(f -> f.compareTo(new User(userName, password)) != 0 || f.isLoggedIn()).findAny().isPresent()) {
            userManager.releaseUsersReadLock();
            return new ErrorMessage(optCode).toString();
        } else {
            userManager.releaseUsersReadLock();
            users = userManager.acquireUsersWriteLock();
            users.keySet().stream().filter(f -> f.compareTo(new User(userName, password)) == 0).findAny().get().setLoggedIn(true);
            userManager.releaseUsersWriteLock();
            login = true;
        }
        if (login) {
            return new AckMessage(optCode).toString();
        } else return new ErrorMessage(optCode).toString();
    }
}
