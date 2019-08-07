package com.solvd.webappsimple;

import com.solvd.webappsimple.service.Service;
import com.solvd.webappsimple.service.util.TransactionHandler;
import com.solvd.webappsimple.web.security.InternalFilter;
import com.solvd.webappsimple.web.util.ProxyHelper;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@HandlesTypes({
        Service.class,
        WebApplicationInitializer.class,
        InternalFilter.class,
})
public class ContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        List<InternalFilter> filters = new ArrayList<>();
        List<Service> services = new ArrayList<>();
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

            if (Service.class.isAssignableFrom(initializedClass) && !Modifier.isAbstract(initializedClass.getModifiers())) {
                @SuppressWarnings("unchecked")
                Service service = ProxyHelper.createProxy((Class<Service>) initializedClass, TransactionHandler.class);
                services.add(service);
            }
        });

        if (filters.size() != 0) {
            servletContext.setAttribute("filters", filters);
        }

        if (services.size() != 0) {
            servletContext.setAttribute("services", services);
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
