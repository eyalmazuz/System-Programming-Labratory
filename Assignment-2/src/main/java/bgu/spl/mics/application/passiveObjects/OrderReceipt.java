package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a receipt that should 
 * be sent to a customer after the completion of a BookOrderEvent.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class OrderReceipt {

	private int orderId;
	private String seller;
	private int customerId;
	private String bookTitle;
	private int price;
	private int issuedTick;
	private int orderTick;
	private int processTick;

	public OrderReceipt(int orderId, String seller, int customerId, String bookTitle, int price, int issuedTick, int orderTick, int processTick) {
		this.orderId = orderId;
		this.seller = seller;
		this.customerId = customerId;
		this.bookTitle = bookTitle;
		this.price = price;
		this.issuedTick = issuedTick;
		this.orderTick = orderTick;
		this.processTick = processTick;
	}

	/**
     * Retrieves the orderId of this receipt.
     */
	public int getOrderId() {
		// TODO Implement this
		return 0;
	}
	
	/**
     * Retrieves the name of the selling service which handled the order.
     */
	public String getSeller() {
		// TODO Implement this
		return null;
	}
	
	/**
     * Retrieves the ID of the customer to which this receipt is issued to.
     * <p>
     * @return the ID of the customer
     */
	public int getCustomerId() {
		// TODO Implement this
		return 0;
	}
	
	/**
     * Retrieves the name of the book which was bought.
     */
	public String getBookTitle() {
		// TODO Implement this
		return null;
	}
	
	/**
     * Retrieves the price the customer paid for the book.
     */
	public int getPrice() {
		// TODO Implement this
		return 0;
	}
	
	/**
     * Retrieves the tick in which this receipt was issued.
     */
	public int getIssuedTick() {
		// TODO Implement this
		return 0;
	}
	
	/**
     * Retrieves the tick in which the customer sent the purchase request.
     */
	public int getOrderTick() {
		// TODO Implement this
		return 0;
	}
	
	/**
     * Retrieves the tick in which the treating selling service started 
     * processing the order.
     */
	public int getProcessTick() {
		// TODO Implement this
		return 0;
	}
}
