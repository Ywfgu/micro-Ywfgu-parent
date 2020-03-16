package com.ltyc.sms.connect.manager;


import com.ltyc.sms.connect.manager.EndpointManagerInterface;
import com.ltyc.sms.connect.manager.EventLoopGroupFactory;
import com.ltyc.sms.connect.manager.ExitUnlimitCirclePolicy;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author guht
 * @version 1.0
 * @Description 系统连接的统一管理器，负责连接服务端，或者开启监听端口，等客户端连接
 * @create 2020/2/5
 */
public enum EndpointManager implements EndpointManagerInterface {
    INS;
    private static final Logger logger = LoggerFactory.getLogger(EndpointManager.class);

    private Set<EndpointEntity> endpoints = Collections.synchronizedSet(new HashSet<EndpointEntity>());

    private ConcurrentHashMap<String, EndpointEntity> idMap = new ConcurrentHashMap<String, EndpointEntity>();

    private ConcurrentHashMap<String, EndpointConnector<?>> map = new ConcurrentHashMap<String, EndpointConnector<?>>();

    private volatile boolean started = false;

    @Override
    public synchronized void openEndpoint(EndpointEntity entity) {
        if (!entity.isValid()) {
            return;
        }

        EndpointEntity old = idMap.get(entity.getId());
        if (old == null) {
            addEndpointEntity(entity);
        }

        EndpointConnector<?> conn = map.get(entity.getId());
        if (conn == null){
            //在这里将对象和连接器进行关联
            conn = entity.buildConnector();
            map.put(entity.getId(), conn);
        }

        try {
            conn.open();
        } catch (Exception e) {
            logger.error("Open Endpoint Error. {}", entity, e);
        }
    }

    @Override
    public synchronized void close(EndpointEntity entity) {
        EndpointConnector<?> conn = map.get(entity.getId());
        if (conn == null) {
            return;
        }
        try {
            conn.close();
            // 关闭所有连接，并把Connector删掉
            map.remove(entity.getId());

        } catch (Exception e) {
            logger.error("close Error", e);
        }
    }

    @Override
    public EndpointConnector<?> getEndpointConnector(EndpointEntity entity) {
        return map.get(entity.getId());
    }

    @Override
    public EndpointConnector<?> getEndpointConnector(String entityId) {
        return map.get(entityId);
    }

    @Override
    public EndpointEntity getEndpointEntity(String id) {
        return idMap.get(id);
    }

    @Override
    public void openAll() throws Exception {
        for (EndpointEntity e : endpoints) {
            openEndpoint(e);
        }
    }

    @Override
    public synchronized void addEndpointEntity(EndpointEntity entity) {
        endpoints.add(entity);
        idMap.put(entity.getId(), entity);
    }

    @Override
    public void addAllEndpointEntity(List<EndpointEntity> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        for (EndpointEntity entity : entities) {
            if (entity.isValid()) {
                addEndpointEntity(entity);
            }
        }
    }

    @Override
    public Set<EndpointEntity> allEndPointEntity() {
        return endpoints;
    }

    @Override
    public synchronized void remove(String id) {
        EndpointEntity entity = idMap.remove(id);
        if (entity != null) {
            endpoints.remove(entity);
            close(entity);
        }
    }

    @Override
    public void close() {
        for (EndpointEntity en : endpoints) {
            close(en);
        }
    }

    @Override
    public void stopConnectionCheckTask(){
        started = false;
    }
    @Override
    public void startConnectionCheckTask(){
        if(started) {
            return;
        }

        started = true;
        //每秒检查一次所有连接，不足数目的就新建一个连接
        EventLoopGroupFactory.INS.submitUnlimitCircleTask(new Callable<Boolean>(){

            @Override
            public Boolean call() throws Exception {
                for(Map.Entry<String, EndpointConnector<?>> entry: map.entrySet()){
                    EndpointConnector conn = entry.getValue();
                    EndpointEntity entity = conn.getEndpointEntity();
                    int max = entity.getMaxChannels();
                    int actual = conn.getConnectionNum();

                    //客户端重连
                    if(entity instanceof ClientEndpoint && actual < max){
                        logger.debug("open connection {}",entity);
                        conn.open();
                    }
                }
                return started;
            }

        }, new ExitUnlimitCirclePolicy<Boolean>(){

            @Override
            public boolean notOver(Future<Boolean> future) {
                return started;
            }

        }, 1000);
    }
}
