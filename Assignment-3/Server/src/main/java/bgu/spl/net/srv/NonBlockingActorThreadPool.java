package bgu.spl.net.srv;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NonBlockingActorThreadPool<T> {

    private ExecutorService pool;
    private final ReadWriteLock actsRWLock = new ReentrantReadWriteLock();
    private Map<T, Actor> acts = new WeakHashMap<>();

    public NonBlockingActorThreadPool(int threads) {
        this.pool = Executors.newFixedThreadPool(threads);
    }

    private Actor getActor(T act) {

        actsRWLock.readLock().lock();
        Actor actor = acts.get(act);
        actsRWLock.readLock().unlock();

        if (actor == null) {
            actsRWLock.writeLock().lock();
            acts.put(act, actor = new Actor());
            actsRWLock.writeLock().unlock();
        }

        return actor;
    }

    public void submit(T act, Runnable r) {
        getActor(act).add(r);
    }

    public void shutdown() {
        pool.shutdownNow();
    }

    private static class ExecutionState {

        //the amount of tasks that should be handled
        public int tasksLeft;
        //true if there is a thread that handle or designated to handle these task
        public boolean playingNow;

        public ExecutionState(int tasksLeft, boolean playingNow) {
            this.tasksLeft = tasksLeft;
            this.playingNow = playingNow;
        }

    }

    private class Actor implements Runnable {

        public ConcurrentLinkedQueue<Runnable> tasksQueue = new ConcurrentLinkedQueue<>();
        public AtomicReference<ExecutionState> state = new AtomicReference<>(new ExecutionState(0, false));

        public void add(Runnable task) {
            tasksQueue.add(task);
            ExecutionState oldState = state.get();
            //after this operation completed,
            //one additional task will be added to the queue and a thread for this tasks will be executed if not already is
            ExecutionState newState = new ExecutionState(oldState.tasksLeft + 1, true);

            while (!state.compareAndSet(oldState, newState)) {
                oldState = state.get();
                newState.tasksLeft = oldState.tasksLeft + 1;
            }

            if (!oldState.playingNow){
                pool.submit(this);
            }
        }

        @Override
        public void run() {
            //when this function completes,  0 tasks will be in the queue and no one will and the actor will not "play now"
            ExecutionState newState = new ExecutionState(0, false);
            int tasksDone = 0;
            ExecutionState oldState;

            do {
                oldState = state.get();
                for (; tasksDone < oldState.tasksLeft; tasksDone++) {
                    tasksQueue.remove().run();
                }

            } while (!state.compareAndSet(oldState, newState));
        }
    }
}