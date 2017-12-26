package com.controller;

import com.entity.*;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class CORSFilter implements Filter {
    private Logger LOGGER = Logger.getLogger(CORSFilter.class.getSimpleName());
    static {
        ObjectifyService.register(Member.class);
        ObjectifyService.register(Song.class);
        ObjectifyService.register(Admin.class);
        ObjectifyService.register(AdminCredential.class);
        ObjectifyService.register(MemberCredential.class);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        servletResponse.setContentType("application/json");
        servletResponse.setCharacterEncoding("UTF-8");
        servletRequest.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, MemberAuth");
        if (request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        filterChain.doFilter(request, response);

    }


    @Override
    public void destroy() {

    }
}
