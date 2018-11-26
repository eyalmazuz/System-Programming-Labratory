package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
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
        ResourcesHolder hold = ResourcesHolder.getInstance();
        JsonParser jsonParser = new JsonParser();
        JsonObject obj = null;
        try {
            obj = jsonParser.parse(new FileReader(args[0])).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

        updateBooks(inv, obj);
        updateVehicles(hold, obj);
        System.out.println("");


    }
    //retrieves the delivery vehicles
    private static void updateVehicles(ResourcesHolder hold, JsonObject obj) {
        ArrayList<DeliveryVehicle> deliveryVehicles = new ArrayList<>();
        JsonArray vehicles = obj.get("initialResources").getAsJsonArray().get(0).getAsJsonObject().get("vehicles").getAsJsonArray();
        for(JsonElement vehicle : vehicles){
            DeliveryVehicle v = new DeliveryVehicle(vehicle.getAsJsonObject().get("license").getAsInt(),
                    vehicle.getAsJsonObject().get("speed").getAsInt());
            deliveryVehicles.add(v);
        }
        DeliveryVehicle[] arr = new DeliveryVehicle[deliveryVehicles.size()];
        for(int i=0; i<arr.length; i++){
            arr[i] = deliveryVehicles.get(i);
        }
        hold.load(arr);
    }

    //retrieves the books
    private static void updateBooks(Inventory inv, JsonObject obj) {
        ArrayList<BookInventoryInfo> books = new ArrayList<>();
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
    }
}