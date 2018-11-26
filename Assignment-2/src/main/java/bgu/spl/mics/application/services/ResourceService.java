package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.RequestVehicleEvent;
import bgu.spl.mics.application.messages.RestoreVehicleEvent;
import bgu.spl.mics.application.messages.ReturnVehicleEvent;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourcesHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService{

	private ResourcesHolder resourcesHolder;

	public ResourceService() {
		super("ResourceService");
		resourcesHolder = ResourcesHolder.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(RequestVehicleEvent.class, ev -> {
			System.out.println(getName()+": receiving RequestVehicleEvent event from" + ev.getSenderName());
			Future<DeliveryVehicle> deliveryVehicleFuture = resourcesHolder.acquireVehicle();
			//waiting for the first vehicle that finished order (in case there is not free vehicle)
			while (!deliveryVehicleFuture.isDone()){
                Future<DeliveryVehicle> restoreDeliveryVehicle = sendEvent(new RestoreVehicleEvent(getName()));
                if (restoreDeliveryVehicle != null && restoreDeliveryVehicle.isDone())
                    deliveryVehicleFuture.resolve(restoreDeliveryVehicle.get());
            }
			DeliveryVehicle deliveryVehicle = deliveryVehicleFuture.get();
			complete(ev,deliveryVehicle);
		});
		subscribeEvent(ReturnVehicleEvent.class, ev->{
            System.out.println(getName()+": receiving ReturnVehicleEvent event from" + ev.getSenderName());
            resourcesHolder.releaseVehicle(ev.getDeliveryVehicle());
        });

	}

}
