package com.ltyc.modules;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.UUID;

/**
 * @author ltyc
 * @version 1.0
 * @Description string
 * @create 2018/6/1
 */
public class StringTest {

    public static void test1(){
        String filePath = "/gz-warhouse/nginx/EC3E749E1BA386E89E718B80BA4AD217.tar";
        int index = filePath.lastIndexOf("/");

        String path = index >0? filePath.substring(0, index) : "/";
        String fileName = filePath.substring(index+1);
        String fileName_regex = fileName.replaceAll("\\.", "\\\\.");

        System.out.println(path);
        System.out.println(fileName);
        System.out.println(fileName_regex);
    }

    public static void test2(){
        String s = "scan_start_time->";
        String[] sarr = s.split("->");
        System.out.println(sarr.length);
//        System.out.println(sarr[0]);
//        System.out.println(sarr[1]);

    }

    public static String combinePfx(String key){
        try{
            String pfxStr = key.substring(0, key.indexOf(":", 11));
            int pfx = Math.abs(pfxStr.hashCode()%1000);
            return (pfx<10?"0"+pfx:""+pfx)+"0"+key;
        }catch (Exception e){
            //logger.error("CombinePfx Error key {}", key);
            return "020"+key;
        }
    }

    public void test(){
        String s = "799,798,797,796,795,794,793,792,791,790,701";
        String[] ss=  s.split(",");

        for (int i = 0; i < ss.length; i++) {
            System.out.println(ss[i].hashCode()%1000);
        }
    }

    public void test3(){
        String  s = "{\"ALARM_MSG_FROM:1&NMS_ALARM_TYPE:1\":\"AlarmCache1\",\"ALARM_MSG_FROM:1&NMS_ALARM_TYPE:2\":\"AlarmCache2\"}";
        Map map = JSON.parseObject(s,Map.class);
        System.out.println(map);
    }


    public static void main(String[] args) {
        StringTest st = new StringTest();
        st.test3();

    }
}
