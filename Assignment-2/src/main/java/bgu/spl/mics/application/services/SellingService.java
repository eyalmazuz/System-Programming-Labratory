package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
public class SellingService extends MicroService {

	private MoneyRegister register;
	private static final AtomicInteger index = new AtomicInteger(0);

	public SellingService(String name) {
		super(name);
		register = MoneyRegister.getInstance();
		//index = new AtomicInteger(0);
	}

	//ToDo: change if isFiftyDiscount is needed
	private int calculateOrderPrice(String bookTitle){
		System.out.println(getName() + ": sending CheckAvailability of " + bookTitle);
		Future<Integer> futureObject = sendEvent(new CheckAvailability(getName(),bookTitle));
		if (futureObject == null)
			return -1;
		Integer resolvedPrice = futureObject.get();
		if (resolvedPrice == null || resolvedPrice == -1)
			return -1;
		System.out.println(getName()+ " book price: " + resolvedPrice);
		return resolvedPrice;
    }
	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, br->{
			System.out.println("terminating: " + getName());
			terminate();
			//Thread.currentThread().interrupt();
		});
		subscribeEvent(BookOrderEvent.class, ev->{
			int orderTick = index.get();
			System.out.println(getName()+": receiving book order event from " + ev.getSenderName());
			int resolvedPrice = calculateOrderPrice(ev.getBookTitle());
				if (resolvedPrice != -1){
        			if (ev.getCustomer().getAvailableCreditAmount() >= resolvedPrice){
						System.out.println(getName() + " sending TakingBookEvent " + ev.getBookTitle());
						Future<Boolean> take = sendEvent(new TakingBookEvent(getName(),ev.getBookTitle()));
						if (take != null && take.get()) {
							System.out.println(getName() + " sending DeliveryEvent " + ev.getBookTitle());
							Future<Boolean> res = sendEvent(new DeliveryEvent(getName(), ev.getCustomer()));
							if (res != null && res.get()) {
								System.out.println(getName()+": charging credit card in tick " + index.get()+ " in service " + getName());
								register.chargeCreditCard(ev.getCustomer(), resolvedPrice);
								OrderSchedule orderSchedule = ev.getCustomer().getOrderSchedules().stream()
										.filter(b -> b.getBookTitle().equals(ev.getBookTitle()))
										.findFirst()
										.get();
								OrderReceipt orderReceipt = new OrderReceipt(
										orderSchedule.getOrderId(), getName(), ev.getCustomer().getId(), orderSchedule.getBookTitle(),
										orderSchedule.getFixedPrice(), index.get(), orderTick, orderSchedule.getTick());
								register.file(orderReceipt);
								ev.getCustomer().getCustomerReceiptList().add(orderReceipt);
								System.out.println(getName()+ " completed order book " + ev.getBookTitle());
								complete(ev, true);
							} else {
								System.err.println(getName()+": failed to deliver book");
								complete(ev, false);
							}
						}else{
							System.err.println(getName()+": failed to take book from inv");
							complete(ev, false);
						}
        			}else{
        				System.err.println(getName()+": customer " + ev.getCustomer().getName() + " does not have enough money for the book " + ev.getBookTitle());
        				complete(ev,false);
					}
				}else{
					System.err.println(getName()+": customer: " + ev.getCustomer().getName() + " cannot buy the book " +ev.getBookTitle()+ " because the amount is 0 or service is off");
					complete(ev,false);
				}
		});
		subscribeBroadcast(TickBroadcast.class, br -> index.set(br.getCurrentTick()));

	}
}
