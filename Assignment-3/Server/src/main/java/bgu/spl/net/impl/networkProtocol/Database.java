package bgu.spl.net.impl.networkProtocol;

import bgu.spl.net.impl.networkProtocol.ReplayMessage.ReplyMessage;

import java.util.ArrayList;

public interface Database{


    ReplyMessage regsiterCommand(int connectionId, String username, String password);

    ReplyMessage loginCommand(int connectionId, String s, String username);

    ReplyMessage LogoutCommand(int connectionId);

    ReplyMessage followCommand(int connectionId, ArrayList<String> users, int sign);

    ArrayList<String> postCommand(String content, int connectionId);

    ReplyMessage pmCommand(int connectionId, String username);

    ReplyMessage userListCommand(int connectionId);

    ReplyMessage statCommand(int connectionId, String username);


    boolean isLoggedInbyConnId(int connectionId);

    boolean isLoggedInByName(String user);

    int getConnetionIdByName(String user);

    User getUserbyName(String user);

    User getUserByConnectionID(int connectionId);
}
