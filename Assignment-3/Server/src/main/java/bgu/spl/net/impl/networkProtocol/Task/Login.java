package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.Database;

import java.util.concurrent.ConcurrentHashMap;

public class Login extends BaseTask <String>{
    private User user;

    public Login(Database database, int connectionId, int opCode, User user) {
        super(database, connectionId, opCode);
        this.user = user;
    }

    @Override
    public String run() {
        boolean login=false;
        ConcurrentHashMap<String, Integer> loggedInMap= database.getLoggedInMap();
        if (loggedInMap.containsKey(user.getName())||loggedInMap.containsValue(connectionId)) {
            return new ErrorMessage(opCode).toString();
        }
        User check = database.getUserbyName(user.getName());
        if(check != null && user.compareTo(check) == 0){
            loggedInMap.put(user.getName(),connectionId);
            login = true;
        }
        if(login) return new AckMessage(opCode).toString();
        else return new ErrorMessage(opCode).toString();
    }
}
