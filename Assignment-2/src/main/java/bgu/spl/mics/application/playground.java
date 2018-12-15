package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class playground {

    private static HashMap<Integer, Customer> customers;
    private static HashMap<String,Integer> inventory;
    private static List<OrderReceipt> receipts;
    private static MoneyRegister moneyRegister;
    private static StringBuilder testResults;
    private static StringBuilder testErrors;

    public static void main(String[] args) throws InterruptedException, IOException {
        testErrors = new StringBuilder();
        testResults = new StringBuilder();
        readOutput(args);
        BasicTests generalTests = new BasicTests();
        generalTests.runTests();
        outputTestResults(testResults.toString(), testErrors.toString());
    }

    private static void outputTestResults(String testResults, String testErrors) throws IOException {
        Files.write(Paths.get("test_result.txt"), testResults.getBytes());
        Files.write(Paths.get("test_errors.txt"), testErrors.getBytes());
    }

    private static ObjectInputStream deserialization(String filename){
        FileInputStream fin = null;
        ObjectInputStream ooi = null;
        try {
            fin = new FileInputStream(filename);
            ooi = new ObjectInputStream(fin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return ooi;
        }

    }

    private static void readOutput(String[] args) throws IOException {
        try {
            printOutputHashMap(deserialization(args[1])); //customers
            testResults.append("-----------------------------------------------------------------------");
            printInv(deserialization(args[2])); //inv
            testResults.append("-----------------------------------------------------------------------");
            printOutputLinkList(deserialization(args[3])); //receipt
            testResults.append("-----------------------------------------------------------------------");
            printOutputMoneyRegister(deserialization(args[4])); //money register
            testResults.append("-----------------------------------------------------------------------");
        } catch (Exception e) {
            testErrors.append(e.getMessage());
        } finally {
        }
    }

    private static void printInv(ObjectInputStream ooi) throws IOException, ClassNotFoundException {
        testResults.append("\n");
        inventory = (HashMap<String, Integer>) ooi.readObject();
        inventory.keySet().stream().forEach(l -> testResults.append("bookTitle: " +l+" amount: " + inventory.get(l)+"\n"));
        testResults.append("\n");
    }

    private static void printOutputMoneyRegister(ObjectInputStream ooi) throws IOException, ClassNotFoundException {
        testResults.append("\n");
        MoneyRegister out = (MoneyRegister) ooi.readObject();
        testResults.append("total earnings: " + out.getTotalEarnings());
        testResults.append("\n");
    }

    private static void printOutputLinkList(ObjectInputStream ooi) throws IOException, ClassNotFoundException {
        testResults.append("\n");
        receipts = (LinkedList<OrderReceipt>) ooi.readObject();
        receipts.stream().forEach(l -> testResults.append(toString(l)));
        testResults.append("\n");
    }

    private static void printOutputHashMap(ObjectInputStream ooi) throws IOException, ClassNotFoundException {
        testResults.append("\n");
        customers = (HashMap<Integer, Customer>) ooi.readObject();
        customers.values().stream().forEach(l -> testResults.append(toString(l)));
        testResults.append("\n");
    }

    private static String toString(Object c) {
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

    private static class BasicTests{

        StringBuilder runTests() {
            runBasicTests();
            finishUp();
            return testResults;
        }

        private void runBasicTests() {
        }

        private void finishUp() {
            if (testErrors.toString().isEmpty())
                testErrors.append("Nice!! All tests passed! \n");
        }
    }

}
