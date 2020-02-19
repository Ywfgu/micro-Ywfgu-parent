package com.ltyc.common.hbase;

import com.google.common.collect.Sets;
import net.agkn.hll.HLL;
import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Map;
import java.util.Set;

public class HBaseClient extends AbstractHBaseClient {

    private static Set hllKeyList = Sets.newHashSet("BEN_USER_CNT", "OTHER_USER_CNT", "TM_CNT", "USER_CNT",
            "TOTAL_USER_CNT", "LTE_USER_CNT", "GET_REDPKG_USER_CNT", "WATCH_CW_USER_CNT");

    public HBaseClient(Map stormConf) {
        super(stormConf);
    }

    @Override
    public long objectToLong(Object o) {
        if(o instanceof HLL){
            return ((HLL)o).cardinality();
        }else if(o instanceof MutableLong){
            return ((MutableLong)o).getValue();
        }else{
            return (long)o;
        }
    }


    @Override
    public Object byteToObject(String colKey, byte[] bytes) {
        Object o ;
        if(hllKeyList.contains(colKey)){
            o = HLL.fromBytes(bytes);
        }else{
            try{
                o = Bytes.toLong(bytes);
            }catch (Exception e){
                logger.info("{} 不是long类型，转换为hll类型", colKey);
                o = HLL.fromBytes(bytes);
                hllKeyList.add(colKey);
            }
        }
        return o;
    }

    @Override
    public byte[] objectToByte(Object o) {
        if(o instanceof MutableLong){
            return Bytes.toBytes(((MutableLong)o).getValue());
        }else if(o instanceof HLL){
            return ((HLL)o).toBytes();
        }
        return new byte[0];
    }
}
