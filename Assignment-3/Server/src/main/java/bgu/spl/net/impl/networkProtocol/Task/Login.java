package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.UsersManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Login extends BaseTask {
    private User user;

    public Login(UsersManager userManager, ConcurrentHashMap<String, Integer> loggedInMap, int connectionId, int optCode, User user) {
        super(userManager, loggedInMap, connectionId, optCode);
        this.user = user;
    }

    @Override
    public String run() {
        boolean login=false;
        if (loggedInMap.containsKey(user.getName())||loggedInMap.containsValue(connectionId)) return fail;
        ConcurrentLinkedQueue<User> users= userManager.acquireUsersReadLock();
        for(User user:users)
            if(this.user.compareTo(user) == 0) {
                loggedInMap.put(user.getName(),connectionId);
                login = true;
            }
        userManager.releaseUsersReadLock();
        if(login) return success;
        else return fail;
    }
}
