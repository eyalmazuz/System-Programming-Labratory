package bgu.spl.mics.application.passiveObjects;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing the store finance management.
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister {


	private static MoneyRegister ourInstance = null;
	private List<OrderReceipt> receipts;

	private int earnings;
	/**
	 * Retrieves the single instance of this class.
	 */

	private MoneyRegister() {
		earnings = 0;
		receipts = new LinkedList<>();
	}

	public static MoneyRegister getInstance() {
		if(ourInstance == null){
			ourInstance = new MoneyRegister();
		}
		return ourInstance;
	}

	/**
	 * Saves an order receipt in the money register.
	 * <p>
	 * @param r		The receipt to save in the money register.
	 */
	public void file (OrderReceipt r) {
		//TODO: Implement this.
	}

	/**
	 * Retrieves the current total earnings of the store.
	 */
	public int getTotalEarnings() {
		return earnings;
	}

	/**
	 * Charges the credit card of the customer a certain amount of money.
	 * <p>
	 * @param amount 	amount to charge
	 */
	public void chargeCreditCard(Customer c, int amount) {
		c.chargeCustomer(amount);
	}

	/**
	 * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts
	 * currently in the MoneyRegister
	 * This method is called by the main method in order to generate the output..
	 */
	public void printOrderReceipts(String filename) {
		try
		{
			FileOutputStream fos =
					new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(receipts);
			oos.close();
			fos.close();
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}