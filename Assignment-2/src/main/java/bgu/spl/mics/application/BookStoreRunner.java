package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {

        Inventory inv = Inventory.getInstance();
        ArrayList<BookInventoryInfo> books = new ArrayList<>();
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject obj = jsonParser.parse(new FileReader(args[0])).getAsJsonObject();
            JsonArray inventory = obj.get("initialInventory").getAsJsonArray();
            for(JsonElement book : inventory){
                BookInventoryInfo item = new BookInventoryInfo(book.getAsJsonObject().get("bookTitle").getAsString(),
                        book.getAsJsonObject().get("amount").getAsInt(),
                        book.getAsJsonObject().get("price").getAsInt());
                books.add(item);
            }
            BookInventoryInfo[] arr = new BookInventoryInfo[books.size()];
            for(int i=0; i<arr.length; i++){
                arr[i] = books.get(i);
            }
            inv.load(arr);
            System.out.println("");

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

    }
}
