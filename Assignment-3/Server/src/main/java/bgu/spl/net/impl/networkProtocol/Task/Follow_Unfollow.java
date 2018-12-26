package bgu.spl.net.impl.networkProtocol.Task;

import bgu.spl.net.impl.networkProtocol.Operation.AckMessage;
import bgu.spl.net.impl.networkProtocol.Operation.ErrorMessage;
import bgu.spl.net.impl.networkProtocol.User;
import bgu.spl.net.impl.networkProtocol.Database;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Follow_Unfollow extends BaseTask <String> {

    private int sign;
    private ConcurrentLinkedQueue<String> userNameList;
    private int succesfull;

    public Follow_Unfollow(Database database, int connectionId, int opCode, int sign, int numOfUsers, ArrayList<String> userNameList) {
        super(database, connectionId, opCode);
        this.sign = sign;
        this.userNameList = new ConcurrentLinkedQueue<>();
        this.userNameList.addAll(userNameList);
        succesfull = 0;
    }

    @Override
    public String run() {
        if (sign == 0) {
            ConcurrentHashMap<String, Integer> loggedInMap = database.getLoggedInMap();
            if (loggedInMap.containsValue(connectionId)) {
                User user = database.getUserByConnectionID(connectionId);
                if (user != null) {
                    ArrayList<String> userList = userNameList.stream()
                            .filter(u -> database.getUserbyName(u) != null && !database.getUserByConnectionID(connectionId).isFollow(u))
                            .collect(Collectors.toCollection(ArrayList::new));
                    userList.stream().forEach(u -> database.getUserbyName(u).addFollower(user.getName()));
                    succesfull = userList.size();
                    if (succesfull > 0) {
                        user.addFollowers(userList);
                    } else if (succesfull == 0) {
                        return new ErrorMessage(opCode).toString();
                    }
                } else {
                    return new ErrorMessage(opCode).toString();
                }
            }
        } else if (sign == 1) {
            ConcurrentHashMap<String, Integer> loggedInMap = database.getLoggedInMap();
            if (loggedInMap.containsValue(connectionId)) {
                User user = database.getUserByConnectionID(connectionId);
                if (user != null) {
                    ArrayList<String> userList = userNameList.stream()
                            .filter(u -> database.getUserbyName(u) != null && database.getUserByConnectionID(connectionId).isFollow(u))
                            .collect(Collectors.toCollection(ArrayList::new));
                    userList.stream().forEach(u -> database.getUserbyName(u).removeFollower(user.getName()));
                    succesfull = userList.size();
                    if (succesfull > 0) {
                        user.unfollowUsers(userList);
                        return new AckMessage(opCode).toString();
                    } else if (succesfull == 0) {
                        return new ErrorMessage(opCode).toString();
                    }
                } else {
                    return new ErrorMessage(opCode).toString();
                }
            }

        }

        return new AckMessage(opCode).toString();

    }
}