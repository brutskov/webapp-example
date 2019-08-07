package com.solvd.webappsimple.web.util;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHelper {

    public static String getRequestRelativePath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    public static String buildRegex(String pathWildcard) {
        String basePath = pathWildcard.split("/")[1];
        return String.format("^(\\/%s|\\/%s\\/.*)$", basePath, basePath);
    }

    public static Matcher getRequestPathParameterMatcher(String path) {
        return getMatcher("\\{(.*?)}", path);
    }

    private static Matcher getMatcher(String regex, String testText) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(testText);
    }

}
