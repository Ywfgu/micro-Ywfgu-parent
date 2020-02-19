package com.ltyc.common.utils;

import net.agkn.hll.HLL;
import net.agkn.hll.HLLType;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;

/**
 * @author ltyc
 * @version 1.0
 * @Description hll工具类
 * @create 18.1.24
 */
public class HLLUtils {

    public static long longValue(byte[] byteValue) {
        try {
            long value = 0;
            HLL hll = null;
            if (byteValue != null) {
                hll = HLL.fromBytes(byteValue);
                value = hll.cardinality();
            }
            return value;
        } catch (Throwable ex) {
            return 0;
        }
    }

    public static HLL hll(byte[] byteValue) {
        try {
            HLL hll = null;
            if (byteValue != null) {
                hll = HLL.fromBytes(byteValue);
            }
            return hll;
        } catch (Throwable ex) {
            return create();
        }
    }

    public static HLL union(HLL... hlls) {
        HLL result = create();
        for (HLL hll : hlls) {
            result.union(hll);
        }
        return result;
    }

    public static HLL union(byte [] bytes1,byte [] bytes2 ) {
        HLL result = create();
        result.union(hll(bytes1));
        result.union(hll(bytes2));
        return result;
    }

    public static HLL create() {
        return new HLL(14/* log2m */, 5/* registerWidth */);
    }

    public static HLL create(int a,int b) {
        return new HLL(14/* log2m */, 5/* registerWidth */);
    }

    public static String parse(HLL hll){
        String r = "";
        byte[] b = hll.toBytes();
        for (int i = 0; i < b.length; i++) {
            r += b[i];
            r += ",";
        }
        return r;
    }

    public static void main(String[] args) throws InterruptedException {
//17,-114,127,
//        System.out.println(parse(create(14,5)));
//        System.out.println(parse(create(14,6)));
//        System.out.println(parse(create(14,7)));
//        System.out.println(parse(create(14,8)));
//        System.out.println("**********************");
//        System.out.println(parse(create(14,8)));
//        System.out.println(parse(create(15,8)));
//        System.out.println(parse(create(16,8)));
//        System.out.println(parse(create(17,8)));
//        System.out.println("**********************");
//        System.out.println(parse(create(4,1)));
//        System.out.println(parse()));
        System.out.println((byte)(15 & 1));
    }

}
