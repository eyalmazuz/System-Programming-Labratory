package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.Customer;

public class BookOrderEvent extends BaseEvent<Boolean> {

    private Customer customer;
    private int tick;
    private String bookTitle;

    public Customer getCustomer() {
        return customer;
    }

    public int getTick() {
        return tick;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public BookOrderEvent(String senderName, Customer customer, int tick, String bookTitle) {
        super(senderName);
        this.customer = customer;
        this.tick = tick;
        this.bookTitle = bookTitle;
    }
}
