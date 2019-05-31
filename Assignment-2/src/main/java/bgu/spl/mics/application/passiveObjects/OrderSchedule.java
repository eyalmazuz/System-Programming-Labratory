package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

public class OrderSchedule implements Serializable {
    private int orderId;
    private String bookTitle;
    private int tick;
    private boolean fiftyDiscount;
    private int fixedPrice;

    public OrderSchedule(int orderId,String bookTitle, int tick, boolean fiftyDiscount) {
        this.orderId = orderId;
        this.bookTitle = bookTitle;
        this.tick = tick;
        this.fiftyDiscount = fiftyDiscount;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getTick() {
        return tick;
    }

    public boolean isFiftyDiscount() {
        return fiftyDiscount;
    }

    public void setFiftyDiscount(boolean fiftyDiscount) {
        this.fiftyDiscount = fiftyDiscount;
    }

    public int getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(int fixedPrice) {
        this.fixedPrice = fixedPrice;
    }
}
