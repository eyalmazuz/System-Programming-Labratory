package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class playground {


//    private static void test1() throws InterruptedException {
//        MessageBus mb;
//        Map<String, MicroService> services = new HashMap<>();
//        final int api_count = 50;
//
//        mb =  MessageBusImpl.getInstance();
//        services.put("inv",new InventoryService("InventoryService"));
//        services.put("logistics", new LogisticsService("LogisticsService"));
//        services.put("resource", new ResourceService("ResourceService"));
//        services.put("selling", new SellingService("SellingService"));
//        services.put("time", new TimeService(10,1000));
//        for (int i = 1; i <= api_count ; i++) {
//            ArrayList<OrderSchedule> order = new ArrayList<>();
//            order.add(new OrderSchedule(i,"harryPotter1",5,true));
//            Customer c = new Customer("lebron"+i,
//                    i,"ben-gurion"+i,
//                    1000,
//                    new ArrayList<OrderReceipt>(),
//                    1000,
//                    i,
//                    order);
//
//            services.put("api"+i,new APIService("APIService",c));
//        }
//
//        Inventory.getInstance().load(new BookInventoryInfo[]{
//                new BookInventoryInfo("harryPotter1",40,60),
//                new BookInventoryInfo("harryPotter2",5,50),
//                new BookInventoryInfo("harryPotter3",5,50),
//                new BookInventoryInfo("harryPotter4",5,50),
//                new BookInventoryInfo("harryPotter5",0,50),
//                new BookInventoryInfo("harryPotter6",5,50),
//                new BookInventoryInfo("harryPotter7",5,50),
//                new BookInventoryInfo("harryPotter8",5,50),
//                new BookInventoryInfo("harryPotter9",5,50),
//                new BookInventoryInfo("harryPotter10",5,50),
//        });
//
//        ResourcesHolder.getInstance().load(new DeliveryVehicle[]{
//                new DeliveryVehicle(100,500),
//                new DeliveryVehicle(101,500),
//                new DeliveryVehicle(102,500)
//        });
//
//        ExecutorService executor = Executors.newFixedThreadPool(services.size());
//        for (String key : services.keySet())
//            executor.execute(services.get(key));
//
//        executor.shutdown();
//        while (!executor.isTerminated()){
//        }
////        Thread t = new Thread(services.get("time"));
////        t.start();
////        Thread.sleep(10*1000);
//       // System.out.println("thread is still running: " + t.isAlive());
//        //shutdownAndAwaitTermination(executor);
//        System.out.println("!!!!!!!!!!!!!!!!!");
//
//    }

    static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    private static void testBarny() throws InterruptedException {
        System.out.println("Threads are legend... ");
        Thread barney1 = new Thread(() -> {
            for (int i=0; i<10; i++) {
                System.out.println("A: wait for it...");
            }
        });
        Thread barney2 = new Thread(() -> {
            for (int i=0; i<10; i++) {
                System.out.println("B: wait for it...");
            }
        });

        barney1.start();
        barney2.start();
        barney1.join();
        barney2.join();

        System.out.println("ary!");
    }
    
    private static class MyService extends MicroService{

        Customer customer;

        public MyService(String name,Customer customer) {
            super(name);
            this.customer = customer;
        }

        @Override
        protected void initialize() {
            System.out.println("sending event to logistics");
            sendEvent(new DeliveryEvent(getName(),customer));
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        readOutput(args);
    }

    private static void readOutput(String[] args) throws IOException {
        try {
            printOutputHashMap(Utils.deserialization(args[1])); //customers
            System.out.println("-----------------------------------------------------------------------");
            printBooks(Utils.deserialization(args[2])); //books
            System.out.println("-----------------------------------------------------------------------");
            printOutputLinkList(Utils.deserialization(args[3])); //receipt
            System.out.println("-----------------------------------------------------------------------");
            printOutputMoneyRegister(Utils.deserialization(args[4])); //money register
            System.out.println("-----------------------------------------------------------------------");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
        }
    }

    private static void printBooks(ObjectInputStream ooi) throws IOException, ClassNotFoundException {
        HashMap<String,Integer> out = (HashMap<String, Integer>) ooi.readObject();
        out.keySet().stream().forEach(l -> {
            System.out.println("bookTitle: " +l+" amount: " + out.get(l));
        });
    }

    private static void printOutputMoneyRegister(ObjectInputStream ooi) throws IOException, ClassNotFoundException {
        MoneyRegister out = (MoneyRegister) ooi.readObject();
        System.out.println("total earnings: " + out.getTotalEarnings());
    }

    private static void printOutputLinkList(ObjectInputStream ooi) throws IOException, ClassNotFoundException {
        LinkedList<Object> out = (LinkedList<Object>) ooi.readObject();
        out.stream().forEach(l -> System.out.println(toString(l)));
    }

    private static void printOutputHashMap(ObjectInputStream ooi) throws IOException, ClassNotFoundException {
        HashMap<Object,Object> out = (HashMap<Object, Object>) ooi.readObject();
        out.values().stream().forEach(l -> {
            System.out.println(toString(l));
        });
    }

    public static String toString(Object c) {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append( c.getClass().getSimpleName() );
        result.append( " {" );
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = c.getClass().getDeclaredFields();

        //print field names paired with their values
        for ( Field field : fields  ) {
            result.append("  ");
            try {
                field.setAccessible(true);
                result.append( field.getName() );
                result.append(": ");
                //requires access to private field:
                result.append( field.get(c) );
            } catch ( IllegalAccessException ex ) {
                //System.out.println(ex);
                System.err.println(ex.getMessage());
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }



}
