package com.ltyc.modules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author guht
 * @version 1.0
 * @Description date
 * @create 2018/11/15
 */
public class DateTest {
    public static Date StringToDate(String source, String pattern)  throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(source);
    }
    public static String DateToString(Date source, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(source);
    }

    public static void main(String[] args) throws ParseException {
        String d = "2018-11-15 13:37:40";
        Date date = StringToDate(d,"yyyy-MM-dd HH:mm:ss");
        System.out.println(DateToString(date,"yyyymmdd0000"));
    }
}
