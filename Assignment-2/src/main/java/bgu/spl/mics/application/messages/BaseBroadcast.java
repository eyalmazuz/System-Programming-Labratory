package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * A broadcast class extending {@link Broadcast}. use for define the name of the micro-service
 * that send the event to service that will handle the event
 */
public class BaseBroadcast implements Broadcast {
    private String senderName; //for debugging

    protected BaseBroadcast(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
