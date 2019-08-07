package com.solvd.webappsimple;

import com.solvd.webappsimple.web.security.InternalFilter;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@HandlesTypes({
        WebApplicationInitializer.class,
        InternalFilter.class,
})
public class ContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        List<InternalFilter> filters = new ArrayList<>();
        set.forEach(initializedClass -> {
            if (WebApplicationInitializer.class.isAssignableFrom(initializedClass)) {
                WebApplicationInitializer initializer = createNewInitializerInstance(initializedClass);
                initializer.onServletContainerStart(servletContext);
            }
            if (InternalFilter.class.isAssignableFrom(initializedClass) && !Modifier.isAbstract(initializedClass.getModifiers())) {
                try {
                    InternalFilter filter = (InternalFilter) initializedClass.newInstance();
                    filters.add(filter);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        if (filters.size() != 0) {
            servletContext.setAttribute("filters", filters);
        }
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
