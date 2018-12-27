package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Operation.AckMessage;
import bgu.spl.net.impl.networkProtocol.Operation.ErrorMessage;
import bgu.spl.net.impl.networkProtocol.Operation.Follow_Unfollow_Message;
import bgu.spl.net.impl.networkProtocol.Operation.NetworkMessage;
import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.Database;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Follow_Unfollow extends BaseTask <NetworkMessage> {

    private Follow_Unfollow_Message follow_unfollow_message;

    //private int sign;
    private int succesfull;

    public Follow_Unfollow(Database database, int connectionId, int opCode, Follow_Unfollow_Message follow_unfollow_message) {
        super();

        this.follow_unfollow_message = follow_unfollow_message;
        succesfull = 0;
    }

    @Override
    public NetworkMessage run() {
        if (follow_unfollow_message.getSign() == 0) {
            ConcurrentHashMap<String, Integer> loggedInMap = database.getLoggedInMap();
            if (loggedInMap.containsValue(connectionId)) {
                User user = database.getUserByConnectionID(connectionId);
                if (user != null) {
                    ArrayList<String> userList = follow_unfollow_message.getUserNameList().stream()
                            .filter(u -> database.getUserbyName(u) != null && !database.getUserByConnectionID(connectionId).isFollow(u))
                            .collect(Collectors.toCollection(ArrayList::new));
                    userList.stream().forEach(u -> database.getUserbyName(u).addFollower(user.getName()));
                    succesfull = userList.size();
                    if (succesfull > 0) {
                        user.addFollowers(userList);
                    } else if (succesfull == 0) {
                        return new ErrorMessage(opCode);
                    }
                } else {
                    return new ErrorMessage(opCode);
                }
            }
        } else if (follow_unfollow_message.getSign() == 1) {
            ConcurrentHashMap<String, Integer> loggedInMap = database.getLoggedInMap();
            if (loggedInMap.containsValue(connectionId)) {
                User user = database.getUserByConnectionID(connectionId);
                if (user != null) {
                    ArrayList<String> userList = follow_unfollow_message.getUserNameList().stream()
                            .filter(u -> database.getUserbyName(u) != null && database.getUserByConnectionID(connectionId).isFollow(u))
                            .collect(Collectors.toCollection(ArrayList::new));
                    userList.stream().forEach(u -> database.getUserbyName(u).removeFollower(user.getName()));
                    succesfull = userList.size();
                    if (succesfull > 0) {
                        user.unfollowUsers(userList);
                        return new AckMessage(opCode);
                    } else if (succesfull == 0) {
                        return new ErrorMessage(opCode);
                    }
                } else {
                    return new ErrorMessage(opCode);
                }
            }

        }

        return new AckMessage(opCode);

    }
}