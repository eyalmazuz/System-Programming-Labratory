package bgu.spl.net.impl.networkProtocol;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//ToDo: check if necessary to lock/unblock ConcurrentHashMap<String, Integer> loggedInMap;
public class UsersManager {
    private ReentrantReadWriteLock usersRWLock;
    //private ReentrantReadWriteLock loggedInMapRWLock;
    private ConcurrentLinkedQueue<User> users;
    private ConcurrentHashMap<String, Integer> loggedInMap;


    public UsersManager() {
        this.usersRWLock = new ReentrantReadWriteLock();
        //this.loggedInMapRWLock = new ReentrantReadWriteLock();
        this.users = new ConcurrentLinkedQueue<>();
        this.loggedInMap = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, Integer> getLoggedInMap() {
        return loggedInMap;
    }

    //    public ConcurrentHashMap<String, Integer> acquireLoggedInMapReadLock(){
//        this.loggedInMapRWLock.readLock().lock();
//        return loggedInMap;
//    }
//
//    public void releaseLoggedInMapReadLock() {
//        this.loggedInMapRWLock.readLock().unlock();
//    }
//
//    public ConcurrentHashMap<String, Integer> acquireLoggedInMapWriteLock(){
//        this.loggedInMapRWLock.writeLock().lock();
//        return loggedInMap;
//    }
//
//    public void releaseLoggedInMapWriteLock() {
//        this.loggedInMapRWLock.writeLock().unlock();
//    }

    //------------------------------------------------------------------------------------

    public ConcurrentLinkedQueue<User> acquireUsersReadLock(){
        this.usersRWLock.readLock().lock();
        return users;
    }

    public void releaseUsersReadLock() {
        this.usersRWLock.readLock().unlock();
    }

    public ConcurrentLinkedQueue<User> acquireUsersWriteLock(){
        this.usersRWLock.writeLock().lock();
        return users;
    }

    public void releaseUsersWriteLock() {
        this.usersRWLock.writeLock().unlock();
    }
}
