package com.ltyc.sms.common.storedMap;

import java.io.Serializable;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class VersionObject<T extends Serializable> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6721353360960222853L;
    private T obj;
    private long version;
    public VersionObject(T obj){
        this.obj = obj;
        this.version = System.currentTimeMillis();
    }

    public T getObj() {
        return obj;
    }
    public long getVersion() {
        return version;
    }

}
