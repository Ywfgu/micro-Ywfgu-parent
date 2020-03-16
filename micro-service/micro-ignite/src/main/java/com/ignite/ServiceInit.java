package com.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.springframework.stereotype.Component;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/3/12
 */
@Component
public class ServiceInit extends Thread  {
    @Override
    public void run() {
        try {
            test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test() throws Exception {
        Ignite ignite = Ignition.start("ignite-cluster.xml");//
    }
}
