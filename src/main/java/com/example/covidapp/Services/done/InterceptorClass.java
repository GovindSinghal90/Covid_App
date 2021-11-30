package com.example.covidapp.Services.done;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Log4j2
public class InterceptorClass implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Controller got called:  "+request.getRequestURI());
        request.setAttribute("timeElapsed",System.currentTimeMillis());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long diff=System.currentTimeMillis()-Long.parseLong(request.getAttribute("timeElapsed")+"");
        log.info("Time elapsed: "+diff);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
