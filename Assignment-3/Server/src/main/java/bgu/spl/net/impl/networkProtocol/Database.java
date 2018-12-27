package bgu.spl.net.impl.networkProtocol;

import bgu.spl.net.impl.networkProtocol.Operation.NetworkMessage;

import java.util.ArrayList;

public interface Database <T>{


    NetworkMessage regsiterCommand(int connectionId, String username, String password);

    T loginCommand();

    T LogoutCommand();

    T followCommand();

    ArrayList<String> postCommand(String content);

    T pmCommand();

    T userListCommand();

    T statCommand();


}
