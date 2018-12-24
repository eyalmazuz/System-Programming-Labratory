package bgu.spl.net.impl.networkProtocol;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UsersManager {
    private ReentrantReadWriteLock usersRWLock;
    private ConcurrentHashMap<User, Integer> users;

    public UsersManager() {
        this.usersRWLock = new ReentrantReadWriteLock();
        this.users = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<User,Integer> acquireUsersReadLock(){
        this.usersRWLock.readLock().lock();
        return users;
    }

    public void releaseUsersReadLock() {
        this.usersRWLock.readLock().unlock();
    }

    public ConcurrentHashMap<User, Integer> acquireUsersWriteLock(){
        this.usersRWLock.writeLock().lock();
        return users;
    }

    public void releaseUsersWriteLock() {
        this.usersRWLock.writeLock().unlock();
    }
}
