package bgu.spl.mics.application.messages;

public class FiftyPercentDiscountBroadcast extends BaseBroadcast {
    private String bookName;

    public FiftyPercentDiscountBroadcast(String senderName, String bookName) {
        super(senderName);
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }
}
