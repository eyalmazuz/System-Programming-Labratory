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

	private int calculateOrderPrice(List<OrderSchedule> orderSchedulesBooks){
	    int sum = 0;
        for (OrderSchedule orderSchedule: orderSchedulesBooks) {
            Future<Integer> futureObject = sendEvent(new CheckAvailability(getName(),orderSchedule.getBookTitle()));
            if (futureObject == null) return -1;
			System.out.println(getName() + ": sending CheckAvailability of " + orderSchedule.getBookTitle());
            Integer resolvedPrice = futureObject.get(100,TimeUnit.MILLISECONDS);
            if (!(resolvedPrice != null && resolvedPrice != -1)) return -1;
            int val = orderSchedule.isFiftyDiscount() ? resolvedPrice / 2 : resolvedPrice;
            sum += val;
            orderSchedule.setFixedPrice(val);
        }

	    return sum;
    }
	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, br->{
			Thread.currentThread().interrupt();
		});
		subscribeEvent(BookOrderEvent.class, ev->{
			int orderTick = index.get();
			System.out.println(getName()+": receiving book order event from " + ev.getSenderName());
            List<OrderSchedule> orderSchedules = ev.getCustomer().getOrderSchedules().stream()
                    .filter(l -> l.getTick() == ev.getTick()).collect(Collectors.toList());
            List<String> books = ev.getCustomer().getOrderSchedules().stream()
                    .filter(l -> l.getTick() == ev.getTick()).map(OrderSchedule::getBookTitle).collect(Collectors.toList());
			int resolvedPrice = calculateOrderPrice(orderSchedules);
				if (resolvedPrice != -1){
					//System.out.println("resolved price: " + resolvedPrice + " and customer money : " + ev.getCustomer().getAvailableCreditAmount());
        			if (ev.getCustomer().getAvailableCreditAmount() >= resolvedPrice){
						Future<Boolean> take = sendEvent(new TakingBookEvent(getName(),books));
						if (take != null && take.get()) {
							Future<Boolean> res = sendEvent(new DeliveryEvent(getName(), ev.getCustomer()));
							if (res != null && res.get()) {
								System.out.println(getName()+": charging credit card in tick " + index.get()+ " in service " + getName());
								register.chargeCreditCard(ev.getCustomer(), resolvedPrice);
								List<OrderReceipt> orderReceipts = new ArrayList<>();
                                for (OrderSchedule orderSchedule: orderSchedules) {
                                    OrderReceipt orderReceipt = new OrderReceipt(
                                            orderSchedule.getOrderId(), getName(), ev.getCustomer().getId(), orderSchedule.getBookTitle(),
                                            orderSchedule.getFixedPrice(), index.get(), orderTick, orderSchedule.getTick());
                                    register.file(orderReceipt);
                                    orderReceipts.add(orderReceipt);
                                }
								complete(ev, orderReceipts);
							} else {
								System.err.println(getName()+": failed to deliver book");
								complete(ev, null);
							}
						}else{
							System.err.println(getName()+": failed to take book from inv");
							complete(ev, null);
						}
        			}else{
        				System.err.println(getName()+": customer " + ev.getCustomer().getName() + " does not have enough money for books");
        				complete(ev,null);
					}
				}else{
					System.err.println(getName()+": customer: " + ev.getCustomer().getName() + " cannot buy the books because is too expensive or the amount is 0 or service is off");
					complete(ev,null);
				}
		});
		subscribeBroadcast(TickBroadcast.class, br ->{
			index.set(br.getCurrentTick());
		});

	}
}
