package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserList {

    private ConcurrentLinkedQueue<User> users;

    public UserList(ArrayList<User> users){
        this.users = new ConcurrentLinkedQueue<>();
        this.users.addAll(users);

    }

    @Override
    public String toString() {
        String message = "";
        for (User user :users) {
            message = message + user.getName() + " ";

        }
        return message;
    }
}
