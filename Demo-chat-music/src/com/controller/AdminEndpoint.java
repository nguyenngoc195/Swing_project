package com.controller;

import com.entity.*;
import com.google.gson.JsonSyntaxException;
import com.utility.RESTUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;




public class AdminEndpoint extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int action = 1;//1-Fetch the whole list;2-Fetch 1 detail.
        String userName = null;
        String key = null;
        String uriSplit[] = req.getRequestURI().split("/");
        if (uriSplit.length == 5){
            key = uriSplit[uriSplit.length - 1];
        }
        if (uriSplit.length == 6) {
            action = 2;
            userName=uriSplit[uriSplit.length-1];
            key=uriSplit[uriSplit.length-2];
        }
        if (key.equals("key") == false){
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400,"Bad Request","Invalid parameter");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        switch (action) {
            case 1:
                getList(req, resp);
                break;
            case 2:
                getDetail(req, resp, userName);
                break;
            default:
                break;
        }
    }
    private void getDetail(HttpServletRequest req, HttpServletResponse resp, String userName) throws IOException {
        if (userName.length() == 0) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "Admin not found");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
        }
        Admin objDetail = ofy().load().type(Admin.class).id(userName).now();
        if (objDetail == null || objDetail.getStatus() == 0) {
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404, "Not Found", "Admin not Found");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
        }
        resp.getWriter().print(RESTUtil.gson.toJson(objDetail));
    }

    private void getList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int page = 1;
        int limit = 10;
        try {
            page = Integer.parseInt(req.getParameter("page"));
            limit = Integer.parseInt(req.getParameter("limit"));
        } catch (NumberFormatException e) {
            page = 1;
            limit = 10;
        }
        int totalItems = ofy().load().type(Admin.class).filter("status", 1).count();
        List<Admin> list = ofy().load().type(Admin.class).limit(limit).offset((page - 1) * limit).filter("status", 1).list();
        GetListAdmin listAdmin = new GetListAdmin();
        listAdmin.setLimit(limit);
        listAdmin.setData(list);
        listAdmin.setPage(page);
        listAdmin.setTotalItem(totalItems);
        listAdmin.setTotalPage(totalItems / limit + ((totalItems % limit) > 0 ? 1 : 0));
        resp.getWriter().print(RESTUtil.gson.toJson(listAdmin));
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uriSplit[] = req.getRequestURI().split("/");
        if (uriSplit.length != 5) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "URI is wrong");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        String endpoint = uriSplit[uriSplit.length - 1];
        switch (endpoint) {
            case "login":
                loginAdmin(req, resp);
                break;
            case "register":
                registerAdmin(req, resp);
                break;
            default:
                resp.setStatus(400);
                ResponseMsg msg = new ResponseMsg(400, "Bad Request", "URL is wrong");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
        }

    }

    private void loginAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String content = RESTUtil.readStringInputStream(req.getInputStream());
            Admin checkingObj = RESTUtil.gson.fromJson(content, Admin.class);
            Admin obj = ofy().load().type(Admin.class).id(checkingObj.getUserName()).now();
            if (obj == null) {
                resp.setStatus(403);
                ResponseMsg msg = new ResponseMsg(403, "Forbidden", "Admin is not found");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            if (checkingObj.getPassword().equals(obj.getPassword()) == false) {
                resp.setStatus(403);
                ResponseMsg msg = new ResponseMsg(403, "Forbidden", "Password is incorrect");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            AdminCredential credential = new AdminCredential(obj.getUserName());
            if (ofy().save().entity(credential).now() == null) {
                resp.setStatus(500);
                ResponseMsg msg = new ResponseMsg(500, "Server error", "Contact Admin 2");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            resp.getWriter().print(RESTUtil.gson.toJson(credential));
        }catch(JsonSyntaxException e){
            resp.setStatus(500);
            ResponseMsg msg = new ResponseMsg(500, "Server error", "Contact Admin 1");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }


    }

    private void registerAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String content = RESTUtil.readStringInputStream(req.getInputStream());
            Admin obj = RESTUtil.gson.fromJson(content, Admin.class);
            HashMap<String,String> errors = obj.validate();
            if(errors.size()>0){
                resp.setStatus(400);
                ResponseMsg msg = new ResponseMsg(400, "Bad Request", RESTUtil.gson.toJson(errors));
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            if(ofy().save().entity(obj)==null){
                resp.setStatus(500);
                ResponseMsg msg = new ResponseMsg(500, "Server error", "Contact Admin");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            resp.setStatus(201);
            resp.getWriter().print(RESTUtil.gson.toJson(obj));
        } catch (JsonSyntaxException e) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "Data is  not well-form");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }

    }





}
