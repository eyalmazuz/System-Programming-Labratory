package bgu.spl.mics.application;

import java.io.*;

public class Utils implements Serializable {
    public static void serialization(String filename, Object serialize){
        try(FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(serialize);
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static ObjectInputStream deserialization(String filename){
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

}
