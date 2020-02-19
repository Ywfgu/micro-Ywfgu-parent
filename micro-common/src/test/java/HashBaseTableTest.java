import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.*;

/**
 * @author ltyc
 * @version 1.0
 * @Description hashbasetable
 * @create 18.1.25
 */
public class HashBaseTableTest {
    Table<String, String, Integer> table = HashBasedTable.create();

//    table.put("0010","i",1);
//    table.put("0010","e",2);
//    table.put("0020","c",2);

    Set<String> columnKeySet = table.columnKeySet();
    Set<Table.Cell<String, String, Integer>> cellSet = table.cellSet();
    Map<String, Integer> column = table.column("e");
    Map<String, Integer> row = table.row("0010");
    Map<String, Map<String, Integer>> rowMap = table.rowMap();
    Set<String> rowKeySet = table.rowKeySet();//获取行的值到一个set集合中
    List<Map<String, Integer>> list = new ArrayList();
    int i = 0;


//    for(String r:rowKeySet){
//        Map<String, Integer> map = new HashMap();
//        map = table.row(r); //map的key-value是column和参数的一个对应（key-value不为一个）这就算是遍历出同一个节点下的内容了
//        list.add(i, map);
//        i++;
//    }

//    for(int j = 0;j<list.size();j++){
//        for (String m : list.get(j).keySet()) {
//            System.out.print(m + " " + list.get(j).get(m));
//        }
//        System.out.println();
//    }


}
