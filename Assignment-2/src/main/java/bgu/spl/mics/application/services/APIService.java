package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.FiftyPercentDiscountBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService{

	private Customer customer;
	private Deque<Integer> orderSchedulesStack;
	//private int maxTick;

	public APIService(String name, Customer customer) {
		super(name);
		this.customer = customer;
		this.orderSchedulesStack = customer.getOrderSchedules().stream()
				.map(l->l.getTick())
				.sorted()
				.collect(Collectors.toCollection(ArrayDeque::new));
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, br->{
			System.out.println("terminating: " + getName());
			terminate();
			//Thread.currentThread().interrupt();
		});
		subscribeBroadcast(TickBroadcast.class, br -> {
			if (orderSchedulesStack != null && !orderSchedulesStack.isEmpty() && br.getCurrentTick() == orderSchedulesStack.peek()) {
				System.out.println(getName()+": receiving broadcast from " + br.getSenderName());
				System.out.println(getName()+": sending book order event ");
				customer.getOrderSchedules().stream()
						.filter(l -> l.getTick() == br.getCurrentTick())
						.forEach(b -> sendEvent(new BookOrderEvent(getName(),customer,orderSchedulesStack.poll(),b.getBookTitle())));
			}
		});
		subscribeBroadcast(FiftyPercentDiscountBroadcast.class, br ->{
			customer.getOrderSchedules().stream()
					.filter(l->l.getBookTitle().equals(br.getBookName()))
					.findFirst()
					.get()
					.setFiftyDiscount(true);
		});
	}
}
