package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class ReturnVehicleEvent extends BaseEvent<Boolean> {

    DeliveryVehicle deliveryVehicle;

    public ReturnVehicleEvent(String senderName, DeliveryVehicle deliveryVehicle) {
        super(senderName);
        this.deliveryVehicle = deliveryVehicle;
    }

    public DeliveryVehicle getDeliveryVehicle() {
        return deliveryVehicle;
    }
}