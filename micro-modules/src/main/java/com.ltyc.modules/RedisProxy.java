//package com.ltyc.modules;
//
//import jdk.nashorn.internal.runtime.regexp.joni.Config;
//import net.agkn.hll.HLL;
//import org.apache.commons.pool.impl.GenericKeyedObjectPool;
//import org.apache.commons.pool.impl.GenericObjectPool;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.Pipeline;
//import redis.clients.jedis.exceptions.JedisException;
//import redis.clients.util.Pool;
//import redis.clients.util.SafeEncoder;
//
//import java.io.IOException;
//import java.util.*;
//
///**
// * @author guht
// * @version 1.0
// * @Description RedisProxy
// * @create 2018/10/25
// */
//public class RedisProxy {
//
//    private Pool<Jedis> jedisPool;
//    private Pipeline pipeline;
//    private Pool<Jedis> jedisPool2;
//    private Pipeline pipeline2;
//    private int maxBatchSize = 1000;
//    private HLLUtils hllUtils = new HLLUtils();
//    private int expireSec = 100800;// redis2 key expire seconds 28小时
//    private String DB_PERSIST_QUEUE = "DbKeys.DB_PERSIST_QUEUE";
//
//    public void setNeedPersist(boolean needPersist) {
//        this.needPersist = needPersist;
//    }
//
//    private boolean needPersist = false;
//
//    public void setExpireSec3(int expireSec3) {
//        this.expireSec3 = expireSec3;
//    }
//
//    private int expireSec3 = 7200;// redis2 key expire seconds 2小时
//
//    private void setMaxBatchSize(int maxBatchSize) {
//        this.maxBatchSize = maxBatchSize;
//    }
//
//    public void setExpireSec(int expireSec) {
//        this.expireSec = expireSec;
//    }
//
//    public List<Map<String, Object>> hgetAll(List<String> keys) {
//        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//        if (keys.size() == 0) {
//            return new ArrayList<Map<String, Object>>();
//        }
//        if (keys.size() <= maxBatchSize) {
//            List<Map<byte[], byte[]>> data = batchReadByte(keys);
//            return handleResult(data, result);
//        } else {
//            List<List<String>> keysList = fixedSize(keys,
//                    maxBatchSize);
//            for (List<String> ks : keysList) {
//                List<Map<byte[], byte[]>> data = batchReadByte(ks);
//                handleResult(data, result);
//            }
//            return result;
//        }
//    }
//
//    public Map<String, Map<String, Object>> hgetAllMap(List<String> keys) {
//        Map<String, Map<String, Object>> result = new HashMap<>();
//        if (keys.size() == 0) {
//            return new HashMap<>();
//        }
//        if (keys.size() <= maxBatchSize) {
//            Map<String, Map<byte[], byte[]>> data = batchReadMap(keys);
//            return handleResultMap(data, result);
//        } else {
//            List<List<String>> keysList = fixedSize(keys,
//                    maxBatchSize);
//            for (List<String> ks : keysList) {
//                Map<String, Map<byte[], byte[]>> data = batchReadMap(ks);
//                handleResultMap(data, result);
//            }
//            return result;
//        }
//    }
//
//    public Map<String, Map<String, Object>> hgetAllMap2nd(List<String> keys) {
//        Map<String, Map<String, Object>> result = new HashMap<>();
//        if (keys.size() == 0) {
//            return new HashMap<>();
//        }
//        if (keys.size() <= maxBatchSize) {
//            Map<String, Map<byte[], byte[]>> data = batchReadMap2nd(keys);
//            return handleResultMap(data, result);
//        } else {
//            List<List<String>> keysList = fixedSize(keys,
//                    maxBatchSize);
//            for (List<String> ks : keysList) {
//                Map<String, Map<byte[], byte[]>> data = batchReadMap2nd(ks);
//                handleResultMap(data, result);
//            }
//            return result;
//        }
//    }
//
//    public Map<String, String> hgetAll2nd(String key) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool2.getResource();
//            Map<String, String> result = jedis.hgetAll(key);
//            return result;
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }
//
//    public void hmset4Obj(Map<String, Map<String, Object>> map) {
//        Map<String, Map<String, HLL>> hllMap = new HashMap<String, Map<String, HLL>>();
//        Map<String, Map<String, String>> sMap = new HashMap<String, Map<String, String>>();
//        for (Map.Entry<String, Map<String, Object>> mm : map.entrySet()) {
//            Map<String, HLL> mh = new HashMap<String, HLL>();
//            Map<String, String> ms = new HashMap<String, String>();
//            for (Map.Entry<String, Object> m : mm.getValue().entrySet()) {
//                if (m.getValue() instanceof HLL) {
//                    mh.put(m.getKey(), (HLL) m.getValue());
//                } else {
//                    ms.put(m.getKey(), String.valueOf(m.getValue()));
//                }
//            }
//            hllMap.put(mm.getKey(), mh);
//            sMap.put(mm.getKey(), ms);
//        }
//        if (!sMap.isEmpty()) {
//            hmset(sMap);
//        }
//        if (!hllMap.isEmpty()) {
//            hmsetBytes(formatHllMap(hllMap));
//        }
//    }
//
//    public void hmset4ObjForever(Map<String, Map<String, Object>> map) {
//        Map<String, Map<String, HLL>> hllMap = new HashMap<String, Map<String, HLL>>();
//        Map<String, Map<String, String>> sMap = new HashMap<String, Map<String, String>>();
//        for (Map.Entry<String, Map<String, Object>> mm : map.entrySet()) {
//            Map<String, HLL> mh = new HashMap<String, HLL>();
//            Map<String, String> ms = new HashMap<String, String>();
//            for (Map.Entry<String, Object> m : mm.getValue().entrySet()) {
//                if (m.getValue() instanceof HLL) {
//                    mh.put(m.getKey(), (HLL) m.getValue());
//                } else {
//                    ms.put(m.getKey(), String.valueOf(m.getValue()));
//                }
//            }
//            hllMap.put(mm.getKey(), mh);
//            sMap.put(mm.getKey(), ms);
//        }
//        if (!sMap.isEmpty()) {
//            hmsetForever(sMap);
//        }
//        if (!hllMap.isEmpty()) {
//            hmsetBytesForever(formatHllMap(hllMap));
//        }
//    }
//
//    public void hmset4Obj2nd(LinkedHashMap<String, Map<String, Object>> map) {
//        List<String> list = new ArrayList<String>();
//        list.addAll(map.keySet());
//        List<Map<String, Object>> result = hgetAll(list);
//        int index = 0;
//        Map<String, Map<String, HLL>> hllMap = new HashMap<String, Map<String, HLL>>();
//        Map<String, Map<String, String>> sMap = new HashMap<String, Map<String, String>>();
//        for (Map.Entry<String, Map<String, Object>> mm : map.entrySet()) {
//            Map<String, HLL> mh = new HashMap<String, HLL>();
//            Map<String, String> ms = new HashMap<String, String>();
//            Map<String, Object> resultMap = result.get(index);
//            index++;
//            for (Map.Entry<String, Object> m : mm.getValue().entrySet()) {
//                if (m.getValue() instanceof HLL) {
//                    if (!resultMap.isEmpty() && resultMap.containsKey(m.getKey())) {
//                        HLL[] hs = new HLL[]{(HLL) m.getValue(), hllUtils.hll((byte[]) resultMap.get(m.getKey()))};
//                        HLL h = hllUtils.union(hs);
//                        mh.put(m.getKey(), h);
//                    } else {
//                        mh.put(m.getKey(), (HLL) m.getValue());
//                    }
//                } else {
//                    if (resultMap.containsKey(m.getKey())) {
//                        ms.put(m.getKey(), String.valueOf(Long.parseLong(m.getValue() + "")
//                                + Long.parseLong(resultMap.get(m.getKey()) + "")));
//                    } else {
//                        ms.put(m.getKey(), String.valueOf(m.getValue()));
//                    }
//                }
//            }
//            hllMap.put(mm.getKey(), mh);
//            sMap.put(mm.getKey(), ms);
//        }
//        if (!sMap.isEmpty()) {
//            hmset2nd(sMap);
//        }
//        if (!hllMap.isEmpty()) {
//            hmsetBytes2nd(formatHllMap(hllMap));
//        }
//    }
//
//    public void hmset4ObjMergeOld(LinkedHashMap<String, Map<String, Object>> map) {
//        List<String> list = new ArrayList<String>();
//        list.addAll(map.keySet());
//        List<Map<String, Object>> result = hgetAll(list);
//        int index = 0;
//        Map<String, Map<String, HLL>> hllMap = new HashMap<String, Map<String, HLL>>();
//        Map<String, Map<String, String>> sMap = new HashMap<String, Map<String, String>>();
//        for (Map.Entry<String, Map<String, Object>> mm : map.entrySet()) {
//            Map<String, HLL> mh = new HashMap<String, HLL>();
//            Map<String, String> ms = new HashMap<String, String>();
//            Map<String, Object> resultMap = result.get(index);
//            index++;
//            for (Map.Entry<String, Object> m : mm.getValue().entrySet()) {
//                if (m.getValue() instanceof HLL) {
//                    if (!resultMap.isEmpty() && resultMap.containsKey(m.getKey())) {
//                        HLL[] hs = new HLL[]{(HLL) m.getValue(), hllUtils.hll((byte[]) resultMap.get(m.getKey()))};
//                        HLL h = hllUtils.union(hs);
//                        mh.put(m.getKey(), h);
//                    } else {
//                        mh.put(m.getKey(), (HLL) m.getValue());
//                    }
//                } else {
//                    if (resultMap.containsKey(m.getKey())) {
//                        ms.put(m.getKey(), String.valueOf(Long.parseLong(m.getValue() + "")
//                                + Long.parseLong(resultMap.get(m.getKey()) + "")));
//                    } else {
//                        ms.put(m.getKey(), String.valueOf(m.getValue()));
//                    }
//                }
//            }
//            hllMap.put(mm.getKey(), mh);
//            sMap.put(mm.getKey(), ms);
//        }
//        if (!sMap.isEmpty()) {
//            hmset(sMap);
//        }
//        if (!hllMap.isEmpty()) {
//            hmsetBytes(formatHllMap(hllMap));
//        }
//    }
//
//    private void hmsetBytes(Map<byte[], Map<byte[], byte[]>> hashs) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            Pipeline pipeline = jedis.pipelined();
//
//            for (byte[] key : hashs.keySet()) {
//                Map<byte[], byte[]> hash = hashs.get(key);
//                if ((hash != null) && (!hash.isEmpty()))
//                {
//                    pipeline.hmset(key, hash);
//                    pipeline.expire(key, expireSec);
//                }
//            }
//
//            pipeline.sync();
//            jedisPool.returnResource(jedis);
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    private void hmsetBytes2nd(Map<byte[], Map<byte[], byte[]>> hashs) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            Pipeline pipeline = jedis.pipelined();
//
//            for (byte[] key : hashs.keySet()) {
//                Map<byte[], byte[]> hash = hashs.get(key);
//                if (hash == null || hash.isEmpty()) {
//                    continue;
//                }
//                pipeline.hmset(key, hash);
//                pipeline.expire(key, expireSec);
//            }
//
//            pipeline.sync();
//            jedisPool.returnResource(jedis);
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    private void hmsetBytesForever(Map<byte[], Map<byte[], byte[]>> hashs) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            Pipeline pipeline = jedis.pipelined();
//
//            for (byte[] key : hashs.keySet()) {
//                Map<byte[], byte[]> hash = hashs.get(key);
//                if (hash == null || hash.isEmpty()) {
//                    continue;
//                }
//                pipeline.hmset(key, hash);
//            }
//
//            pipeline.sync();
//            jedisPool.returnResource(jedis);
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    private void hmset(Map<String, Map<String, String>> hashs) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            Pipeline pipeline = jedis.pipelined();
//            for (String key : hashs.keySet()) {
//
//                Map<String, String> hash = hashs.get(key);
//                if ((hash != null) && (!hash.isEmpty()))
//                {
//                    pipeline.hmset(key, hash);
//                    pipeline.expire(key, expireSec);
//                }
//            }
//            pipeline.sync();
//            jedisPool.returnResource(jedis);
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    private void hmsetForever(Map<String, Map<String, String>> hashs) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            Pipeline pipeline = jedis.pipelined();
//            for (String key : hashs.keySet()) {
//
//                Map<String, String> hash = hashs.get(key);
//                if (hash == null || hash.isEmpty()) {
//                    continue;
//                }
//                pipeline.hmset(key, hash);
//            }
//            pipeline.sync();
//            jedisPool.returnResource(jedis);
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    public void hmset2nd(Map<String, Map<String, String>> hashs) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool2.getResource();
//            Pipeline pipeline = jedis.pipelined();
//            for (String key : hashs.keySet()) {
//
//                Map<String, String> hash = hashs.get(key);
//                if (hash == null || hash.isEmpty()) {
//                    continue;
//                }
//                pipeline.hmset(key, hash);
//                pipeline.expire(key, expireSec);
//                if (needPersist) {
//                    jedis.lpush(DB_PERSIST_QUEUE, key);
//                }
//            }
//            pipeline.sync();
//
//            jedisPool2.returnResource(jedis);
//        } catch (JedisException e) {
//            jedisPool2.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    /**
//     * 28小时清
//     *
//     * @param key
//     * @param hash
//     */
//    public void hmset2nd(String key, Map<String, String> hash) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool2.getResource();
//            if (hash == null || hash.isEmpty()) {
//                return;
//            }
//            jedis.hmset(key, hash);
//            jedis.expire(key, expireSec);
//            if (needPersist) {
//                jedis.lpush(DB_PERSIST_QUEUE, key);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }
//
//    /**
//     * 这个失效时间要2小时
//     *
//     * @param key
//     * @param hash
//     */
//    public void hmset2nd3(String key, Map<String, String> hash) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool2.getResource();
//            if (hash == null || hash.isEmpty()) {
//                return;
//            }
//            jedis.hmset(key, hash);
//            jedis.expire(key, expireSec3);
//            if (needPersist) {
//                jedis.lpush(DB_PERSIST_QUEUE, key);
//            }
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }
//
//    private Map<byte[], Map<byte[], byte[]>> formatHllMap(Map<String, Map<String, HLL>> map) {
//        Map<byte[], Map<byte[], byte[]>> result = new HashMap<byte[], Map<byte[], byte[]>>();
//        for (Map.Entry<String, Map<String, HLL>> d : map.entrySet()) {
//            result.put(SafeEncoder.encode(d.getKey()), toByteMap(d.getValue()));
//        }
//        return result;
//    }
//
//    private Map<byte[], byte[]> toByteMap(Map<String, HLL> hllMap) {
//        if (hllMap == null || hllMap.isEmpty()) {
//            return Collections.emptyMap();
//        }
//        Map<byte[], byte[]> result = new HashMap<byte[], byte[]>();
//        for (String key : hllMap.keySet()) {
//            HLL hll = hllMap.get(key);
//            if (hll == null) {
//                hll = hllUtils.create();
//            }
//            result.put(SafeEncoder.encode(key), hll.toBytes());
//        }
//        return result;
//    }
//
//    public void init(String host, int port, String pwd, int maxBatchSize) {
//        GenericObjectPoolConfig gop = new GenericObjectPoolConfig();
//        gop.setMaxTotal(8);
//        gop.setMaxIdle(8);
//        gop.setTestOnBorrow(true);
//        setMaxBatchSize(maxBatchSize);
//
//        if (null != pwd && !pwd.equals("")) {
//            this.jedisPool = new JedisPool(gop, host, port, 10000);
//        } else {
//            this.jedisPool = new JedisPool(gop, host, port, 10000, pwd);
//        }
//    }
//
////    public void init2nd(String host, int port, String pwd, int maxBatchSize) {
////        GenericObjectPoolConfig gop = new GenericObjectPoolConfig();
////        gop.setMaxTotal(8);
////        gop.setMaxIdle(8);
////        gop.setTestOnBorrow(true);
////        setMaxBatchSize(maxBatchSize);
////        if (StringUtils.isBlank(pwd)) {
////            jedisPool2 = new JedisPool(gop, host, port, 10000);
////        } else {
////            jedisPool2 = new JedisPool(gop, host, port, 10000, pwd);
////        }
////    }
//
//    public void init(String host, int port, String pwd) {
//        init(host, port, pwd, maxBatchSize);
//    }
//
//    public void init2nd(String host, int port, String pwd) {
//        init(host, port, pwd, maxBatchSize);
//    }
//
//    public void destroy() {
//        if (jedisPool != null) {
//            jedisPool.destroy();
//        }
//        if (jedisPool2 != null) {
//            jedisPool2.destroy();
//        }
//        if (pipeline != null) {
//            pipeline.clear();
//            try {
//                pipeline.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (pipeline2 != null) {
//            pipeline2.clear();
//            try {
//                pipeline2.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public <T> List<List<T>> fixedSize(List<T> list, int size) {
//        List<List<T>> result = new ArrayList<List<T>>();
//        int index = 0;
//        while (index + size <= list.size()) {
//            result.add(list.subList(index, index + size));
//            index += size;
//        }
//        if (index < list.size()) {
//            result.add(list.subList(index, list.size()));
//        }
//        return result;
//    }
//
//    private Map<String, Object> toObjectMap(Map<byte[], byte[]> info) {
//        if (info == null || info.isEmpty()) {
//            return Collections.emptyMap();
//        }
//        Map<String, Object> result = new HashMap<String, Object>();
//        for (byte[] fieldByte : info.keySet()) {
//            String field = SafeEncoder.encode(fieldByte);
////            if (field.endsWith(":n") || field.endsWith(":" + DbKeys.value) || field.endsWith(":" + DbKeys.top)
////                    || field.endsWith(":" + DbKeys.topall) || field.endsWith(":" + DbKeys.valueall)
////                    || field.endsWith(":" + DbKeys.topday) || field.endsWith(":" + DbKeys.time)
////                    || field.endsWith(":tophour")) {
////                continue;
////            } else {
//            byte[] byteValue = info.get(fieldByte);
//            String s = SafeEncoder.encode(byteValue);
//            if (!s.matches("-?[0-9]+.?[0-9]+|-?[0-9]+") && validateHLL(byteValue)) {
//                result.put(field, hllUtils.hll(byteValue));
//            } else {
//                result.put(field, s);
//            }
////            }
//        }
//        return result;
//    }
//
//    public static boolean validateHLL(byte[] bytes){
//        if(bytes.length>2 ){// && bytes[0] == 18 && bytes[1] == -114
//            return true;
//        }
//        return false;
//    }
//
//    private List<Map<byte[], byte[]>> batchReadByte(List<String> keys) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            pipeline = jedis.pipelined();
//            for (String key : keys) {
//                pipeline.hgetAll(SafeEncoder.encode(key));
//            }
//            List<Object> objs = pipeline.syncAndReturnAll();
//            jedisPool.returnResource(jedis);
//            List<Map<byte[], byte[]>> result = new ArrayList<Map<byte[], byte[]>>();
//            try {
//                for (int i = 0; i < keys.size(); i++) {
//                    result.add((Map<byte[], byte[]>) objs.get(i));
//                }
//            } catch (Exception ex) {
//                throw ex;
//            }
//            return result;
//        } catch (Throwable th) {
//            if (jedis != null) {
//                jedisPool.returnBrokenResource(jedis);
//            }
//            throw new RuntimeException(th);
//        }
//    }
//
//    private Map<String, Map<byte[], byte[]>> batchReadMap(List<String> keys) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            pipeline = jedis.pipelined();
//            for (String key : keys) {
//                pipeline.hgetAll(SafeEncoder.encode(key));
//            }
//            List<Object> objs = pipeline.syncAndReturnAll();
//            jedis.close();
//            Map<String, Map<byte[], byte[]>> result = new HashMap<>();
//            try {
//                for (int i = 0; i < keys.size(); i++) {
//                    result.put(keys.get(i), (Map<byte[], byte[]>) objs.get(i));
//                }
//            } catch (Exception ex) {
//                throw ex;
//            }
//            return result;
//        } catch (Throwable th) {
//            if (jedis != null) {
//                jedis.close();
//            }
//            throw new RuntimeException(th);
//        }
//    }
//
//    private Map<String, Map<byte[], byte[]>> batchReadMap2nd(List<String> keys) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool2.getResource();
//            pipeline2 = jedis.pipelined();
//            for (String key : keys) {
//                pipeline2.hgetAll(SafeEncoder.encode(key));
//            }
//            List<Object> objs = pipeline2.syncAndReturnAll();
//            jedis.close();
//            Map<String, Map<byte[], byte[]>> result = new HashMap<>();
//            try {
//                for (int i = 0; i < keys.size(); i++) {
//                    result.put(keys.get(i), (Map<byte[], byte[]>) objs.get(i));
//                }
//            } catch (Exception ex) {
//                throw ex;
//            }
//            return result;
//        } catch (Throwable th) {
//            if (jedis != null) {
//                jedis.close();
//            }
//            throw new RuntimeException(th);
//        }
//    }
//
//    private List<Map<String, Object>> handleResult(List<Map<byte[], byte[]>> data, List<Map<String, Object>> resList) {
//        for (int i = 0; i < data.size(); i++) {
//            Map<String, Object> value = toObjectMap(data.get(i));
//            Map<String, Object> resMap = new HashMap<String, Object>();
//            for (Map.Entry<String, Object> entry : value.entrySet()) {
//                String vkey = entry.getKey();
//                Object vvalue = entry.getValue();
//                if (vvalue instanceof HLL) {
//                    HLL lteuserHll = (HLL) vvalue;
//                    resMap.put(vkey, lteuserHll.toBytes());
//                } else {
//                    resMap.put(vkey, vvalue);
//                }
//            }
//            resList.add(resMap);
//        }
//        return resList;
//    }
//
//    private Map<String, Map<String, Object>> handleResultMap(Map<String, Map<byte[], byte[]>> data, Map<String, Map<String, Object>> rMap) {
//        for (Map.Entry<String, Map<byte[], byte[]>> entity : data.entrySet()) {
//            Map<String, Object> value = toObjectMap(entity.getValue());
//            Map<String, Object> resMap = new HashMap<String, Object>();
//            for (Map.Entry<String, Object> entry : value.entrySet()) {
//                String vkey = entry.getKey();
//                Object vvalue = entry.getValue();
//                if (vvalue instanceof HLL) {
//                    HLL lteuserHll = (HLL) vvalue;
//                    resMap.put(vkey, lteuserHll.toBytes());
//                } else {
//                    resMap.put(vkey, vvalue);
//                }
//            }
//            if (resMap.isEmpty()) continue;
//            rMap.put(entity.getKey(), resMap);
//        }
//        return rMap;
//    }
//
//    /**
//     * 为了持久化key的，不定时清除
//     *
//     * @param key
//     * @param values
//     */
//    public void lpushs(String key, List<String> values) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            String[] res = values.toArray(new String[values.size()]);
//            jedis.lpush(key, res);
//            jedisPool.returnResource(jedis);
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    public void lpushs2(String key, List<String> values, int index) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool2.getResource();
//            jedis.select(index);
//            jedis.lpush(key, values.toArray(new String[values.size()]));
//        } catch (JedisException e) {
//            jedisPool2.returnBrokenResource(jedis);
//            throw e;
//        } finally {
//            if (jedis != null) {
//                jedisPool2.returnResource(jedis);
//            }
//        }
//    }
//
//    public void lpushsDelOld(String key, List<String> values) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            long blen = jedis.llen(key);
//            jedis.lpush(key, values.toArray(new String[values.size()]));
//            long alen = jedis.llen(key);
//            jedis.ltrim(key, 0, alen - blen - 1);
//            jedisPool.returnResource(jedis);
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    public void ltrim(String key, long start, long end) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.ltrim(key, start, end);
//            jedisPool.returnResource(jedis);
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    public void del(String key) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.del(key);
//            jedisPool.returnResource(jedis);
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    public long llen(String key) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            long size = jedis.llen(key);
//            jedisPool.returnResource(jedis);
//            return size;
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    public List<String> lrange(String key) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            List<String> arr = jedis.lrange(key, 0, -1);
//            jedisPool.returnResource(jedis);
//            return arr;
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    public List<String> lrange(String key, int start, int end) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            List<String> arr = jedis.lrange(key, start, end);
//            jedisPool.returnResource(jedis);
//            return arr;
//        } catch (JedisException e) {
//            jedisPool.returnBrokenResource(jedis);
//            throw e;
//        }
//    }
//
//    public void set2nd(String key, String data) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.set(key, data);
//            jedis.expire(key, expireSec);
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }
//
//    public void hset2nd(String key, String field, String value) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.hset(key, field, value);
//            jedis.expire(key, expireSec);
//        } catch (JedisException e) {
//            throw e;
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }
//
//    public String hget2nd(String key, String field) {
//        Jedis jedis = null;
//        String value = null;
//        try {
//            jedis = jedisPool.getResource();
//            value = jedis.hget(key, field);
//        } catch (JedisException e) {
//            throw e;
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//        return value;
//    }
//
//    public void setKeyExpire(String key, int expireSec) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.expire(key, expireSec);
//        } catch (JedisException e) {
//            throw e;
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }
//
//    /**
//     * @param
//     * @return 长整型
//     * @方法功能 字节数组和长整型的转换
//     */
//    public static byte[] longToByte(long number) {
//        long temp = number;
//        byte[] b = new byte[8];
//        for (int i = 0; i < b.length; i++) {
//            b[i] = new Long(temp & 0xff).byteValue();
//            // 将最低位保存在最低位
//            temp = temp >> 8;
//            // 向右移8位
//        }
//        return b;
//    }
//
//    /**
//     * 28小时清
//     *
//     * @param key
//     * @param hash
//     */
//    public void hmset(String key, Map<String, String> hash) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            if (hash == null || hash.isEmpty()) {
//                return;
//            }
//            jedis.hmset(key, hash);
//            jedis.expire(key, expireSec);
//            if (needPersist) {
//                jedis.lpush(DB_PERSIST_QUEUE, key);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }
//
//    static long TOTAL_FLOW = 0;
//    static HLL hll = HLLUtils.create();
//
//    public static void printAllData(List<Map<String, Object>> list) {
//        for (Map<String, Object> map : list) {
//            for (Map.Entry<String, Object> m : map.entrySet()) {
//                Object v = m.getValue();
//                if (v instanceof byte[]) {
////                    v = HLL.fromBytes((byte[]) v).cardinality();
//                    System.out.println("k = " + m.getKey() +" *********HLL v = " + HLL.fromBytes((byte[]) v).cardinality());
////                    if ("USER_CNT".equals(m.getKey())) {
////                        hll = HLLUtils.union(hll, HLL.fromBytes((byte[]) v));
////
////                    }
////                    System.out.println("k = " + m.getKey() + " v = " + String.valueOf(v));
//
//                }else{
//                    System.out.println("k = " + m.getKey() + " v = " + String.valueOf(v));
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        //117.187.18.30@6427:stream!23$
//        String host = "10.243.64.39";
//        host = "10.198.131.15";
//        host = "10.243.72.62";
//        host = "10.221.170.70";
////        host = "10.243.73.64";
//        int port = 16379;
////        port = 16427;
////        port = 16379;
//        port = 16401;
//        String pwd = "eastcom!@#$";
//        pwd = "stream!23$";
//        pwd = "";
//        RedisProxy redisProxy = new RedisProxy();
//        redisProxy.init(host, port, pwd, 10001);//初始化
////        List list = new ArrayList();
////        list.add("B5A000:呼和浩特市:1810241605");
//
////        List<Map<String, Object>> result = redisProxy.hgetAll(list);
////        System.out.println(result.size());
////        printAllData(result);
//
//
//
////        System.out.println(hll.cardinality());
//        List<String> l =   redisProxy.lrange("T5AM00:呼伦贝尔市:VO_LTE_SUCC_RATE:100:1810251305");
//        for(String v : l){
//            System.out.println(v);
//        }
//
//
//
////        Map<String,String> m = new HashMap<String,String>();
////        m.put("2","2");
////        redisProxy.hmset("a",m);
////        LinkedHashMap<String,Map<String,Object>> data = new LinkedHashMap<String,Map<String,Object>>();
////        Map<String,Object> index = new HashMap<String,Object>();
////        Long l = 0l;
////        index.put("USER_CNT", HLLUtils.hll(longToByte(l)));
////        data.put("_RMUA:CITY:青海:徽州区:170927:_RMUA_FLAG_MIN",index);
////
////        Map<String,Object> index2 = new HashMap<String,Object>();
////        index.put("USER_CNT", HLLUtils.hll(longToByte(2l)));
////        data.put("_RMUA:CITY:北京:庐江:170927:_RMUA_FLAG_MIN",index);
////        redisProxy.hmset4ObjMergeOld(data);
//
//        //        List res = redisProxy.lrange("_RMUA");
////        List<String> l = redisProxy.lrange("_RMUA");
////        for (String v:l ) {
////            System.out.println("value == "+v);
////        }
////        for(Map<String,Object> map :result){
////            System.out.println(map.keySet());
////            System.out.println(map.get("USER_CNT"));
////
////        }
////        System.out.println(HLLUtils.longValue((byte [])(result.get(0).get("USER_CNT"))));
////        long start = System.currentTimeMillis();
////        System.out.println("start "+start);
////        redisProxy.lpushs("mlf",list);
//        long end = System.currentTimeMillis();
//
////        System.out.println("use time "+(end- start));
//
////        redisProxy.lpushs("_RMUA",Arrays.asList("1", "2"));
////        List l = Arrays.asList("_IDCR:高速公路:济祁高速淮南段安丰收费站:1708221025");
////
////        HLLUtils hllUtils = new HLLUtils();
////        List<Map<String, Object>> list = redisProxy.hgetAll(l);
////        Map<String,Object> map = list.get(0);
//////        byte[] bytes = (byte[]) map.get("USER_CNT");
////        byte[] bytes = SafeEncoder.encode("4");
////        byte[] bytes1 = SafeEncoder.encode("2");
////        System.out.println(new String(bytes));
////        long l1 = Long.parseLong(new String(bytes));
////        long l2 = Long.parseLong(new String(bytes1));
////        System.out.println(l1+l2);
////   Map map = (Map) list.get(0);
////        HLL hll =  HLL.fromBytes((byte[])map.get("TM_CNT"));
////        System.out.println(hll.cardinality());
//
//
////        try {
////            String path = "D:\\testFile\\";
////            // 2、保存到临时文件
////            // 1K的数据缓冲
////            byte[] bs = new byte[1024];
////            // 读取到的数据长度
////            int len;
////            // 输出的文件流保存到本地文件
////
////            File tempFile = new File(path);
////            if (!tempFile.exists()) {
////                tempFile.mkdirs();
////            }
////            // 开始读取
////            String mainKey = "AMQUE_RECORD2";
////            long size = redisProxy.llen(mainKey);
////            PrintWriter pw = new PrintWriter(new FileWriter(tempFile.getPath() + File.separator + "AMQUE_RECORD2.txt"));
////            int times = 0;
////            for (int i = 0; i < size; i += 5000) {
////                int start = (i == 0 ? 0 : i + 1);
////                List<String> resList = redisProxy.lrange(mainKey, start, start + 5000);
////                for (String v : resList) {
////                    if (v.contains("XUC-绩溪燎原厂（岭里）-FNL-158")) {
////                        pw.write(v);
////                        pw.close();
////                        System.exit(1);
////                    }
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        Map map1 = new HashMap();
////        Map map2 = new HashMap();
////        map2.put("key","value");
////        map1.put("YWAA:17032210:hoursum:test","");
////        map1.put("s","");
////        map.put("ss",map1);
////        redisProxy.hmset4Obj2nd(map);
////        String prefixKey = "CL1B";
////        List<String> l = redisProxy.lrange(prefixKey);
////        for (String v:l ) {
////            System.out.println("value == "+v);
////        }
////
////        redisProxy.hmset4Obj(map);
////        StringUtils.isNotBlank("ss");
//
//
////        List<Map<String, Object>>  l = redisProxy.hgetAll(Arrays.asList("_RMUA"));
////        System.out.println(l.size());
////        for(Map<String,Object> map :l){
////
////            System.out.println(MapUtil.mapObj2Str(map));
////        }
////        List ll = redisProxy.lrange("sss");
////        System.out.println(ll.size());
////        for(int i =0 ;i<ll.size();i++){
////            System.out.println("key=="+ll.get(i));
////        }
////        String key = "";
////        List keyList = new ArrayList();
////        keyList.add(key);
////        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
////        String time=sdf.format(new Date());
////        Calendar cd = Calendar.getInstance();
////        try {
////            cd.setTime(sdf.parse(time));
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        cd.add(Calendar.DATE, -1);
////        Date date=cd.getTime();
////       sdf.format(date);
//    }
//}
