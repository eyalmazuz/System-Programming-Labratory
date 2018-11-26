package bgu.spl.mics.application.passiveObjects;

public class OrderSchedule {
    private String bookTitle;
    private int tick;

    public OrderSchedule(String bookTitle, int tick) {
        this.bookTitle = bookTitle;
        this.tick = tick;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getTick() {
        return tick;
    }
}
