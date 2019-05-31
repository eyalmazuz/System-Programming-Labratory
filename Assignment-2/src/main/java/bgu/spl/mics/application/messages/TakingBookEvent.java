package bgu.spl.mics.application.messages;

public class TakingBookEvent extends BaseEvent<Boolean> {
    private String bookTitle;

    public TakingBookEvent(String senderName, String bookTitle) {
        super(senderName);
        this.bookTitle = bookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
