package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.OrderSchedule;

public class BookOrderEvent extends BaseEvent<OrderReceipt> {

    private Customer customer;
    private OrderSchedule orderSchedule;

    public Customer getCustomer() {
        return customer;
    }

    public OrderSchedule getOrderSchedule() {
        return orderSchedule;
    }

    public BookOrderEvent(String senderName, Customer customer, OrderSchedule orderSchedule) {
        super(senderName);
        this.customer = customer;
        this.orderSchedule = orderSchedule;
    }

//    public int getChargeAmount(OrderReceipt orderReceipt, boolean fiftyDiscount){
//        int price = fiftyDiscount ? orderReceipt.getPrice()/2 : orderReceipt.getPrice();
//        if (getCustomer().getAvailableCreditAmount() <= price){
//            MoneyRegister.getInstance().chargeCreditCard(getCustomer(),price);
//        }
//    }
}
