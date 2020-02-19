package com.ltyc.sms.common.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class DefaultSequenceNumberUtil {

    public static long getSequenceNo() {
        return getNextAtomicValue(sequenceId,Limited);
    }

    /**
     *实现AtomicLong对象的线程安全循环自增
     *@param  atomicObj
     *需要自增的Atomic对象
     *@param limited
     *最大值
     */
    public static long getNextAtomicValue(AtomicLong atomicObj, long limited){
        long ret = atomicObj.getAndIncrement();

        if (ret > limited) { // Long.MAX_VALUE - 0xfff
            synchronized (atomicObj) {
                //双重判断，只能有一个线程更新值
                if (atomicObj.get() > limited) {
                    atomicObj.set(0);
                    return 0;
                } else {
                    return atomicObj.getAndIncrement();
                }
            }
        } else {
            return ret;
        }
    }

    private final static long Limited = 0x7fffffffffff0000L;
    private final static AtomicLong sequenceId = new AtomicLong(Math.abs(RandomUtils.nextInt()));
}
