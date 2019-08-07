package com.solvd.webappsimple.web.util;

import com.solvd.webappsimple.domain.config.ProxyHandler;

import java.lang.reflect.Proxy;

public class ProxyHelper {

    public static <T> T createProxy(Class<? extends T> proxyClass, Class<? extends ProxyHandler> proxyHandlerClass) {
        T t = createInstance(proxyClass);
        ProxyHandler proxyHandler = createInstance(proxyHandlerClass);
        proxyHandler.setObject(t);
        return (T) Proxy.newProxyInstance(proxyClass.getClassLoader(),
                new Class[]{proxyClass.getInterfaces()[0]}, proxyHandler);
    }

    private static <T> T createInstance(Class<? extends T> instanceClass) {
        T t = null;
        try {
            t = instanceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

}
