package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.RequestVehicleEvent;
import bgu.spl.mics.application.messages.ReturnVehicleEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
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

	public LogisticsService(String name) {
		super(name);
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, br->{
			Thread.currentThread().interrupt();
		});
		subscribeEvent(DeliveryEvent.class, ev ->{
			System.out.println(getName()+": receiving Delivery event from " + ev.getSenderName());
			Future<DeliveryVehicle> futureObject = (Future<DeliveryVehicle>) sendEvent(new RequestVehicleEvent(getName()));
			if (futureObject != null) {
				DeliveryVehicle deliveryVehicle = futureObject.get();
				if (deliveryVehicle != null) {
					System.out.println(getName() + " starting to deliver books to customer " + ev.getCustomer().getName());
					try{
						deliveryVehicle.deliver(ev.getCustomer().getAddress(),ev.getCustomer().getDistance());
						complete(ev,true);
						System.out.println(getName() + " finished to deliver books to customer " + ev.getCustomer().getName());
					}catch (Exception e){
						System.err.println(getName() + " failed to deliver books to customer " + ev.getCustomer().getName());
						complete(ev,false);
					}
                    Future<Boolean>booleanFuture = sendEvent(new ReturnVehicleEvent(getName(),deliveryVehicle));
                    if (booleanFuture != null){
						System.out.println(getName() + " ReturnVehicleEvent");
                        booleanFuture.get();
                    }
				}else{
					complete(ev,false);
				}
			}else{
                System.err.println("why futureObject is null ?");
                complete(ev,null);
            }
		});

	}


}
