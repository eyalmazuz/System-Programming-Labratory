package bgu.spl.mics.application.passiveObjects;

public class OrderSchedule {
    private int orderId;
    private String bookTitle;
    private int tick;
    private boolean fiftyDiscount;

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
}
