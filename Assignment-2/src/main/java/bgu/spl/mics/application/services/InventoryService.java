package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService{

	private Inventory inv;

	public InventoryService(String name) {
		super(name);
		inv = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(CheckAvailability.class, ev->{
			System.out.println(getName()+": receiving CheckAvailability from " + ev.getSenderName());
			String bookTitle = ev.getBookTitle();
			complete(ev,inv.checkAvailabiltyAndGetPrice(bookTitle)); //if not exist return -1
		});
		subscribeBroadcast(TerminateBroadcast.class, br->{
			terminate();
			System.out.println("terminating " + getName());
		});
		
	}

}
