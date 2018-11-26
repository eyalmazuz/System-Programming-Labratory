package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.concurrent.TimeUnit;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService{

	private MoneyRegister register;

	public SellingService() {
		super("SellingService");
		register = MoneyRegister.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(BookOrderEvent.class, ev->{
			String bookTitle = ev.getOrderSchedule().getBookTitle();
			System.out.println(getName()+": receiving book order event from" + ev.getSenderName());
			Future<Integer> futureObject = sendEvent(new CheckAvailability(getName(),bookTitle));
			if (futureObject != null){
				Integer resolvedPrice = futureObject.get(100, TimeUnit.MILLISECONDS);
				if (resolvedPrice != null && resolvedPrice != -1){
					resolvedPrice = ev.getOrderSchedule().isFiftyDiscount() ? resolvedPrice/2 : resolvedPrice;
        			if (ev.getCustomer().getAvailableCreditAmount() <= resolvedPrice){
            			register.chargeCreditCard(ev.getCustomer(),resolvedPrice);
        			}
					//complete(ev,register.printOrderReceipts(););
				}
			}
		});
	}

}
