package com.ltyc.sms.common;

import org.springframework.core.env.MapPropertySource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/18
 */
public class DynamicLoadPropertySource extends MapPropertySource {
    private static Map<String, Object> MQDestinationNameMap = new ConcurrentHashMap<String, Object>();

    public DynamicLoadPropertySource(String name, Map<String, Object> source) {
        super(name, MQDestinationNameMap);
    }

    /**
     * 更新环境变量key value
     * @param key
     * @param value
     */
    public static synchronized void updateProperty(String key, String value) {
        MQDestinationNameMap.put(key, value);
    }

    @Override
    public Object getProperty(String name) {
        return MQDestinationNameMap.get(name);
    }

    public static boolean isContain(String key){
        return MQDestinationNameMap.containsKey(key);
    }

    public static boolean isContain(String key,String value){
        return MQDestinationNameMap.containsKey(key) && MQDestinationNameMap.get(key).equals(value);
    }

    public static void removeProperty(String key, String value){
        MQDestinationNameMap.remove(key, value);
    }
}
