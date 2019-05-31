package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.application.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory implements Serializable {

	/**
	 * Retrieves the single instance of this class.
	 */

	private ArrayList<BookInventoryInfo> books;

	private static Inventory ourInstance = null;

	private Inventory(){
		books = new ArrayList<>();
	}

	public static Inventory getInstance() {
		if(ourInstance == null){
			ourInstance = new Inventory();
		}
		return ourInstance;
	}

	/**
	 * Initializes the store inventory. This method adds all the items given to the store
	 * inventory.
	 * <p>
	 * @param inventory 	Data structure containing all data necessary for initialization
	 * 						of the inventory.
	 */
	public void load (BookInventoryInfo[ ] inventory ) {
		for (BookInventoryInfo book : inventory) {
			books.add(book);
		}
	}

	/**
	 * Attempts to take one book from the store.
	 * <p>
	 * @param book 		Name of the book to take from the store
	 * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
	 * 			The first should not change the state of the inventory while the
	 * 			second should reduce by one the number of books of the desired type.
	 */
	public OrderResult take (String book) {
		synchronized (books) {
			for (BookInventoryInfo item : books) {
				if (item.getBookTitle().equals(book) && item.getAmountInInventory() > 0) {
					item.decreaseAmount();
					return OrderResult.SUCCESSFULLY_TAKEN;
				}

			}
			return OrderResult.NOT_IN_STOCK;
		}
	}



	/**
	 * Checks if a certain book is available in the inventory.
	 * <p>
	 * @param book 		Name of the book.
	 * @return the price of the book if it is available, -1 otherwise.
	 */
	public int checkAvailabiltyAndGetPrice(String book) {
		synchronized (books) {
			for (BookInventoryInfo item : books) {
				if (item.getBookTitle().equals(book)) {
					//System.out.println("bookName: " + book + " price: " + item.getPrice());
					return item.getAmountInInventory() != 0 ? item.getPrice() : -1;
				}
			}
			return -1;
		}
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a Map of all the books in the inventory. The keys of the Map (type {@link String})
	 * should be the titles of the books while the values (type {@link Integer}) should be
	 * their respective available amount in the inventory.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printInventoryToFile(String filename){
		Map<String, Integer> serialize = books.stream()
				.distinct()
				.collect(Collectors.toMap(BookInventoryInfo::getBookTitle, BookInventoryInfo::getAmountInInventory,(integer, integer2) -> integer));
        Utils.serialization(filename,serialize);
//		try
//		{
//			FileOutputStream fos = new FileOutputStream(filename);
//			ObjectOutputStream oos = new ObjectOutputStream(fos);
//			oos.writeObject(serialize);
//			oos.close();
//			fos.close();
//		}catch(IOException ioe)
//		{
//			ioe.printStackTrace();
//		}
	}


}