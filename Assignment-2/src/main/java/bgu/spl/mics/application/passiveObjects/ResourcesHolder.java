package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

import java.util.ArrayList;

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

	private ArrayList<DeliveryVehicle> deliveryVehicles;


	/**
	 * Retrieves the single instance of this class.
	 */

	private ResourcesHolder() { deliveryVehicles = new ArrayList<>(); }

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
		//TODO: Implement this
		return null;
	}

	/**
	 * Releases a specified vehicle, opening it again for the possibility of
	 * acquisition.
	 * <p>
	 * @param vehicle	{@link DeliveryVehicle} to be released.
	 */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		vehicle.release();
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