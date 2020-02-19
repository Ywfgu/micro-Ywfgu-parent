package com.ltyc.sms.connect.manager;

import io.netty.util.concurrent.Future;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public interface ExitUnlimitCirclePolicy<T> {
    boolean notOver(Future<T> future);
}
