package com.solvd.webappsimple.web.controller;

import com.solvd.webappsimple.service.Service;
import com.solvd.webappsimple.web.util.ContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseController {

    //private final String path;
    //private final String pathRegex;

    /*public BaseController(String path) {
        this.path = path;
        this.pathRegex = buildRegex(path);
    }*/

    public void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    }

    public void post(HttpServletRequest req, HttpServletResponse resp) {
    }

    public void put(HttpServletRequest req, HttpServletResponse resp) {
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp) {
    }

//    public String getPath() {
//        return path;
//    }
//
//    public String getPathRegex() {
//        return pathRegex;
//    }

    private static ContextHolder context() {
        return ContextHolder.createInstance(null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Service> T getService(Class<T> serviceClass) {
        List<Service> services = new ArrayList<>((Collection<Service>) context().getServletContext().getAttribute("services"));
        return (T) services.stream().filter(service -> serviceClass.isAssignableFrom(service.getClass())).findFirst().orElse(null);
    }

}
