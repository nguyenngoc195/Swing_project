package com.controller;

import com.entity.MemberCredential;
import com.entity.ResponseMsg;
import com.utility.RESTUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class ChatroomFilter implements Filter {
    private Logger LOGGER = Logger.getLogger(ChatroomFilter.class.getSimpleName());
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String MemberTokenKey = req.getHeader("MemberAuth");
        MemberCredential credential = MemberCredential.loadCredential(MemberTokenKey);
        LOGGER.info("TOKEN KEY: "+MemberTokenKey);
        if (credential == null) {
            resp.setStatus(403);
            ResponseMsg msg = new ResponseMsg(403, "Forbidden", "Token key is expired or invalid");
            resp.getWriter().println(RESTUtil.gson.toJson(msg));
            resp.getWriter().println(MemberTokenKey);
            return;
        }
        chain.doFilter(request,response);
//        response.getWriter().print("//chatroom.html");
    }

    @Override
    public void destroy() {

    }
}
