package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.Customer;


public class DeliveryEvent extends BaseEvent<Boolean> {
    private Customer customer;

    public DeliveryEvent(String senderName,Customer customer) {
        super(senderName);
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

//    public void processing(DeliveryVehicle deliveryVehicle){
//        deliveryVehicle.deliver(customer.getAddress(),customer.getDistance());
//    }
}
