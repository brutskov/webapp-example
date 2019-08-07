package com.solvd.webappsimple;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;

@HandlesTypes({
        WebApplicationInitializer.class
})
public class ContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        set.forEach(initializedClass -> {
            if (WebApplicationInitializer.class.isAssignableFrom(initializedClass)) {
                WebApplicationInitializer initializer = createNewInitializerInstance(initializedClass);
                initializer.onServletContainerStart(servletContext);
            }
        });
    }

    private WebApplicationInitializer createNewInitializerInstance(Class<?> initializerClass) {
        WebApplicationInitializer instance = null;
        try {
            instance = (WebApplicationInitializer) initializerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

}
