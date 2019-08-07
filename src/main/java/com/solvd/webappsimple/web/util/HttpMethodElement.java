package com.solvd.webappsimple.web.util;

import com.solvd.webappsimple.web.BaseController;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

import static com.solvd.webappsimple.web.util.RequestHelper.getRequestPathParameterMatcher;

public class HttpMethodElement<T extends BaseController> {

    private final Method method;
    private final String path;
    private final HttpMethod httpMethod;
    private final T controller;
    private final String pathPattern;

    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    public HttpMethodElement(Method method, String path, HttpMethod httpMethod, T controller) {
        this.method = method;
        this.path = path;
        this.httpMethod = httpMethod;
        this.controller = controller;

        this.pathPattern = buildPathRegex();
    }

    public Method getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public T getController() {
        return controller;
    }

    public String getPathPattern() {
        return pathPattern;
    }

    private String buildPathRegex() {
        Matcher matcher = getRequestPathParameterMatcher(path);
        String regex = path.replaceAll("/", "\\\\/");
        while (matcher.find()) {
            regex = regex.replace(matcher.group(), "(.*?)");
        }
        return String.format("^(%s)$", regex);
    }

}
