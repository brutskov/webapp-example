package com.solvd.webappsimple.web.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class AuthFilter extends InternalFilter {

    private static final String[] PUBLIC_PATHS = {

    };

    private static final String[] AUTH_PATHS = {

    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public int order() {
        return 2;
    }

}
