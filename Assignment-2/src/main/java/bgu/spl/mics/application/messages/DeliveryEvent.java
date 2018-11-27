package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class DeliveryEvent extends BaseEvent<DeliveryVehicle> {
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
