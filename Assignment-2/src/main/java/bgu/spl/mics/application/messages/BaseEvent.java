package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;


/**
 * An event class extending {@link Event}. use for define the name of the micro-service
 * that send the event to service that will handle the event
 *
 * @param <T>
 */
public abstract class BaseEvent<T> implements Event<T> {
    private String senderName; //for debugging

    protected BaseEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
