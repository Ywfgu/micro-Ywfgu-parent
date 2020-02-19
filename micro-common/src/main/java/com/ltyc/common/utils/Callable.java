package com.ltyc.common.utils;

import java.io.IOException;

public interface  Callable<T> {

    public T call(Object ...args) throws IOException;
}