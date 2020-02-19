package com.ltyc.modules;

import net.agkn.hll.HLL;

/**
 * @author guht
 * @version 1.0
 * @Description hll
 * @create 2018/10/25
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

}
