package com.solvd.webappsimple.web.security.filter;

import com.solvd.webappsimple.domain.User;
import com.solvd.webappsimple.service.UserService;
import com.solvd.webappsimple.web.controller.BaseController;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.solvd.webappsimple.web.util.RequestHelper.buildRegex;
import static com.solvd.webappsimple.web.util.RequestHelper.getRequestRelativePath;

public class AuthFilter extends InternalFilter {

    private static final String[] PUBLIC_PATHS = {
            "/auth/**"
    };

    private static final String[] AUTH_PATHS = {
            "/users/**",
            "/events/**"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = getRequestRelativePath(req);
        boolean isAuthEndpoint = needAuth(path);
        if (isAuthEndpoint) {
            String authToken = req.getHeader("Authorization");
            if (authToken != null && authToken.length() != 0) {
                String username = authToken.split(" ")[0];
                String token = authToken.split(" ")[1];
                UserService userService = BaseController.getService(UserService.class);
                User user = userService.getByUsername(username);
                if (user == null || !token.equals(user.getSessionId()) || LocalDateTime.now().isAfter(user.getSessionExpiredIn())) {
                    throw new RuntimeException("Forbidden exception");
                }

                req.setAttribute("principalId", user.getId());
                chain.doFilter(request, response);
            } else {
                throw new RuntimeException("Forbidden exception");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public int order() {
        return 2;
    }

    private boolean needAuth(String path) {
        return Arrays.stream(AUTH_PATHS).anyMatch(authPath -> path.matches(buildRegex(authPath)));
    }

}
