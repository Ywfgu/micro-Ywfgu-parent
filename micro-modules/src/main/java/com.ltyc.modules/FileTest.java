package com.ltyc.modules;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/1/15
 */
public class FileTest {


    public static void main(String[] args) throws IOException {
        String path1 = "C:\\Users\\Haitao\\Desktop\\spxm-ceshi2.log";
        String path2 = "C:\\Users\\Haitao\\Desktop\\ceshi22.log";

        List<String> ori = new ArrayList();
        List<String> compare = new ArrayList();


        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        reader = new BufferedReader(new FileReader(new File(path1)));
        String tempStr;
        String[] temp;
        while ((tempStr = reader.readLine()) != null) {
            temp = tempStr.split("#%#");
            ori.add(temp[0]);
        }
        reader.close();
        //System.out.println(ori);

        reader = new BufferedReader(new FileReader(new File(path2)));
        while ((tempStr = reader.readLine()) != null) {
            if(tempStr.contains("trace_id")){
                compare.add(tempStr.substring(17,tempStr.length()-2));
            }

        }
        reader.close();
//        System.out.println(compare);


        List<String> duplicate = getDuplicateElements(compare);
        System.out.println("list 中重复的元素：" + duplicate);

//        for (String s : compare) {
////            if(!ori.contains(s)){
////                System.out.println("没有该ID："+s);
////            }
//        }
//        List list = compare.removeAll(ori);

    }

    public static <E> List<E> getDuplicateElements(List<E> list) {
        return list.stream()   // list 对应的 Stream
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                // 获得元素出现频率的 Map，键为元素，值为元素出现的次数
                .entrySet().stream()
                // 所有 entry 对应的 Stream
                .filter(entry -> entry.getValue() > 1)
                // 过滤出元素出现次数大于 1 的 entry
                .map(entry -> entry.getKey())
                // 获得 entry 的键（重复元素）对应的 Stream
                .collect(Collectors.toList());
        // 转化为 List
    }
}
