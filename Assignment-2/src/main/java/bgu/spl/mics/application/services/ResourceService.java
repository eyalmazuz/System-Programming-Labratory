package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.RequestVehicleEvent;
import bgu.spl.mics.application.messages.ReturnVehicleEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourcesHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService {

	private ResourcesHolder resourcesHolder;
	private static ConcurrentLinkedQueue<Future<DeliveryVehicle>> deliveryVehicleFutureQueue = new ConcurrentLinkedQueue<>();
	private CountDownLatch countDownLatch;

	public ResourceService(String name,CountDownLatch countDownLatch) {
		super(name);
		resourcesHolder = ResourcesHolder.getInstance();
		this.countDownLatch = countDownLatch;
	}

	@Override
	protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, br->{
            System.out.println("terminating: " + getName());
            terminate();
            deliveryVehicleFutureQueue.stream()
					.filter(future -> !future.isDone())
					.forEach(future -> future.resolve(null));
            //Thread.currentThread().interrupt();
        });
		subscribeEvent(RequestVehicleEvent.class, ev -> {
			System.out.println(getName()+": receiving RequestVehicleEvent event from " + ev.getSenderName());
			Future<DeliveryVehicle>	deliveryVehicleFuture = resourcesHolder.acquireVehicle();
			if (!deliveryVehicleFuture.isDone())
				deliveryVehicleFutureQueue.add(deliveryVehicleFuture);
			DeliveryVehicle deliveryVehicle = deliveryVehicleFuture.get();
            System.out.println(getName()+": found a free vehicle " + deliveryVehicle.getLicense() + " and send him to work");
			complete(ev,deliveryVehicle);
		});
		subscribeEvent(ReturnVehicleEvent.class, ev->{
            System.out.println(getName()+": receiving ReturnVehicleEvent event from" + ev.getSenderName());
            if (deliveryVehicleFutureQueue.isEmpty())
                resourcesHolder.releaseVehicle(ev.getDeliveryVehicle());
            else
				deliveryVehicleFutureQueue.poll().resolve(ev.getDeliveryVehicle());
            complete(ev,true);
        });
		countDownLatch.countDown();
	}

}
