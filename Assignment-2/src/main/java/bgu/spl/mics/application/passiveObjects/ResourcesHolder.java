package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {

	private static ResourcesHolder ourInstance = null;

	private ConcurrentLinkedQueue<DeliveryVehicle> deliveryVehicles;

	/**
	 * Retrieves the single instance of this class.
	 */

	private ResourcesHolder() { deliveryVehicles = new ConcurrentLinkedQueue<>(); }

	public static ResourcesHolder getInstance() {
		if(ourInstance == null){
			ourInstance = new ResourcesHolder();
		}
		return ourInstance;
	}

	/**
	 * Tries to acquire a vehicle and gives a future object which will
	 * resolve to a vehicle.
	 * <p>
	 * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a
	 * 			{@link DeliveryVehicle} when completed.
	 */
	public Future<DeliveryVehicle> acquireVehicle() {
        Future<DeliveryVehicle> vehicleFuture = new Future<DeliveryVehicle>();
		if (!deliveryVehicles.isEmpty())
			vehicleFuture.resolve(deliveryVehicles.poll());
		return vehicleFuture;
	}

	/**
	 * Releases a specified vehicle, opening it again for the possibility of
	 * acquisition.
	 * <p>
	 * @param vehicle	{@link DeliveryVehicle} to be released.
	 */
	public void releaseVehicle(DeliveryVehicle vehicle) {
        deliveryVehicles.add(vehicle);
	}

	/**
	 * Receives a collection of vehicles and stores them.
	 * <p>
	 * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
	 */
	public void load(DeliveryVehicle[] vehicles) {
		for (DeliveryVehicle vehicle : vehicles) {
			deliveryVehicles.add(vehicle);
		}
	}
}