package bgu.spl.mics.application.passiveObjects;

/**
 * Enum representing result of trying to fetch a book from the inventory.
 */
public enum OrderResult{
    NOT_IN_STOCK(1), SUCCESSFULLY_TAKEN(2);

    public final int value;

    OrderResult(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OrderResult convert(int n) {
        for(OrderResult o : values())
            if(o.getValue() == n)
                return o;
        return null;
    }
}
