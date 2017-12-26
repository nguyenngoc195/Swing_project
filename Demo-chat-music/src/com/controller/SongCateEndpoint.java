package com.controller;

import com.entity.*;
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

public class SongCateEndpoint extends HttpServlet {

    static {
        ObjectifyService.register(SongCate.class);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = null;
        String uriSplit[] = req.getRequestURI().split("/");
        String key = uriSplit[uriSplit.length - 2];
        if(key.equals("get")==false){
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400,"Bad Request","Invalid parameter");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        if (uriSplit.length == 6)
            id = uriSplit[uriSplit.length - 1];
        int page = 1;
        int limit = 10;
        try {
            page = Integer.parseInt(req.getParameter("page"));
            limit = Integer.parseInt(req.getParameter("limit"));
        } catch (NumberFormatException e) {
            page = 1;
            limit = 10;
        }
        int totalItems = ofy().load().type(SongCate.class).filter("idCate", id).count();
        List<SongCate> list = ofy().load().type(SongCate.class).limit(limit).offset((page - 1) * limit).filter("idCate", id).list();
        GetListSongCate listSongCate = new GetListSongCate();
        listSongCate.setLimit(limit);
        listSongCate.setData(list);
        listSongCate.setPage(page);
        listSongCate.setTotalItem(totalItems);
        listSongCate.setTotalPage(totalItems / limit + ((totalItems % limit) > 0 ? 1 : 0));
        resp.getWriter().print(RESTUtil.gson.toJson(listSongCate));


        }





    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uriSplit[] = req.getRequestURI().split("/");
        String key =  uriSplit[uriSplit.length - 1];
        if(key.equals("admin") == false){
            resp.setStatus(408);
            ResponseMsg msg = new ResponseMsg(408,"Internal Server Error","Please contact admin for more information");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        try {
            String content = RESTUtil.readStringInputStream(req.getInputStream());
            SongCate obj = RESTUtil.gson.fromJson(content, SongCate.class);

            if (ofy().save().entity(obj).now() == null) {
                resp.setStatus(500);
                ResponseMsg msg = new ResponseMsg(500, "Internal Server Error", "???");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            resp.setStatus(200);
            resp.getWriter().print(RESTUtil.gson.toJson(obj));
        } catch (IOException | JsonSyntaxException e) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "error");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uriSplit[] = req.getRequestURI().split("/");
        String key =  uriSplit[uriSplit.length - 2];
        if (uriSplit.length != 6 || key.equals("admin") == false) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "Invalid");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }

        String id = uriSplit[uriSplit.length - 1];
        SongCate obj = ofy().load().type(SongCate.class).id(id).now();
        if (obj == null ) {
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404, "Not Found", "Category is not exist or has been deleted");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        try {
            String content = RESTUtil.readStringInputStream(req.getInputStream());
            SongCate newObj = RESTUtil.gson.fromJson(content, SongCate.class);

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
        String key =  uriSplit[uriSplit.length - 2];
        if(uriSplit.length!=6 || key.equals("admin")==false){
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400,"Bad Request","Missing parameter");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        String id = uriSplit[uriSplit.length-1];
        SongCate obj = ofy().load().type(SongCate.class).id(id).now();
        if(obj==null){
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404,"Not Found","SongCate is not exist or has been deleted");
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

