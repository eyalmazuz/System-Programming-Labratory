package bgu.spl.mics.application.passiveObjects;

public class OrderSchedule {
    private String bookTitle;
    private int tick; //ToDo: maybe remove tick
    private boolean fiftyDiscount;

    public OrderSchedule(String bookTitle, int tick, boolean fiftyDiscount) {
        this.bookTitle = bookTitle;
        this.tick = tick;
        this.fiftyDiscount = fiftyDiscount;
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
