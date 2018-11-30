package bgu.spl.mics.application.messages;

import java.util.List;

public class TakingBookEvent extends BaseEvent<Boolean> {
    private List<String> books;

    public List<String> getBooks() {
        return books;
    }

    public TakingBookEvent(String senderName, List<String> books) {
        super(senderName);
        this.books = books;
    }
}
