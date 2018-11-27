package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class RequestVehicleEvent extends BaseEvent<DeliveryVehicle> {

    public RequestVehicleEvent(String senderName) {
        super(senderName);
    }
}
