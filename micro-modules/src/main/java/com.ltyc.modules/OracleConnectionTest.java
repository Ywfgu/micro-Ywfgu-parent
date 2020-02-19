package com.ltyc.modules;

import java.sql.*;

/**
 * @author guht
 * @version 1.0
 * @Description 测试oracle使用同一个连接同时入库时是否会有问题
 * @create 2018/9/7
 */
public class OracleConnectionTest {

    private static String USERNAMR = "ipmsp";
    private static String PASSWORD = "ipmsp$1234";
    private static String DRVIER = "oracle.jdbc.OracleDriver";
    private static String URL = "jdbc:oracle:thin:@10.8.132.222:1521:ipmscomm";
    Connection connection = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    public void AddData(String name, int age, String sex, String birthday,Long now) {
        connection = getConnection();

        Tread01 t1 = new Tread01();
        Tread02 t2 = new Tread02();
        t1.start();
        t2.start();
    }

    public void execPrepareStatement(String name, int age, String sex, String birthday,Long now) {
        String sql = "select count(*) from TABLE_TEST where 1 = 1";
        String sqlStr = "insert into TABLE_TEST values(?,?,?,?,?)";
        int count = 0;

        try {
            // 计算数据库student表中数据总数
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1) + 1;
//                System.out.println(rs.getInt(1));
            }
            // 执行插入数据操作
            pstm = connection.prepareStatement(sqlStr);

            pstm.setString(1, name);
            pstm.setInt(2, age);
            pstm.setString(3, sex);
            pstm.setString(4, birthday);
            pstm.setLong(5,now);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ReleaseResource();
        }
    }

    public Connection getConnection() {
        try {
            Class.forName(DRVIER);
            connection = DriverManager.getConnection(URL, USERNAMR, PASSWORD);
            System.out.println("成功连接数据库");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not find !", e);
        } catch (SQLException e) {
            throw new RuntimeException("get connection error!", e);
        }

        return connection;
    }

    public void ReleaseResource() {
//        if (rs != null) {
//            try {
//                rs.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        if (pstm != null) {
//            try {
//                pstm.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    }


    class Tread01 extends Thread{
        public void run(){
//            connection = getConnection();
            System.out.println("执行Tread01：");
            for (int i = 0; i < 20; i++) {
                execPrepareStatement("zhangsan",14,"m","",System.currentTimeMillis());
                System.out.println("执行Tread01："+i);
            }
        }
    }
    class Tread02 extends Thread{
        public void run(){
//            connection = getConnection();
            System.out.println("执行Tread02：");
            for (int i = 0; i < 20; i++) {
                execPrepareStatement("lisi",24,"w","",System.currentTimeMillis());
                System.out.println("执行Tread02："+i);
            }
        }
    }

    public static void main(String[] args) {
        OracleConnectionTest oo=new OracleConnectionTest();
        oo.AddData("zhangsan",14,"m","",System.currentTimeMillis());

    }
}
