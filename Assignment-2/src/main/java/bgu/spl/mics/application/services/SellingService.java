package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
	private CountDownLatch countDownLatch;

	public SellingService(String name,CountDownLatch countDownLatch) {
		super(name);
		register = MoneyRegister.getInstance();
		this.countDownLatch = countDownLatch;
	}

	//ToDo: change if isFiftyDiscount is needed
	private int calculateOrderPrice(String bookTitle){
		System.out.println(getName() + ": sending CheckAvailability of " + bookTitle);
		Future<Integer> futureObject = sendEvent(new CheckAvailability(getName(),bookTitle));
		if (futureObject == null)
			return -1;
		Integer resolvedPrice = futureObject.get(100,TimeUnit.MILLISECONDS);
		if (resolvedPrice == null || resolvedPrice == -1)
			return -1;
		System.out.println(getName()+ " book price: " + resolvedPrice);
		return resolvedPrice;
    }

   private void chargingCustomer(int resolvedPrice,BookOrderEvent ev){
	   System.out.println(getName()+": charging credit card in tick " + index.get()+ " in service " + getName());
	   register.chargeCreditCard(ev.getCustomer(), resolvedPrice);
	   OrderSchedule orderSchedule = ev.getCustomer().getOrderSchedules().stream()
			   .filter(b -> b.getBookTitle().equals(ev.getBookTitle()))
			   .findFirst()
			   .get();
	   orderSchedule.setFixedPrice(resolvedPrice);
	   OrderReceipt orderReceipt = new OrderReceipt(
			   orderSchedule.getOrderId(), getName(), ev.getCustomer().getId(), orderSchedule.getBookTitle(),
			   orderSchedule.getFixedPrice(), index.get(), ev.getTick(), orderSchedule.getTick());
	   register.file(orderReceipt);
	   ev.getCustomer().getCustomerReceiptList().add(orderReceipt);
	   System.out.println(getName()+ ": completed order book " + ev.getBookTitle());
	   complete(ev, true);
   }

   	private void deliverBook(BookOrderEvent ev, int resolvedPrice){
		if (ev.getCustomer().getAvailableCreditAmount() >= resolvedPrice){
			synchronized (ev.getCustomer()) {
				System.out.println(getName() + " sending TakingBookEvent " + ev.getBookTitle());
				Future<Boolean> take = sendEvent(new TakingBookEvent(getName(), ev.getBookTitle()));
				if (take != null && take.get(100, TimeUnit.MILLISECONDS)) {
					System.out.println(getName() + " sending DeliveryEvent " + ev.getBookTitle());
					chargingCustomer(resolvedPrice, ev);
					sendEvent(new DeliveryEvent(getName(), ev.getCustomer()));
				} else {
					System.err.println(getName() + ": failed to take book from inv");
					complete(ev, false);
				}
			}
		}else{
			System.err.println(getName()+": customer " + ev.getCustomer().getName() + " does not have enough money for the book " + ev.getBookTitle());
			complete(ev,false);
		}
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, br->{
			System.out.println("terminating: " + getName());
			terminate();
		});
		subscribeEvent(BookOrderEvent.class, ev->{
			System.out.println(getName()+": receiving book order event from " + ev.getSenderName());
			int resolvedPrice = calculateOrderPrice(ev.getBookTitle());
				if (resolvedPrice != -1){
					deliverBook(ev,resolvedPrice);
				}else{
					System.err.println(getName()+": customer: " + ev.getCustomer().getName() + " cannot buy the book " +ev.getBookTitle()+ " because the amount is 0 or service is off");
					complete(ev,false);
				}
		});
		subscribeBroadcast(TickBroadcast.class, br -> index.set(br.getCurrentTick()));
		countDownLatch.countDown();
	}
}
