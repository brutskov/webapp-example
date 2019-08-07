package com.solvd.webappsimple;

import com.solvd.webappsimple.service.Service;
import com.solvd.webappsimple.service.util.TransactionHandler;
import com.solvd.webappsimple.web.annotation.DeleteMapping;
import com.solvd.webappsimple.web.annotation.GetMapping;
import com.solvd.webappsimple.web.annotation.PostMapping;
import com.solvd.webappsimple.web.annotation.PutMapping;
import com.solvd.webappsimple.web.annotation.RequestMapping;
import com.solvd.webappsimple.web.controller.BaseController;
import com.solvd.webappsimple.web.security.filter.InternalFilter;
import com.solvd.webappsimple.web.util.ContextHolder;
import com.solvd.webappsimple.web.util.HttpMethodElement;
import com.solvd.webappsimple.web.util.ProxyHelper;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@HandlesTypes({
        Service.class,
        WebApplicationInitializer.class,
        InternalFilter.class,
        BaseController.class,
})
public class ContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        List<HttpMethodElement> controllers = new ArrayList<>();
        List<InternalFilter> filters = new ArrayList<>();
        List<Service> services = new ArrayList<>();
        set.forEach(initializedClass -> {
            if (WebApplicationInitializer.class.isAssignableFrom(initializedClass)) {
                WebApplicationInitializer initializer = createNewInitializerInstance(initializedClass);
                initializer.onServletContainerStart(servletContext);
            }
            if (BaseController.class.isAssignableFrom(initializedClass) && !Modifier.isAbstract(initializedClass.getModifiers())) {
                try {
                    BaseController controller = (BaseController) initializedClass.newInstance();
                    List<HttpMethodElement> httpMethodElements = retrieveMappings(controller);
                    controllers.addAll(httpMethodElements);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
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

        if (controllers.size() != 0) {
            servletContext.setAttribute("controllers", controllers);
        }

        if (filters.size() != 0) {
            servletContext.setAttribute("filters", filters);
        }

        if (services.size() != 0) {
            servletContext.setAttribute("services", services);
        }

        ContextHolder.createInstance(servletContext);
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

    private List<HttpMethodElement> retrieveMappings(BaseController controller) {
        Class<? extends BaseController> controllerClass = controller.getClass();
        Method[] methods = controllerClass.getDeclaredMethods();
        String basePath = controllerClass.isAnnotationPresent(RequestMapping.class) ? controllerClass.getAnnotation(RequestMapping.class).value() : "";
        return Arrays.stream(methods).map(method -> {
            String path = null;
            HttpMethodElement.HttpMethod httpMethod = null;
            if (method.isAnnotationPresent(GetMapping.class)) {
                path = basePath + method.getAnnotation(GetMapping.class).value();
                httpMethod = HttpMethodElement.HttpMethod.GET;
            }
            if (method.isAnnotationPresent(PostMapping.class)) {
                path = basePath + method.getAnnotation(PostMapping.class).value();
                httpMethod = HttpMethodElement.HttpMethod.POST;
            }
            if (method.isAnnotationPresent(PutMapping.class)) {
                path = basePath + method.getAnnotation(PutMapping.class).value();
                httpMethod = HttpMethodElement.HttpMethod.PUT;
            }
            if (method.isAnnotationPresent(DeleteMapping.class)) {
                path = basePath + method.getAnnotation(DeleteMapping.class).value();
                httpMethod = HttpMethodElement.HttpMethod.DELETE;
            }
            return new HttpMethodElement<>(method, path, httpMethod, controller);
        }).collect(Collectors.toList());
    }

}
