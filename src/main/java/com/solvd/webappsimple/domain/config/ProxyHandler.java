package com.solvd.webappsimple.domain.config;

import java.lang.reflect.InvocationHandler;

public interface ProxyHandler<T> extends InvocationHandler {

    void setObject(T t);

}
