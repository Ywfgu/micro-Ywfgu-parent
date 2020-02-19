package com.ltyc.modules;

//import com.eastcom_sw.data_combine.client.thrift.client.ThriftClient;
//import com.eastcom_sw.data_combine.client.thrift.resp.DataFetchSortResp;
import redis.clients.util.SafeEncoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Description webservicetest
 * @create 18.3.5
 */
public class WebserviceTest {

//    public static void main(String[] args){
//        List list = new ArrayList();
//        list.add("TEST:180309000");
//        try {
//            //List<Map<String, byte[]>>
//            DataFetchSortResp resp = new ThriftClient("10.198.131.2", "6668").fetchSortData(list);
//            List arr = resp.getData();
//            for (int i = 0; i < arr.size(); i++) {
//                Map map = (Map) arr.get(i);
//                Iterator entries = map.entrySet().iterator();
//                while (entries.hasNext()) {
//                    Map.Entry entry = (Map.Entry) entries.next();
//                    String key = (String)entry.getKey();
//                    Object value = entry.getValue();
//
//                    byte[] bytes = null;
//                    if(value instanceof byte[]){
//                        bytes = (byte[])value;
//                        if(isHLL(bytes)){
//                            HLL h = HLLUtils.hll(bytes);
//                            logBasic( "key:" + key + ",   value:" + String.valueOf(h.cardinality()));
//                        }else{
//                            String s = String.valueOf(value);
//                            logBasic( "key:" + key + ",   value:" + s);
//                        }
//                    }else{
//                        logBasic( " not byte[] key:" + key);
//                    }
//                    //logBasic( "key:" + key + ",   value:" + value );
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        int i =1000;
//        int s = 1000;
//
//        byte[] bt = long2Bytes(s);
//        byte[] bt = int2bytes(s);

        Double d = 10000000.0;


        String s = "100";

        System.out.println(isHLL(s.getBytes()));
//
//
//        Long l = 1000000L;
//
//        byte[] b = long2Bytes(l);
//
//
//        System.out.println(bytes2Long(b));
//        ByteArrayInputStream byteInt = new ByteArrayInputStream(bt);
//        ObjectInputStream objInt = new ObjectInputStream(byteInt);
//        List<Map<String, byte[]>> result = (List)objInt.readObject();
//        objInt.readLong();
//        ByteBuffer buffer = ByteBuffer.allocate(8);


    }
    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static long bytes2Long(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }
    public static byte[] int2bytes(int num){
        byte[] result = new byte[4];
        result[0] = (byte)((num >>> 24) & 0xff);//说明一
        result[1] = (byte)((num >>> 16)& 0xff );
        result[2] = (byte)((num >>> 8) & 0xff );
        result[3] = (byte)((num >>> 0) & 0xff );
        return result;
    }

    private static boolean isHLL(byte[] bs){
        String s = SafeEncoder.encode(bs);
        System.out.println("s="+s);
        return !s.matches("-?[0-9]+.?[0-9]+|-?[0-9]+");
    }


}
