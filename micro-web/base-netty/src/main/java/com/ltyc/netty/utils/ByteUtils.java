package com.ltyc.netty.utils;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2019/11/13
 */
public class ByteUtils {
    public static byte[] mergeBytes(byte[]... values){
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    public static byte[] subBytes(byte[] values, int from, int to){
        byte[] result = new byte[to-from];
        System.arraycopy(values, from, result, 0, to-from);
        return result;
    }
}
