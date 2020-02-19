package com.ltyc.common.hbase;


import com.ltyc.common.utils.Callable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractHBaseClient {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String HBASE_QUORUM_KEY = "hbase.zookeeper.quorum";
    protected static final String HBASE_PORT_KEY = "hbase.zookeeper.property.clientPort";
    protected static final String HBASE_ZK_PARENT_KEY = "zookeeper.znode.parent";

    protected static final String HBASE_TABLE_NAME = "StormData";
    protected static final int HBASE_POOL_SIZE = 10;

    Configuration conf = null;
    Connection connection = null;
    ExecutorService hbasePool = null;

    private AbstractHBaseClient(){};

    public AbstractHBaseClient(Map stormConf) {
        logger.info("init hbase client.");
        Configuration configuration = new Configuration();
        configuration.set(HBASE_QUORUM_KEY, String.valueOf(stormConf.get(HBASE_QUORUM_KEY)));
        configuration.set(HBASE_PORT_KEY, String.valueOf(stormConf.get(HBASE_PORT_KEY)));
        configuration.set(HBASE_ZK_PARENT_KEY, String.valueOf(stormConf.get(HBASE_ZK_PARENT_KEY)));

        conf = HBaseConfiguration.create(configuration);
        hbasePool = Executors.newFixedThreadPool(HBASE_POOL_SIZE);
        connect();
        initTable(HBASE_TABLE_NAME);
        logger.info("finished init hbase client.");
    }

    protected void connect () {
        if(connection == null || connection.isClosed()){
            try {
                logger.info("建立HBase连接");
                connection = ConnectionFactory.createConnection(conf, hbasePool);
            } catch (IOException e) {
                logger.error("HBase 连接失败", e);
            }
        }
    }

    public synchronized void initTable(String tableName){
        try{
            if(!connection.getAdmin().tableExists(TableName.valueOf(tableName))){
                logger.info("开始初始化Table {}", tableName);
                HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
                tableDesc.addFamily(new HColumnDescriptor(Bytes.toBytes("F")));
                connection.getAdmin().createTable(tableDesc);
                logger.info("Table {} 初始化成功", tableName);
            }
        }catch (Exception e){
            logger.error("Table {} 创建失败", tableName, e);
        }
    }

    public <T> T getTable(String tableName, Callable<T> callable){
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            return callable.call(table);
        }catch (Exception e) {
            logger.error("Table 获取失败", e);
            connect();
        } finally {
            closeTable(table);
        }
        return null;
    }

    public List<Cell> listRow(String tableName, final String rowKey){
        final Table table = null;
        Result results = getTable(tableName, new Callable<Result>() {
            @Override
            public Result call(Object ...args) throws IOException {
                Get get = new Get(Bytes.toBytes(rowKey));
                return ((Table)args[0]).get(get);
            }
        });
        return results.listCells();
    }

    public void printCell(List<Cell> list){
        for(Cell cell : list){
            logger.info("[{}] [{}] [{}]",
                    Bytes.toString(CellUtil.cloneRow(cell)),
                    Bytes.toString(CellUtil.cloneQualifier(cell)),
                    objectToLong(byteToObject(Bytes.toString(CellUtil.cloneRow(cell)), CellUtil.cloneValue(cell))));
        }
    }

    public void restoreTable(com.google.common.collect.Table<String, String, Object> dataTable){
        Table table = null;
        try {
            long start = new Date().getTime();
            table = connection.getTable(TableName.valueOf(HBASE_TABLE_NAME));
            if(table instanceof HTable){
                ((HTable)table).setAutoFlush(false, false);
            }
            for(Map.Entry<String, Map<String, Object>> rowMap : dataTable.rowMap().entrySet()){
                Put put = new Put(Bytes.toBytes(rowMap.getKey()));
                for(Map.Entry<String, Object> colMap : rowMap.getValue().entrySet()){
                    put.addColumn(Bytes.toBytes("F"), Bytes.toBytes(colMap.getKey()), objectToByte(colMap.getValue()));
//                    logger.info("{} {} {}", colMap.getValue(), objectToByte(colMap.getValue()), Bytes.toLong(objectToByte(colMap.getValue())));
                }
                table.put(put);
            }
            if(table instanceof HTable){
                ((HTable)table).flushCommits();
            }
            logger.info("存储Table {} 条 耗时 {} ms", dataTable.size(), new Date().getTime()-start);
        }catch (IOException e) {
            logger.error("Table 获取失败", e);
            connect();
        } finally {
            closeTable(table);
        }
    }

    //获取对象的long值用于显示
    public abstract long objectToLong(Object o);

    public abstract Object byteToObject(String colKey, byte[] bytes);

    public abstract byte[] objectToByte(Object o);

    public void closeTable(Table table){
        if(table!=null){
            try {
                table.close();
            } catch (IOException e) {
                logger.error("HBase Table 关闭失败", e);
            }
        }
    }

    protected void close() {
        try {
            if(connection!=null && !connection.isClosed()){
                connection.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
