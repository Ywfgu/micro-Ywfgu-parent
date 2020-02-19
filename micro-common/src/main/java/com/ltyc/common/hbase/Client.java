package com.ltyc.common.hbase;

import com.google.common.collect.Sets;
import com.ltyc.common.utils.ConfigUtils;
import org.apache.hadoop.hbase.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Client {
    private static Set hllKeyList = Sets.newHashSet("BEN_USER_CNT", "OTHER_USER_CNT", "TM_CNT", "USER_CNT",
            "TOTAL_USER_CNT", "LTE_USER_CNT", "GET_REDPKG_USER_CNT", "WATCH_CW_USER_CNT");

    HBaseClient hBaseClient = null;
    public Client(){
        Map config = ConfigUtils.LoadConf("hbase.yaml");
        hBaseClient = new HBaseClient(config);
    }

    public void list(String tabName, String rowKey){
        List<Cell> list = hBaseClient.listRow(tabName, rowKey);
        hBaseClient.printCell(list);
        hBaseClient.close();
    }

    public static void main(String[] args) {

//        System.setProperty("hadoop.home.dir", "D:\\DevelopTool\\ToolsInstall\\hadoop");
        System.out.println("1111111");
//        Client client = new Client();
//        client.list("StormData", "YWAA:合肥市:1803261650");
//        StringBuffer guide = new StringBuffer("Guide:\n");
//        guide.append("列表显示： list [tabName:StormData] colName \n");
//        if(args.length == 0){
//            System.out.println(guide.toString());
//            return ;
//        }
//        Client client = new Client();
//        switch (args[0]) {
//            case "list" :
//                if (args.length < 3) {
//                    client.list("StormData", args[1]);
//                } else {
//                    client.list(args[1], args[2]);
//                }
//                break;
//            case "help":
//            default:
//                System.out.println(guide.toString());
//                return ;
//        }

    }
}
