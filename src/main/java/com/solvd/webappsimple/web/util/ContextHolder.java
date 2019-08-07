package com.solvd.webappsimple.web.util;

import javax.servlet.ServletContext;

public class ContextHolder {

    private static ContextHolder contextHolder;
    private ServletContext servletContext;

    private ContextHolder() {
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public static synchronized ContextHolder createInstance(ServletContext servletContext) {
        if (contextHolder == null) {
            contextHolder = new ContextHolder();
            contextHolder.servletContext = servletContext;
        }
        return contextHolder;
    }

}
