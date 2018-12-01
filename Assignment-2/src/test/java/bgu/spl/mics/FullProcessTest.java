package bgu.spl.mics;

import bgu.spl.mics.application.BookStoreRunner;
import bgu.spl.mics.application.Utils;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.OrderSchedule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class FullProcessTest {
    BookStoreRunner bookStoreRunner;
    HashMap<Integer, Customer> customers;
    HashMap<String,ArrayList<String>> optionalsValues;
    List<OrderReceipt> orderReceipts;

    @Before
    public void setUp() {
        bookStoreRunner = new BookStoreRunner();
        orderReceipts.add(new OrderReceipt(8,
                "Selling1",
                234567891,
                "Harry Poter",
                90,
                11,
                3,
                3));
        orderReceipts.add(new OrderReceipt(5,
                "Selling0",
                123456789,
                "Harry Poter",
                192,
                11,
                3,
                3));
        orderReceipts.add(new OrderReceipt(5,
                "Selling2",
                123456781,
                "Trump: The Art of the Deal",
                550,
                11,
                7,
                7));
        orderReceipts.add(new OrderReceipt(5,
                "Selling2",
                123456781,
                "Trump: The Art of the Deal",
                550,
                11,
                7,
                7));
        //customers.put(123456781,new Customer("Haim",123456781,"Building 35, Room 124",2,
                //{},1,67891,null));
    }

    @After
    public void tearDown() { bookStoreRunner = null;}

    //@Test(timeout = 30*1000)
    public void testComplexJson(String[] args) throws IOException, ClassNotFoundException {
        //bookStoreRunner.main(args);
        //customers:
        HashMap<Integer, Customer> out1 = (HashMap<Integer, Customer>) Utils.deserialization(args[1]).readObject();
        out1.values().stream()
                //.sorted(Comparator.comparingInt(Customer::getId))
                .forEach(l -> {
                    try {
                        assertTrue(compareCustomeObjectsFields(l,customers.get(l)));
                    } catch (IllegalAccessException e) {
                        fail();
                    }
                });


    }

    private boolean compareCustomeObjectsFields(Object execpted, Object actual) throws IllegalAccessException {
        //determine fields declared in this class only (no fields of superclass)
        Field[] fieldsExcepted = execpted.getClass().getDeclaredFields();
        Field[] fieldsActual = actual.getClass().getDeclaredFields();
        //print field names paired with their values
        if (fieldsExcepted.length > fieldsActual.length)
            System.out.println("you may have created more fields than needed...");
        for (int i = 0; i < fieldsActual.length; i++) {
            fieldsExcepted[i].setAccessible(true);
            fieldsActual[i].setAccessible(true);
            ArrayList<String> optional = optionalsValues.getOrDefault(fieldsExcepted[i].getName(), null);
            if (fieldsExcepted[i].get(fieldsExcepted[i].get(fieldsExcepted[i].getName())) == null)
                return false;
            if (!(fieldsExcepted[i].get(fieldsExcepted[i].getName()).equals(fieldsActual[i].get(fieldsActual[i].getName()))))
                if (optional != null && !optional.contains(fieldsExcepted[i].get(fieldsExcepted[i].getName())))
                    return false;
        }

        return true;

    }

}
