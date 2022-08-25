package com.adam.zuulgateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoggingPreFilter extends ZuulFilter{

    Logger log = LoggerFactory.getLogger(LoggingPreFilter.class);

    @Override
    public String filterType() {
        //type of filter, i.e. pre, post, route or error
        return "pre";
    }

    @Override
    public int filterOrder() {
        //the order the filter is run
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        //always enable this filter
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //get the current request context
        RequestContext ctx = RequestContext.getCurrentContext();
        //get the request from the context
        HttpServletRequest request = ctx.getRequest();

        //simply log the request info
        log.info(String.format("PRE: %s request to %s ", request.getMethod(), request.getRequestURL().toString()));
        return null;
    }
}
