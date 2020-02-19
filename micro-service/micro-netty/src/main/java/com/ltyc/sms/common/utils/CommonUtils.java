package com.ltyc.sms.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public class CommonUtils {
    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private static String binDir = null;

    public static String getBinDir() {
        if (binDir == null) {
            binDir = System.getProperty("user.dir");
        }
        return binDir;
    }

    private static String workingDir = null;

    public static String getWorkingDir() {
        if (workingDir == null) {
            workingDir = new File(getBinDir()).getParent();
        }
        return workingDir;
    }

    private static String configDir = null;

    public static String getConfigDir() {
        if (configDir == null) {
            configDir = new File(new File(getWorkingDir()), "config").getAbsolutePath();
        }
        return configDir;
    }

    private static String cacheDir = null;

    public static String getCacheDir() {
        if (cacheDir == null) {
            cacheDir = new File(new File(getWorkingDir()), "cache").getAbsolutePath();
        }
        return cacheDir;
    }

    private static String tmpDir = null;

    public static String getTmpDir() {
        if (tmpDir == null) {
            tmpDir = new File(new File(getWorkingDir()), "tmp").getAbsolutePath();
        }
        return tmpDir;
    }

    private static String dataDir = null;

    public static String getDataDir() {
        if (dataDir == null) {
            dataDir = new File(new File(getWorkingDir()), "data").getAbsolutePath();
        }
        return dataDir;
    }

    /**
     * 根据objectId，获取消息发送到具体的MQ队列
     *
     * @param objectId
     * @return
     */
    public static int selectMsgQueueNum(String objectId, int sharding) {
        return Math.abs(objectId.hashCode() % sharding) + 1;
    }

    /**
     * 根据objectId，获取消息发送到具体的处理消息线程
     *
     * @param objectId
     * @return
     */
    public static int selectMsgProcessThread(String objectId, int threadMaxCount) {
        return Math.abs(objectId.hashCode() % threadMaxCount);
    }


    public static ApplicationContext getApplicationContext() {
        ApplicationContext context = null;
        while (true) {
            try {
                context = SpringUtil.getApplicationContext();
                if (context != null) {
                    return context;
                }
            } catch (Exception e) {
                logger.error("getApplicationContext error:" + e.getMessage(), e);
            }
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
            }
        }
    }

    public static String getEnvProperty(String propertyName, String defaultValue) {
        ApplicationContext context = CommonUtils.getApplicationContext();
        String value = context.getEnvironment().getProperty(propertyName);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }

    public static String getEnvProperty(String propertyName) {
        return getEnvProperty(propertyName, "");
    }

    /**
     * 获取当前时间的时间戳
     * @return
     */
    public static Long getCurrentTimeMillis() {
        return CachedMillisecondClock.INS.now();
//        return System.currentTimeMillis();
    }

    /**
     * 时间戳的明文,由客户端产生,格式为MMDDHHMMSS，即月日时分秒，10位数字的整型，右对齐 �?
     */
    public static String getTimestamp() {
        DateFormat format = new SimpleDateFormat("MMddhhmmss");
        return format.format(new Date());
    }

    /**
     * 获取本机Ip e.g.--172.16.132.177
     *
     * @return 本机Ip
     * @throws Exception
     */
    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            return "";
        }
    }

    public volatile static AtomicInteger count = new AtomicInteger();

    public static int getAtomicInt() {
        synchronized (count) {
            return count.getAndIncrement();
        }
    }

}
