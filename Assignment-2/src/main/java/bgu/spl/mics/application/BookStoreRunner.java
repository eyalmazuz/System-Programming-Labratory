package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {

    static int orderId = 1;
    //static int customerId = 1;

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
        ArrayList<MicroService> threads = getThreadList(obj.getAsJsonObject("services"),customers);
        ExecutorService executor = Executors.newFixedThreadPool(threads.size());
        for (MicroService m : threads)
            executor.execute(m);

        executor.shutdown();
        while (!executor.isTerminated()){
        }

        System.out.println("!!!!!!!!!!!!!!!!!");


        //Customers.getInstance().printCustomersToFile(args[1]);
        Inventory.getInstance().printInventoryToFile(args[2]);
        MoneyRegister.getInstance().printOrderReceipts(args[3]);
        try
        {
            FileOutputStream fos4 = new FileOutputStream(args[4]);
            FileOutputStream fos2 = new FileOutputStream(args[1]);
            ObjectOutputStream oosMoneyRegister = new ObjectOutputStream(fos4);
            ObjectOutputStream oosCustomer = new ObjectOutputStream(fos2);

            oosMoneyRegister.writeObject(MoneyRegister.getInstance());
            oosCustomer.writeObject(customers);

            oosCustomer.close();
            oosMoneyRegister.close();
            fos2.close();
            fos4.close();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }

     private static ArrayList<MicroService> getThreadList(JsonObject obj,HashMap<Integer, Customer> customers){
        ArrayList<MicroService> threadsList = new ArrayList<>();
         for (Customer c: customers.values()) { threadsList.add(new APIService("Api_"+c.getName(),c));}
         for (int i = 0; i < obj.get("selling").getAsInt(); i++) { threadsList.add(new SellingService("Selling"+i)); }
         for (int i = 0; i < obj.get("inventoryService").getAsInt(); i++) { threadsList.add(new InventoryService("inv"+i)); }
         for (int i = 0; i < obj.get("logistics").getAsInt(); i++) { threadsList.add(new LogisticsService("LogisticsService"+i)); }
         for (int i = 0; i < obj.get("resourcesService").getAsInt(); i++) { threadsList.add(new ResourceService("ResourceService"+i)); }
         threadsList.add(new TimeService(obj.get("time").getAsJsonObject().get("duration").getAsInt(),
                 obj.get("time").getAsJsonObject().get("speed").getAsInt()));
         return threadsList;

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
        ArrayList<OrderSchedule> orderSchedules = new ArrayList<>();
        JsonArray order = customer.getAsJsonObject().get("orderSchedule").getAsJsonArray();
        for (JsonElement o:order) {
            orderSchedules.add(new OrderSchedule(orderId,
                    o.getAsJsonObject().get("bookTitle").getAsString(),
                    o.getAsJsonObject().get("tick").getAsInt(),false));
            orderId++;
        }

        Customer c = new Customer(name, id, address, distance, new LinkedList<OrderReceipt>(), creditAmount, creditCardNumber,orderSchedules);

        return  c;
    }
}