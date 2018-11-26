package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

public class BookOrderEvent extends BaseEvent<OrderReceipt> {

    private Customer customer;
    private String bookName;

    public BookOrderEvent(String senderName,Customer customer, String bookName) {
        super(senderName);
        this.customer = customer;
        this.bookName = bookName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getBookName() {
        return bookName;
    }

    public void processing(OrderReceipt orderReceipt, boolean fiftyDiscount){
        int price = fiftyDiscount ? orderReceipt.getPrice()/2 : orderReceipt.getPrice();
        if (getCustomer().getAvailableCreditAmount() <= price){
            MoneyRegister.getInstance().chargeCreditCard(getCustomer(),price);
        }
    }
}
