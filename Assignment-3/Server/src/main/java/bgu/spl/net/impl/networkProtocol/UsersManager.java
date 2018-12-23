package bgu.spl.net.impl.networkProtocol;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UsersManager {
    private ReentrantReadWriteLock usersRWLock;
    private ConcurrentLinkedQueue<User> users;

    public UsersManager() {
        this.usersRWLock = new ReentrantReadWriteLock();
        this.users = new ConcurrentLinkedQueue<>();
    }

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
