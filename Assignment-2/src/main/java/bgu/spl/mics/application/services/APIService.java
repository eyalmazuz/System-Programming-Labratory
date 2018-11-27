package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.FiftyPercentDiscountBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
	private CopyOnWriteArrayList<OrderSchedule> list;
	private final AtomicInteger index;
	private int maxTick;

	public APIService(String name, Customer customer, ArrayList<OrderSchedule> list) {
		super(name +customer.getName());
		this.customer = customer;
		this.list = list.stream()
				.distinct()
				.sorted(Comparator.comparingInt(OrderSchedule::getTick))
				.collect(Collectors.toCollection(CopyOnWriteArrayList::new));
		this.index = new AtomicInteger(0);
		this.maxTick = list.stream().max(Comparator.comparing(OrderSchedule::getTick)).get().getTick();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, br -> {
			if (index.get() >= list.size() && br.getCurrentTick() >  maxTick){
				//terminate();//ToDo: check in forum
			}else if (br.getCurrentTick() == list.get(index.get()).getTick()) {
				System.out.println(getName()+": receiving broadcast from " + br.getSenderName() + " in 'good' tick" + br.getCurrentTick());
				String name = "BookOrderEvent_"+customer.getName()+"_"+list.get(index.get()).getTick();
				System.out.println(getName()+": sending book order event ");
				Future<OrderReceipt> futureObject = sendEvent(new BookOrderEvent(getName(),customer,list.get(index.get())));
				if (futureObject != null) {
					OrderReceipt result = futureObject.get();//ToDo: check in forum
					if (result != null)
						customer.getCustomerReceiptList().add(result);
				}
				index.set(br.getCurrentTick());
			}
		});
		subscribeBroadcast(FiftyPercentDiscountBroadcast.class, br ->{
			list.stream().filter(l -> l.getBookTitle().equals(br.getBookName())).findFirst().get().setFiftyDiscount(true);
		});
		subscribeBroadcast(TerminateBroadcast.class, br->{
			terminate();
			System.out.println("terminating " + getName());
		});

	}

}
