package com.ltyc.common.redis;

import com.ltyc.common.utils.HLLUtils;
import net.agkn.hll.HLL;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;
import redis.clients.util.SafeEncoder;

import java.util.*;


/**
 * @author ltyc
 * @version 1.0
 * @Description redis代理类
 * @create 18.1.24
 */
public class RedisProxy {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private Pool<Jedis> jedisPool;
    private Pipeline pipeline;
    private int expireSec = 100800;// redis2 key expire seconds 28小时
    private int maxBatchSize = 1000;
    private HLLUtils hllUtils = new HLLUtils();

    private void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }

    public void init(String host, int port, String pwd, int maxBatchSize) {
        GenericObjectPoolConfig gop = new GenericObjectPoolConfig();
        gop.setMaxTotal(8);
        gop.setMaxIdle(8);
        gop.setTestOnBorrow(true);
        setMaxBatchSize(maxBatchSize);
        if (StringUtils.isBlank(pwd)) {
            jedisPool = new JedisPool(gop, host, port, 10000);
        } else {
            jedisPool = new JedisPool(gop, host, port, 10000, pwd);
        }
    }

    public void hmset4Obj(Map<String, Map<String, Object>> map) {
        Map<String, Map<String, HLL>> hllMap = new HashMap<String, Map<String, HLL>>();
        Map<String, Map<String, String>> sMap = new HashMap<String, Map<String, String>>();
        for (Map.Entry<String, Map<String, Object>> mm : map.entrySet()) {
            Map<String, HLL> mh = new HashMap<String, HLL>();
            Map<String, String> ms = new HashMap<String, String>();
            for (Map.Entry<String, Object> m : mm.getValue().entrySet()) {
                if (m.getValue() instanceof HLL) {
                    mh.put(m.getKey(), (HLL) m.getValue());
                } else {
                    ms.put(m.getKey(), String.valueOf(m.getValue()));
                }
            }
            hllMap.put(mm.getKey(), mh);
            sMap.put(mm.getKey(), ms);
        }
        if (!sMap.isEmpty()) {
            hmset(sMap);
        }
        if (!hllMap.isEmpty()) {
            hmsetBytes(formatHllMap(hllMap));
        }
    }

    private void hmset(Map<String, Map<String, String>> hashs) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            for (String key : hashs.keySet()) {

                Map<String, String> hash = hashs.get(key);
                if (hash == null || hash.isEmpty()) {
                    continue;
                }
                pipeline.hmset(key, hash);
                pipeline.expire(key, expireSec);
            }
            pipeline.sync();
            jedisPool.returnResource(jedis);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            throw e;
        }
    }

    private void hmsetBytes(Map<byte[], Map<byte[], byte[]>> hashs) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pipeline = jedis.pipelined();

            for (byte[] key : hashs.keySet()) {
                Map<byte[], byte[]> hash = hashs.get(key);
                if (hash == null || hash.isEmpty()) {
                    continue;
                }
                pipeline.hmset(key, hash);
                pipeline.expire(key, expireSec);
            }

            pipeline.sync();
            jedisPool.returnResource(jedis);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            throw e;
        }
    }

    private Map<byte[], Map<byte[], byte[]>> formatHllMap(Map<String, Map<String, HLL>> map) {
        Map<byte[], Map<byte[], byte[]>> result = new HashMap<byte[], Map<byte[], byte[]>>();
        for (Map.Entry<String, Map<String, HLL>> d : map.entrySet()) {
            result.put(SafeEncoder.encode(d.getKey()), toByteMap(d.getValue()));
        }
        return result;
    }

    private Map<byte[], byte[]> toByteMap(Map<String, HLL> hllMap) {
        if (hllMap == null || hllMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<byte[], byte[]> result = new HashMap<byte[], byte[]>();
        for (String key : hllMap.keySet()) {
            HLL hll = hllMap.get(key);
            if (hll == null) {
                hll = hllUtils.create();
            }
            result.put(SafeEncoder.encode(key), hll.toBytes());
        }
        return result;
    }

    public List<Map<String, Object>> hgetAll(List<String> keys) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (keys.size() == 0) {
            return new ArrayList<Map<String, Object>>();
        }
        if (keys.size() <= maxBatchSize) {
            List<Map<byte[], byte[]>> data = batchReadByte(keys);
            return handleResult(data, result);
        } else {
            List<List<String>> keysList = fixedSize(keys,
                    maxBatchSize);
            for (List<String> ks : keysList) {
                List<Map<byte[], byte[]>> data = batchReadByte(ks);
                handleResult(data, result);
            }
            return result;
        }
    }

    public <T> List<List<T>> fixedSize(List<T> list, int size) {
        List<List<T>> result = new ArrayList<List<T>>();
        int index = 0;
        while (index + size <= list.size()) {
            result.add(list.subList(index, index + size));
            index += size;
        }
        if (index < list.size()) {
            result.add(list.subList(index, list.size()));
        }
        return result;
    }

    private List<Map<String, Object>> handleResult(List<Map<byte[], byte[]>> data, List<Map<String, Object>> resList) {
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> value = toObjectMap(data.get(i));
            Map<String, Object> resMap = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : value.entrySet()) {
                String vkey = entry.getKey();
                Object vvalue = entry.getValue();
                if (vvalue instanceof HLL) {
                    HLL lteuserHll = (HLL) vvalue;
                    resMap.put(vkey, lteuserHll.toBytes());
                } else {
                    resMap.put(vkey, vvalue);
                }
            }
            resList.add(resMap);
        }
        return resList;
    }

    private Map<String, Object> toObjectMap(Map<byte[], byte[]> info) {
        if (info == null || info.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        for (byte[] fieldByte : info.keySet()) {
            String field = SafeEncoder.encode(fieldByte);
//            if (field.endsWith(":n") || field.endsWith(":" + DbKeys.value) || field.endsWith(":" + DbKeys.top)
//                    || field.endsWith(":" + DbKeys.topall) || field.endsWith(":" + DbKeys.valueall)
//                    || field.endsWith(":" + DbKeys.topday) || field.endsWith(":" + DbKeys.time)
//                    || field.endsWith(":tophour")) {
//                continue;
//            } else {
            byte[] byteValue = info.get(fieldByte);
            String s = SafeEncoder.encode(byteValue);
            if (!s.matches("-?[0-9]+.?[0-9]+|-?[0-9]+")) {
                result.put(field, hllUtils.hll(byteValue));
            } else {
                result.put(field, s);
            }
//            }
        }
        return result;
    }

    private List<Map<byte[], byte[]>> batchReadByte(List<String> keys) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            pipeline = jedis.pipelined();
            for (String key : keys) {
                pipeline.hgetAll(SafeEncoder.encode(key));
            }
            List<Object> objs = pipeline.syncAndReturnAll();
            jedisPool.returnResource(jedis);
            List<Map<byte[], byte[]>> result = new ArrayList<Map<byte[], byte[]>>();
            try {
                for (int i = 0; i < keys.size(); i++) {
                    result.add((Map<byte[], byte[]>) objs.get(i));
                }
            } catch (Exception ex) {
                throw ex;
            }
            return result;
        } catch (Throwable th) {
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            throw new RuntimeException(th);
        }
    }

    public void pfadd(String key,String[] Elements){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            if (Elements == null || Elements.length == 0) {
               return;
            }
            pipeline.pfadd(key, Elements);
            pipeline.expire(key, expireSec);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void pfadds(Map<String, String[]> hashs){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            for (String key : hashs.keySet()) {
                String[] arr = hashs.get(key);
                if (arr == null || arr.length == 0 ) {
                    continue;
                }
                pipeline.pfadd(key, arr);
                pipeline.expire(key, expireSec);
            }
            pipeline.sync();
            jedisPool.returnResource(jedis);
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            throw e;
        }
    }

    public Long pfcount(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long l = jedis.pfcount(key);
            jedisPool.returnResource(jedis);
            return l;
        } catch (JedisException e) {
            jedisPool.returnBrokenResource(jedis);
            throw e;
        }
    }


    public static void main(String[] args) {
        RedisProxy redisProxy = new RedisProxy();
        redisProxy.init("10.221.193.2", 6401, "", 10001);
//        List ll = redisProxy.lrange("R5R000");
//        String s ="";
//        for(int i=0;i<ll.size();i++){
//            s = ll.get(i).toString();
//            System.out.println(s);
//
//        }


//        System.out.println("111111111111");
//        RedisProxy redisProxy = new RedisProxy();
////        redisProxy.init("10.8.132.230", 6381, "", 10001);//初始化
//        redisProxy.init("10.8.132.162", 6480, "", 10001);//初始化
//
//        Map<String, String[]> hashs = new HashMap<String,String[]>();
//        String[] c1 = {"a","d"};
//        String[] c2 = {"a","b","d"};
//
//        hashs.put("c1",c1);
//        hashs.put("c2",c2);
//
//        redisProxy.pfadds(hashs);


//        String[] c = {"a","b","c"};
//        String[] c = {"a","d","e"};
//        redisProxy.pfadd("c",c);
//        System.out.println("c的去重数量为："+redisProxy.pfcount("c"));

//        LinkedHashMap result = new LinkedHashMap();
//        Map innerMap = new HashMap();
//        HLL[] hll = new HLL[]{HLLUtils.hll("a".getBytes()),HLLUtils.hll("b".getBytes()),HLLUtils.hll("c".getBytes())};
//        innerMap.put("usercnt",HLLUtils.union(hll));
//        result.put("c2",innerMap);
//        redisProxy.hmset4Obj(result);

//        List<String> keys = new ArrayList<String>();
//        keys.add("c2");
//        List<Map<String, Object>> rs = redisProxy.hgetAll(keys);
//        System.out.println(rs);
//        Object v = rs.get(0).get("usercnt");
//
//        HLL hll = HLLUtils.hll((byte[])v);
//        System.out.println(hll.cardinality());
    }
}
