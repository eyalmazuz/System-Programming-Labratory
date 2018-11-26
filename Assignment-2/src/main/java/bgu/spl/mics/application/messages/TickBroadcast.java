package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int currentTick;
    private String senderName;

    public TickBroadcast(String senderName,int currentTick) {
        this.currentTick = currentTick;
        this.senderName=senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public int getCurrentTick() {
        return currentTick;
    }


}
