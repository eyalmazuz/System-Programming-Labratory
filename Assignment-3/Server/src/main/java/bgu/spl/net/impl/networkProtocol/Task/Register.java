package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Operation.AckMessage;
import bgu.spl.net.impl.networkProtocol.Operation.NetworkMessage;
import bgu.spl.net.impl.networkProtocol.Operation.ErrorMessage;
import bgu.spl.net.impl.networkProtocol.Operation.RegisterMessage;
import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.Database;

import java.util.concurrent.ConcurrentHashMap;

public class Register extends BaseTask <NetworkMessage> {
    private RegisterMessage user;

    public Register(Database database, int connectionId, int opCode, RegisterMessage userRegisterMessage) {
        super(database, connectionId, opCode);
        this.user = userRegisterMessage;
    }

    @Override
    public NetworkMessage run() {
        ConcurrentHashMap<String, Integer> loggedInMap= database.getLoggedInMap();
        if(loggedInMap.containsKey(user.getUsername()) || loggedInMap.containsValue(this.connectionId)) {
            return new ErrorMessage(opCode);
        }
        User check = database.getUserbyName(user.getUsername());
        if(check != null){
            return new ErrorMessage(opCode);
        }

        User user = new User(this.user.getUsername(),this.user.getPassword());
        database.addUser(user);
        return new AckMessage(opCode);
    }
}
