package com.ltyc.modules;

import java.util.Date;
import java.util.Random;

/**
 * @author ltyc
 * @version 1.0
 * @Description arrtest
 * @create 18.2.5
 */
public class ArrTest {

    private static final String[] destqueues = null;


    public void test1(){
            // Positive example:
        long a = System.currentTimeMillis();
        // Negative example:
        long b = new Date().getTime();

        long c = System.nanoTime();

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }

    public static void test2(){
        String ip = "10.243.73.63|10.243.73.64|10.243.73.65|10.243.73.66|10.243.72.62|10.243.72.64|10.243.72.65|10.243.72.67";
        String ips[] = ip.split("\\|");

        for (int i = 0; i < 100; i++) {
            System.out.println(ips[new Random().nextInt(8)]);
        }

//        System.out.println();
    }

    public static void main(String[] args) {

        test2();

    }



}
