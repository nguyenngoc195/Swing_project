package com.controller;

import com.entity.MemberCredential;
import com.entity.ResponseMsg;
import com.utility.RESTUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberAuth implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String MemberTokenKey = req.getHeader("MemberAuth");
        MemberCredential credential = MemberCredential.loadCredential(MemberTokenKey);
        if (credential == null) {
            resp.setStatus(403);
            ResponseMsg msg = new ResponseMsg(403, "Forbidden", "Token key is exppired or invalid");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
