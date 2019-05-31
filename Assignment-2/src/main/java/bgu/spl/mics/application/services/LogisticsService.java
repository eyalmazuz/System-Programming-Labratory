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

import java.util.concurrent.CountDownLatch;

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
	private CountDownLatch countDownLatch;
	public LogisticsService(String name, CountDownLatch countDownLatch) {
		super(name);
		this.countDownLatch = countDownLatch;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, br->{
			//System.out.println("terminating: " + getName());
			terminate();
			//Thread.currentThread().interrupt();
		});
		subscribeEvent(DeliveryEvent.class, ev ->{
			//System.out.println(getName()+": receiving Delivery event from " + ev.getSenderName());
			Future<DeliveryVehicle> futureObject = sendEvent(new RequestVehicleEvent(getName()));
			if (futureObject != null) {
				DeliveryVehicle deliveryVehicle = futureObject.get();
				if (deliveryVehicle != null) {
					sendVehicle(ev, deliveryVehicle);
				}else{
					//System.out.println(getName() + ": no connection with resource service");
					complete(ev,false);
				}
			}else{
                //System.err.println(getName() + ": failed to RequestVehicleEvent");
                complete(ev,null);
            }
		});
		countDownLatch.countDown();
	}

	private void sendVehicle(DeliveryEvent ev, DeliveryVehicle deliveryVehicle) {
		try{
			//System.out.println(getName() + " starting to deliver book "+ ev.getCustomer().getName() + " to customer " + ev.getCustomer().getName() + " vehicle " + deliveryVehicle.getLicense());
			deliveryVehicle.deliver(ev.getCustomer().getAddress(),ev.getCustomer().getDistance());
			//System.out.println(getName() + " finished to deliver book to customer " + ev.getCustomer().getName());
			//System.out.println(getName() + " sending ReturnVehicleEvent");
			Future<Boolean> booleanFuture = sendEvent(new ReturnVehicleEvent(getName(),deliveryVehicle));
			if (booleanFuture == null){
				//System.err.println(getName()+" failed to ReturnVehicleEvent");
			}
			complete(ev,true);
		}catch (Exception e){
			//System.err.println(getName() + " failed to deliver book to customer " + ev.getCustomer().getName());
			complete(ev,false);
		}
	}


}
