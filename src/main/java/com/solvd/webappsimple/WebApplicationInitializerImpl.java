package com.solvd.webappsimple;

import com.solvd.webappsimple.web.security.filter.DelegatingFilter;
import com.solvd.webappsimple.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebApplicationInitializerImpl implements WebApplicationInitializer {

    @Override
    public void onServletContainerStart(ServletContext servletContext) {
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet());
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        servletContext.addFilter("securityFilterChain", new DelegatingFilter()).addMappingForUrlPatterns(null, true, "/*");
    }

}
