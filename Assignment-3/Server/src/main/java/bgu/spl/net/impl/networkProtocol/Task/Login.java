package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Operation.AckMessage;
import bgu.spl.net.impl.networkProtocol.Operation.ErrorMessage;
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
        if (database.isLoggedInByName(user.getName()) || database.isLoggedInbyConnId(connectionId)) {
            return new ErrorMessage(opCode).toString();
        }
        User check = database.getUserbyName(user.getName());
        if(check != null && user.compareTo(check) == 0){
            database.putNewLogin(user.getName(),connectionId);
            login = true;
        }
        if(login) return new AckMessage(opCode).toString();
        else return new ErrorMessage(opCode).toString();
    }
}
