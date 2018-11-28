package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

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

        HashMap<Integer, Customer> customers = getCustomers(obj);
        updateBooks(inv, obj);
        updateVehicles(hold, obj);
        System.out.println("");


        //Customers.getInstance().printCustomersToFile(args[1]);
        Inventory.getInstance().printInventoryToFile(args[2]);
        MoneyRegister.getInstance().printOrderReceipts(args[3]);
        try
        {
            FileOutputStream fos =
                    new FileOutputStream(args[4]);
            FileOutputStream fos2 = new FileOutputStream(args[1]);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);

            oos.writeObject(MoneyRegister.getInstance());
            oos2.writeObject(customers);

            oos2.close();
            oos.close();
            fos2.close();
            fos.close();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

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

    public static HashMap<Integer, Customer> getCustomers(JsonObject obj){
        HashMap<Integer, Customer> customerHashMap = new HashMap<>();
        JsonArray customers = obj.get("services").getAsJsonObject().get("customers").getAsJsonArray();
        for (JsonElement customer : customers){
            Customer c = getCustomerData(customer);
            customerHashMap.put(c.getId(), c);
        }
        return customerHashMap;
    }

    private static Customer getCustomerData(JsonElement customer) {
        int id = customer.getAsJsonObject().get("id").getAsInt();
        String name = customer.getAsJsonObject().get("name").getAsString();
        String address = customer.getAsJsonObject().get("address").getAsString();
        int distance = customer.getAsJsonObject().get("distance").getAsInt();
        int creditCardNumber = customer.getAsJsonObject().get("creditCard").getAsJsonObject().get("number").getAsInt();
        int creditAmount = customer.getAsJsonObject().get("creditCard").getAsJsonObject().get("amount").getAsInt();
        Customer c = new Customer(name, id, address, distance, new LinkedList<OrderReceipt>(), creditAmount, creditCardNumber);

        return  c;
    }
}