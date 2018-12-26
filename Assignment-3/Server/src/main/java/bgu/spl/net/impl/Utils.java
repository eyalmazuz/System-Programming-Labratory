package bgu.spl.net.impl;

public class Utils {
    public static short twoBytesToShort(byte[] byteArr, int start)
    {
        short result = (short)((byteArr[start] & 0xff) << 8);
        result += (short)(byteArr[start+1] & 0xff);
        return result;
    }

    public static short twoBytesToShort(String strArr, int start)
    {
        short result = (short)((strArr.charAt(start) & 0xff) << 8);
        result += (short)(strArr.charAt(start+1) & 0xff);
        return result;
    }


}
