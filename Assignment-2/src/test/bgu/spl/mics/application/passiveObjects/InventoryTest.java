package bgu.spl.mics.application.passiveObjects;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class InventoryTest {

    Inventory inv;
    boolean flag =false;
    @Before
    public void setUp() throws Exception {
        inv = Inventory.getInstance();
    }

    protected void setUpBooks(){
        BookInventoryInfo[] infos = {new BookInventoryInfo("milhama ve shalom", 4, 10),
                new BookInventoryInfo("sar hatabaot", 2, 200),
                new BookInventoryInfo("Harry Poter", 2, 100),
                new BookInventoryInfo("XD", 2 ,20),
        };
        inv.load(infos);

    }

    @Test
    public void checkBookTakenInInventory(){
        setUpBooks();
        OrderResult o = inv.take("Harry Poter");
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, o);
    }

    @Test
    public void check1BookTakenFail(){
        OrderResult o = inv.take("WWWW");
        assertEquals(OrderResult.NOT_IN_STOCK, o);
    }
    @Test
    public void checkBookTakenXD(){
        OrderResult o = inv.take("XD");
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, o);
    }

    @Test
    public void checkTake2BooksInIntory(){
        setUpBooks();
        OrderResult o1 = inv.take("milhama ve shalom");
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, o1);
        OrderResult o2 = inv.take("sar hatabaot");
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, o2);
    }

    @Test
    public void checkTake2BooksInIntorySameBook(){
        OrderResult o1 = inv.take("milhama ve shalom");
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, o1);
        OrderResult o2 = inv.take("milhama ve shalom");
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, o2);
    }


    @Test
    public void checkTake2BooksInIntorySameBookFail(){
        OrderResult o1 = inv.take("milhama ve shalom");
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, o1);
        OrderResult o2 = inv.take("milhama ve shalom");
        assertEquals(OrderResult.NOT_IN_STOCK, o2);
    }

    @After
    public void tearDown() throws Exception {
        inv = null;
    }
}