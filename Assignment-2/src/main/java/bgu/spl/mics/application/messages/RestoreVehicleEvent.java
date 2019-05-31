package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

//waiting for the first vehicle that finished order (in case there is not free vehicle)
public class RestoreVehicleEvent extends BaseEvent<DeliveryVehicle> {

    public RestoreVehicleEvent(String senderName) {
        super(senderName);
    }
}
