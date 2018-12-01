package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.util.List;

public class BookOrderEvent extends BaseEvent<List<OrderReceipt>> {

    private Customer customer;
    private int tick;

    public Customer getCustomer() {
        return customer;
    }

    public int getTick() {
        return tick;
    }

    public BookOrderEvent(String senderName, Customer customer, int tick) {
        super(senderName);
        this.customer = customer;
        this.tick = tick;
    }
}
