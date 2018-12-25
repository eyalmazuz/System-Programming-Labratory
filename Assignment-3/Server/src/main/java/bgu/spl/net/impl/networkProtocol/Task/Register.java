package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.Database;

import java.util.concurrent.ConcurrentHashMap;

public class Register extends BaseTask {
    private User user;

    public Register(Database database, int connectionId, int opCode, User user) {
        super(database, connectionId, opCode);
        this.user = user;
    }

    @Override
    public String run() {
        ConcurrentHashMap<String, Integer> loggedInMap= database.getLoggedInMap();
        if(loggedInMap.containsKey(user.getName()) || loggedInMap.containsValue(this.connectionId)) {
            return new ErrorMessage(opCode).toString();
        }
        User check = database.getUserbyName(user.getName());
        if(check != null){
            return new ErrorMessage(opCode).toString();
        }

        User user = new User(this.user.getName(),this.user.getPassword());
        database.addUser(user);
        return new AckMessage(opCode).toString();
    }
}
