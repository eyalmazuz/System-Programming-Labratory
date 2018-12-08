package bgu.spl.mics.application;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class BookStoreRunnerTest {
    BookStoreRunner bookStoreRunner;
    String[]args;


    @Before
    public void setUp(){
        bookStoreRunner = new BookStoreRunner();
        args = new String[]{"./files/input_example",
                "./files/output_customers.ser",
                "./files/output_books.ser",
                "./files/output_orderReceipt.ser",
                "./files/output_moneyRegister.ser"};
    }

    @After
    public void tearDown() { bookStoreRunner = null;}


    @Test(timeout = 35*1000)
    public void testMain() throws IOException, ClassNotFoundException {
        bookStoreRunner.main(args);
    }


//    private boolean compareCustomeObjectsFields(Object execpted, Object actual) throws IllegalAccessException {
//        //determine fields declared in this class only (no fields of superclass)
//        Field[] fieldsExcepted = execpted.getClass().getDeclaredFields();
//        Field[] fieldsActual = actual.getClass().getDeclaredFields();
//        //print field names paired with their values
//        if (fieldsExcepted.length > fieldsActual.length)
//            System.out.println("you may have created more fields than needed...");
//        for (int i = 0; i < fieldsActual.length; i++) {
//            fieldsExcepted[i].setAccessible(true);
//            fieldsActual[i].setAccessible(true);
//            ArrayList<String> optional = optionalsValues.getOrDefault(fieldsExcepted[i].getName(), null);
//            if (fieldsExcepted[i].get(fieldsExcepted[i].get(fieldsExcepted[i].getName())) == null)
//                return false;
//            if (!(fieldsExcepted[i].get(fieldsExcepted[i].getName()).equals(fieldsActual[i].get(fieldsActual[i].getName()))))
//                if (optional != null && !optional.contains(fieldsExcepted[i].get(fieldsExcepted[i].getName())))
//                    return false;
//        }
//
//        return true;
//
//    }
}
