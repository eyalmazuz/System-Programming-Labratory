package bgu.spl.mics.application.messages;

public class CheckAvailability extends BaseEvent<Integer> {
    private String bookTitle;


    public CheckAvailability(String senderName, String bookTitle) {
        super(senderName);
        this.bookTitle = bookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
