package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.RequestVehicleEvent;
import bgu.spl.mics.application.messages.ReturnVehicleEvent;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {

	public LogisticsService() {
		super("LogisticsService");
	}

	@Override
	protected void initialize() {
		subscribeEvent(DeliveryEvent.class, ev ->{
			System.out.println(getName()+": receiving Delivery event from " + ev.getSenderName());
			Future<DeliveryVehicle> futureObject = (Future<DeliveryVehicle>) sendEvent(new RequestVehicleEvent(getName()));
			if (futureObject != null) {
				DeliveryVehicle deliveryVehicle = futureObject.get();
				if (deliveryVehicle != null) {
					deliveryVehicle.deliver(ev.getCustomer().getAddress(),ev.getCustomer().getDistance());
                    Future<Boolean>booleanFuture = sendEvent(new ReturnVehicleEvent(getName(),deliveryVehicle));
                    if (booleanFuture != null){
                        Boolean isSent = booleanFuture.get();
                        if (isSent)
                            complete(ev,true);
                    }
				}
			}else{
                System.out.println("why futureObject is null ?");
            }
		});
	}

}
