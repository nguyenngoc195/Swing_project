package com.controller;

import com.entity.*;
import com.entity.GetListCategory;
import com.google.gson.JsonSyntaxException;
import com.googlecode.objectify.ObjectifyService;
import com.utility.RESTUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class CategoryEndpoint extends HttpServlet {

    static {
        ObjectifyService.register(Category.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       int action =1;
       String id = null;
       String uriSplit[] = req.getRequestURI().split("/");
       if (uriSplit.length == 5) {
            action = 2;
            id = uriSplit[uriSplit.length - 1];
       }
       switch (action){
           case 1:
               getList(req,resp);
               break;

           case 2:
               getDetail(req,resp,id);
       }

    }
    private void getList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int page = 1;
        int limit = 10;
        try {
            page = Integer.parseInt(req.getParameter("page"));
            limit = Integer.parseInt(req.getParameter("limit"));
        }
        catch (NumberFormatException e){
            page = 1;
            limit = 10;
        }
        int totalItems = ofy().load().type(Category.class).count();
        List<Category> list = ofy().load().type(Category.class).limit(limit).offset((page - 1) * limit).list();
        GetListCategory listCategory = new GetListCategory();
        listCategory.setLimit(limit);
        listCategory.setData(list);
        listCategory.setPage(page);
        listCategory.setTotalItem(totalItems);
        listCategory.setTotalPage(totalItems / limit + ((totalItems % limit) > 0 ? 1 : 0));
        resp.getWriter().print(RESTUtil.gson.toJson(listCategory));
    }

    private void getDetail(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        if (id.length() == 0) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "Missing ID in request's parameter");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
        }
        Category objDetail = ofy().load().type(Category.class).id(id).now();
        if (objDetail == null ) {
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404, "Not Found", "Category not Found");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
        }
        resp.getWriter().print(RESTUtil.gson.toJson(objDetail));
    }




    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String content = RESTUtil.readStringInputStream(req.getInputStream());
        try {
            Category category = RESTUtil.gson.fromJson(content,Category.class);
            if(ofy().save().entity(category).now()==null){
                resp.setStatus(500);
                ResponseMsg msg = new ResponseMsg(500,"Internal Server Error","Please contact admin for more information");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            resp.setStatus(201);
            resp.getWriter().print(RESTUtil.gson.toJson(category));
        }catch (JsonSyntaxException e){
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400,"BAD REQUEST","The data is not well-form");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uriSplit[] = req.getRequestURI().split("/");
        if (uriSplit.length != 5) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "Invalid");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }

        String id = uriSplit[uriSplit.length - 1];
        Category obj = ofy().load().type(Category.class).id(id).now();
        if (obj == null ) {
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404, "Not Found", "Category is not exist or has been deleted");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        try {
            String content = RESTUtil.readStringInputStream(req.getInputStream());
            Category newObj = RESTUtil.gson.fromJson(content, Category.class);

            if(ofy().save().entity(newObj).now()==null){
                resp.setStatus(500);
                ResponseMsg msg = new ResponseMsg(500,"Internal server errors","PLease contact admin for being supported");
                return;
            }
            resp.setStatus(200);
            resp.getWriter().print(RESTUtil.gson.toJson(newObj));

        } catch (IOException | JsonSyntaxException e) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "Data is not well-form");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uriSplit[] = req.getRequestURI().split("/");
        if(uriSplit.length!=5){
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400,"Bad Request","Missing parameter");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        String id = uriSplit[uriSplit.length-1];
        Void obj = ofy().delete().type(Category.class).id(id).now();
        if(obj==null){
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404,"Not Found","Category is not exist or has been deleted");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        if(ofy().save().entity(obj).now()==null){
            resp.setStatus(500);
            ResponseMsg msg = new ResponseMsg(500,"Server Error","PLease contact admin for support");
            return;
        }
        ResponseMsg msg = new ResponseMsg(200,"OK","Delete successfully");
        resp.getWriter().print(RESTUtil.gson.toJson(msg));
    }
}

