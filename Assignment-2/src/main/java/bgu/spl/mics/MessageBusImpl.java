package bgu.spl.mics;

import bgu.spl.mics.application.messages.TerminateBroadcast;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	/**
	 * singleton
	 */
	private static class SingletonHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}
	private MessageBusImpl() {
		// initialization code..
	}
	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * private variables
	 */

	private static final Map<MicroService, BlockingQueue<Message>> servicesMessageQueue = new ConcurrentHashMap<>();
	private static final Map<Class<? extends Message>, CopyOnWriteArrayList<MicroService>> messageSubscribes = new ConcurrentHashMap<>();
    private static final Map<Event<?>, Object> eventResults = new ConcurrentHashMap<>();
    private static final AtomicBoolean sendingEvents = new AtomicBoolean(true);


	public void subscribeMessage(Class<? extends Message> type, MicroService m){
		if (!messageSubscribes.containsKey(type)){
			synchronized (messageSubscribes){
				if (!messageSubscribes.containsKey(type)) messageSubscribes.put(type,new CopyOnWriteArrayList<>());
			}
		}
		messageSubscribes.get(type).add(m);
	}

	/**
	 *
	 * @param type The type to subscribe to,
	 * @param m    The subscribing micro-service.
	 * @param <T>
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		subscribeMessage(type,m);
	}

	/**
	 *
	 * @param type 	The type to subscribe to.
	 * @param m    	The subscribing micro-service.
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		subscribeMessage(type,m);
	}


	/**
	 *
	 * @param e      The completed event.
	 * @param result The resolved result of the completed event.
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> void complete(Event<T> e, T result) {
		if (eventResults.containsKey(e))
			if (!((Future<T>) eventResults.get(e)).isDone())
				((Future<T>) eventResults.get(e)).resolve(result);
	}

	private void resolveAllfutures(){
		eventResults.values().stream()
				.filter(f -> ((Future<Object>) f).isDone())
				.forEach(f -> ((Future<Object>) f).resolve(null));
	}

	/**
	 *
	 * @param b 	The message to added to the queues.
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		Objects.requireNonNull(b, "broadcast");
		if (b instanceof TerminateBroadcast) {
			sendingEvents.compareAndSet(true, false);
			//System.out.println("sending events status: " +sendingEvents.get());
			resolveAllfutures();
		}
		messageSubscribes.keySet().stream()
				.filter(type -> type.isAssignableFrom(b.getClass()))
				.flatMap(type -> messageSubscribes.get(type).stream())
				.forEach(sub -> servicesMessageQueue.get(sub).add(b));
	}

	/**
	 *
	 * @param e     	The event to add to the queue.
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Objects.requireNonNull(e, "events");
		Future<T> future = new Future<>();
		eventResults.put(e,future);
		if (!messageSubscribes.containsKey(e.getClass()))
			return null;
		int size = messageSubscribes.get(e.getClass()).size();
		if (size == 0) return null;
		MicroService ms = messageSubscribes.get(e.getClass()).get(RoundRobin.getIndex(e,size).get());
		if (ms == null) return null;
		if (servicesMessageQueue.containsKey(ms))
			servicesMessageQueue.get(ms).add(e);
		return future;
	}


	/**
	 *
	 * @param m the micro-service to create a queue for.
	 */
	@Override
	public void register(MicroService m) {
		Objects.requireNonNull(m,"Micro service");
		if (!servicesMessageQueue.containsKey(m)) {
			synchronized (servicesMessageQueue) {
				if (!servicesMessageQueue.containsKey(m))
					servicesMessageQueue.put(m, new LinkedBlockingQueue<Message>());
			}
		}
	}

	/**
	 *
	 * @param m the micro-service to unregister.
	 */
	@Override
	public void unregister(MicroService m) {
		Objects.requireNonNull(m,"Micro service");
		messageSubscribes.values().stream()
				.filter(f -> f.contains(m))
				.forEach(s -> s.remove(m));
		servicesMessageQueue.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		Message message = servicesMessageQueue.get(m).take();
		return message;
	}

	private static class RoundRobin {

		private static final Map<Class<? extends Message> , AtomicInteger> eventIdxMap = new ConcurrentHashMap<>();

		static AtomicInteger getIndex(Event<?> e, int size) {
			if (!eventIdxMap.containsKey(e.getClass())) {
				eventIdxMap.put(e.getClass(), new AtomicInteger(0));
				return new AtomicInteger(0);
			}
			eventIdxMap.get(e.getClass()).getAndIncrement();
			return new AtomicInteger(eventIdxMap.get(e.getClass()).get() % size);
		}
	}
}