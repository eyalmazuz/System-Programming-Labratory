package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
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

	private static final Map<MicroService, MessageQueue> servicesMessageQueue = new ConcurrentHashMap<>();
	//ToDo: maybe split messageSubscribes to events and brodcast
	private static final Map<Class<? extends Message>, CopyOnWriteArrayList<MicroService>> messageSubscribes = new ConcurrentHashMap<>();
    private static final Map<Event<?>, Object> eventResults = new ConcurrentHashMap<>();

	/**
	 *
	 * @param type The type to subscribe to,
	 * @param m    The subscribing micro-service.
	 * @param <T>
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if (!messageSubscribes.containsKey(type)){
			messageSubscribes.put(type,new CopyOnWriteArrayList<>());
		}
		messageSubscribes.get(type).add(m);
	}

	/**
	 *
	 * @param type 	The type to subscribe to.
	 * @param m    	The subscribing micro-service.
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (!messageSubscribes.containsKey(type)){
			messageSubscribes.put(type,new CopyOnWriteArrayList<>());
		}
		messageSubscribes.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		MicroService ms = RoundRobin.getMicroService(e);
		if (ms != null){
            ((Future<T>)eventResults.get(e)).resolve(result);
		    //e.getFuture().resolve(result);
			servicesMessageQueue.get(ms).onCompleted();
		}
	}

	/**
	 *
	 * @param b 	The message to added to the queues.
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		Objects.requireNonNull(b, "brodcast");
		messageSubscribes.keySet().stream()
				.filter(type -> type.isAssignableFrom(b.getClass()))
				.flatMap(type -> messageSubscribes.get(type).stream())
				.forEach(sub -> servicesMessageQueue.get(sub).addToQueue(b));
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
		//e.addFuture(future);
		if (!messageSubscribes.containsKey(e.getClass())) return null;
		int size = messageSubscribes.get(e.getClass()).size();
		if (size == 0) return null;
		MicroService ms = messageSubscribes.get(e.getClass()).get(RoundRobin.getIndex(e,size).get());
		if (ms == null) return null;
		servicesMessageQueue.get(ms).addToQueue(e);
		return future;
	}

	/**
	 *
	 * @param m the micro-service to create a queue for.
	 */
	@Override
	public void register(MicroService m) {
		Objects.requireNonNull(m,"Micro service");
		if (!servicesMessageQueue.containsKey(m))
			servicesMessageQueue.put(m,new MessageQueue());
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
		//if (servicesMessageQueue.containsKey(m)){
			return servicesMessageQueue.get(m).pollMessage();
		//}
		//return null;
	}

	private static class MessageQueue {

		private static class Data{
			final Queue<Message> queue = new ConcurrentLinkedQueue<>();
			//private AtomicBoolean isActive = new AtomicBoolean(false);
			private AtomicBoolean isCompleted = new AtomicBoolean(true);
		}

		private final Data data;

		MessageQueue() {
			data = new Data();
		}

		MessageQueue(Data data) {
			this.data = data;
		}

		synchronized void addToQueue(Message message){
			//if (!data.isActive.get())
				//data.isActive.compareAndSet(false,true);
			data.queue.add(message);
			this.notifyAll();
		}

		synchronized Message pollMessage(){
			while (data.queue.isEmpty() || !data.isCompleted.get()) {
				try {
					this.wait();
				} catch (InterruptedException e) {

                }
			}
			Message message= data.queue.poll();
			if (!(message instanceof Broadcast))
				data.isCompleted.compareAndSet(true,false);
			this.notifyAll(); //notify service for receiving Message
			return message;
		}

		synchronized void onCompleted(){
			data.isCompleted.compareAndSet(false,true);
			this.notifyAll();
		}

	}

	private static class RoundRobin {

		private static final Map<Event<?> , AtomicInteger> eventIdxMap = new ConcurrentHashMap<>();

		static MicroService getMicroService(Event<?> event){
			if (eventIdxMap.get(event) == null)
				return null;
			return messageSubscribes.get(event.getClass()).//return list of micro-services
					get(eventIdxMap.get(event).get()); //return the relevant services
		}

		static AtomicInteger getIndex(Event<?> e, int size) {
			if (!eventIdxMap.containsKey(e))
				eventIdxMap.put(e,new AtomicInteger(-1));
			return new AtomicInteger(eventIdxMap.get(e).getAndIncrement() % size);
		}


	}


	

}
