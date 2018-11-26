package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;



public abstract class BaseEvent<T> implements Event<T> {
    private String senderName; //for debugging

    protected BaseEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
