package com.ltyc.sms.common.utils;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public enum CachedMillisecondClock {
    INS;
    private volatile long now = 0;// 当前时间

    private CachedMillisecondClock() {
        this.now = System.currentTimeMillis();
        start();
    }

    private void start() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    now = System.currentTimeMillis();
                }
            }
        },"CachedMillisecondClockUpdater");
        t.setDaemon(true);
        t.start();
    }

    public long now() {
        return now;
    }

}
