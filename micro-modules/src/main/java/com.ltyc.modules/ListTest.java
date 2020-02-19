package com.ltyc.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltyc
 * @version 1.0
 * @Description listtest
 * @create 18.1.29
 */
public class ListTest {

    public static Map<String,Map<String,List>> newdata() {
        Map<String,Map<String,List>> all = new HashMap<String, Map<String, List>>();

        Map<String,List> map1 = new HashMap<String, List>();
        Map<String,List> map2 = new HashMap<String, List>();

        List<String> l1 = new ArrayList();
        l1.add("1");
        l1.add("3");
        l1.add("2");
        l1.add("4");
        l1.add("5");

        List<String> l2 = new ArrayList();
        l2.add("b");
        l2.add("a");
        l2.add("c");
        l2.add("d");
        l2.add("e");

        map1.put("l1",l1);
        map2.put("l2",l2);

        all.put("map1",map1);
        all.put("map2",map2);
        return all;
    }

//    public static void main(String[] args) {
//        Map<String,Map<String,List>> data = newdata();
//        data.get("map1").get("l1").add("6");
//        System.out.println(data);
//    }

    /**
     * 取数组后一百个元素
     * @param args
     */
    public static void main(String[] args) {
        List<String> l1 = new ArrayList();
        l1.add("1");
        l1.add("3");
        l1.add("2");
        l1.add("4");
        l1.add("5");
        l1.add("6");
        l1.add("7");
        l1.add("8");
        l1.add("9");
        l1.add("10");


        //result_list = result_list.subList(result_list.size()-100,result_list.size());

        for (int i = 0; i < l1.size(); i++) {
            System.out.println(l1.get(i));
        }

    }
}
