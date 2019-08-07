package com.solvd.webappsimple.web.security;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class DelegatingFilter implements Filter {

    private List<InternalFilter> internalFilters;

    @Override
    @SuppressWarnings("unchecked")
    public void init(FilterConfig filterConfig) throws ServletException {
        List<InternalFilter> filters = new ArrayList<>((Collection<InternalFilter>) filterConfig.getServletContext().getAttribute("filters"));
        setInternalFilters(filters);

        for (InternalFilter filter : internalFilters) {
            filter.init(filterConfig);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        new InternalFilterChain(chain, internalFilters).doFilter(request, response);
    }

    @Override
    public void destroy() {
        for (InternalFilter filter : internalFilters) {
            filter.destroy();
        }
    }

    private void setInternalFilters(List<InternalFilter> internalFilters) {
        this.internalFilters = internalFilters;
        this.internalFilters.sort(Comparator.comparingInt(InternalFilter::order));
    }

    private static class InternalFilterChain implements FilterChain {

        private final FilterChain originChain;
        private final List<InternalFilter> internalFilters;

        private int currentPosition = 0;

        InternalFilterChain(FilterChain originChain, List<InternalFilter> internalFilters) {
            this.originChain = originChain;
            this.internalFilters = internalFilters;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            currentPosition ++;
            if (currentPosition <= internalFilters.size()) {
                InternalFilter filter = internalFilters.get(currentPosition - 1);
                filter.doFilter(request, response, this);
            } else {
                originChain.doFilter(request, response);
            }
        }

    }

}
