package com.solvd.webappsimple.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.webappsimple.web.annotation.PathVariable;
import com.solvd.webappsimple.web.annotation.RequestBody;
import com.solvd.webappsimple.web.exception.ExceptionResolver;
import com.solvd.webappsimple.web.util.HttpMethodElement;
import com.solvd.webappsimple.web.util.ResponseHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;

import static com.solvd.webappsimple.web.util.RequestHelper.getRequestPathParameterMatcher;
import static com.solvd.webappsimple.web.util.RequestHelper.getRequestRelativePath;

public class DispatcherServlet extends HttpServlet {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMethod(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMethod(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMethod(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMethod(req, resp);
    }

    @SuppressWarnings("unchecked")
    private void doMethod(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Object controllersAttribute = getServletContext().getAttribute("controllers");
            List<HttpMethodElement> httpMethodElements = new ArrayList<>((Collection<HttpMethodElement>) controllersAttribute);
            HttpMethodElement httpMethodElement = httpMethodElements.stream()
                                                   .filter(hme -> getRequestRelativePath(req).equals(hme.getPath()) && req.getMethod().equals(hme.getHttpMethod().name()))
                                                   .findFirst()
                                                   .orElse(null);
            if (httpMethodElement == null) {
                httpMethodElement = httpMethodElements.stream()
                                                      .filter(hme -> match(req, hme) && req.getMethod().equals(hme.getHttpMethod().name()))
                                                      .findFirst()
                                                      .orElseThrow(() -> new RuntimeException("Requested path is not supported"));
            }
            dispatch(httpMethodElement, req, resp);
        } catch (Throwable e) {
            ExceptionResolver.handle(e, resp);
            e.printStackTrace();
        }
    }

    private void dispatch(HttpMethodElement httpMethodElement, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Object> arguments = new ArrayList<>();
        Arrays.stream(httpMethodElement.getMethod().getParameters()).forEach(parameter -> {
            if (HttpServletRequest.class.isAssignableFrom(parameter.getType())) {
                arguments.add(req);
            } else if (HttpServletResponse.class.isAssignableFrom(parameter.getType())) {
                arguments.add(resp);
            } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                try {
                    Object body = mapper.readValue(req.getReader(), parameter.getType());
                    arguments.add(body);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (parameter.isAnnotationPresent(PathVariable.class)) {
                List<AbstractMap.SimpleEntry<String, String>> pathVariableEntries = getPathVariableEntry(req, httpMethodElement);
                AbstractMap.SimpleEntry<String, String> pathEntry = pathVariableEntries.stream()
                                   .filter(entry -> entry.getKey().equals(parameter.getAnnotation(PathVariable.class).value()))
                                   .findFirst()
                                   .orElse(new AbstractMap.SimpleEntry<>(null, null));
                Object value = pathEntry.getValue();
                if (pathEntry.getValue() != null) {
                    if (Long.class.isAssignableFrom(parameter.getType())) {
                        value = Long.valueOf(pathEntry.getValue());
                    }
                }
                arguments.add(value);
            } else {
                arguments.add(null);
            }
        });
        Object result = httpMethodElement.getMethod().invoke(httpMethodElement.getController(), arguments.toArray());
        ResponseHelper.writeResponseObject(resp, mapper.writeValueAsString(result));
    }

    private boolean match(HttpServletRequest req, HttpMethodElement httpMethodElement) {
        String relativePath = getRequestRelativePath(req);
        return relativePath.matches(httpMethodElement.getPathPattern());
    }

    private List<AbstractMap.SimpleEntry<String, String>> getPathVariableEntry(HttpServletRequest req, HttpMethodElement httpMethodElement) {
        List<AbstractMap.SimpleEntry<String, String>> result = new ArrayList<>();
        String relativePath = getRequestRelativePath(req);
        Matcher requestTemplateMatcher = getRequestPathParameterMatcher(httpMethodElement.getPath());
        while (requestTemplateMatcher.find()) {
            String pathVariableTemplate = requestTemplateMatcher.group();
            String mainSlice = httpMethodElement.getPath().substring(0, httpMethodElement.getPath().indexOf(pathVariableTemplate));
            String endSlice = httpMethodElement.getPath().substring(httpMethodElement.getPath().indexOf(pathVariableTemplate) + pathVariableTemplate.length());
            String value = endSlice.length() != 0 ? relativePath.substring(mainSlice.length(), relativePath.indexOf(endSlice)) : relativePath.substring(mainSlice.length());
            result.add(new AbstractMap.SimpleEntry<>(pathVariableTemplate.substring(1, pathVariableTemplate.length() - 1), value));
        }
        return result;
    }

}
