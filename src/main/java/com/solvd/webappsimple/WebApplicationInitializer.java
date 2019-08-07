package com.solvd.webappsimple;

import javax.servlet.ServletContext;

public interface WebApplicationInitializer {

    void onServletContainerStart(ServletContext servletContext);

}
