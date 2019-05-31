package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {

    Inventory inv;
    boolean flag =false;

    @Before
    public void setUp() {
        inv = Inventory.getInstance();

    }

    protected void setUpBooks(){
        BookInventoryInfo[] infos = {new BookInventoryInfo("milhama ve shalom", 8, 10),
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
    public void checkPriceOfLordOfTheRings(){
        int price= 200;
        int priceTest = inv.checkAvailabiltyAndGetPrice("sar hatabaot");
        assertEquals(price, priceTest);
    }

    @Test
    public void checkPriceOfNotAviableBook(){
        int price = -1;
        int priceTEST = inv.checkAvailabiltyAndGetPrice("milhana be shalom");
        assertEquals(price, priceTEST);
    }

    @Test
    public void CheckSerlize(){
        inv.printInventoryToFile("serlize");

    }

    @After
    public void tearDown() throws Exception {
        inv = null;
    }
}