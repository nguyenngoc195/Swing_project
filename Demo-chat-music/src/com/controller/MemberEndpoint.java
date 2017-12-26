package com.controller;

import com.entity.GetListMember;
import com.entity.ResponseMsg;
import com.entity.Member;
import com.entity.MemberCredential;
import com.google.gson.JsonSyntaxException;
import com.googlecode.objectify.cmd.Query;
import com.utility.FullTextSearchHandle;
import com.utility.RESTUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class MemberEndpoint extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int action = 1; //1-Get whole list; 2-get detail.
        String id = null;
        String admin = null;
        String uriSplit[] = req.getRequestURI().split("/");
        if (uriSplit.length == 5){
            admin = uriSplit[uriSplit.length - 1];
        }
        if (uriSplit.length == 6) {
            action = 2;
            id=uriSplit[uriSplit.length-1];
            admin=uriSplit[uriSplit.length-2];

        }
        if (admin.equals("admin") == false){
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
                getObjDetail(req, resp, id);
                break;
            default:
                break;
        }
    }

    private void getObjDetail(HttpServletRequest req, HttpServletResponse resp, String userName) throws IOException {
        if (userName == null) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "ID NULL");
            resp.getWriter().write(RESTUtil.gson.toJson(msg));
            return;
        }
        Member memberFound = ofy().load().type(Member.class).id(userName).now();
        if (memberFound == null || memberFound.getStatus() == 0) {
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404, "Member not Found", "Member is not exist or has been deleted");
            resp.getWriter().println(RESTUtil.gson.toJson(msg));

        }
        resp.getWriter().write(RESTUtil.gson.toJson(memberFound));
    }

    //Fetch the whole list and Pagination the list.
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
        Query<Member> queryObj = ofy().load().type(Member.class).filter("status", 1);
        int totalItemCount = queryObj.count();
        int totalPages = totalItemCount / limit + ((totalItemCount % limit) > 0 ? 1 : 0);
        List<Member> listObj = ofy().load().type(Member.class).limit(limit).offset((page - 1) * limit).filter("status", 1).list();
        GetListMember list = new GetListMember();
        list.setData(listObj);
        list.setLimit(limit);
        list.setPage(page);
        list.setTotalItem(totalItemCount);
        list.setTotalPage(totalPages);
        resp.getWriter().write(RESTUtil.gson.toJson(list));

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
                loginMember(req, resp);
                break;
            case "register":
                registerMember(req, resp);
                break;
            default:
                resp.setStatus(400);
                ResponseMsg msg = new ResponseMsg(400, "Bad Request", "URL is wrong");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
        }

    }

    private void loginMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String content = RESTUtil.readStringInputStream(req.getInputStream());
            Member checkingObj = RESTUtil.gson.fromJson(content, Member.class);
            if (checkingObj == null){
                resp.setStatus(403);
                ResponseMsg msg = new ResponseMsg(403, "xxxx", "Member null");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;

            }
            Member obj = ofy().load().type(Member.class).id(checkingObj.getUserName()).now();

            if (obj == null && obj.getStatus()!=1) {
                resp.setStatus(403);
                ResponseMsg msg = new ResponseMsg(403, "Forbidden", "Member is not found");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            if (checkingObj.getPassword().equals(obj.getPassword()) == false) {
                resp.setStatus(403);
                ResponseMsg msg = new ResponseMsg(403, "Forbidden", "Password is incorrect");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }

                MemberCredential credential = new MemberCredential(obj.getUserName());

            if (credential == null){
                resp.setStatus(500);
                ResponseMsg msg = new ResponseMsg(500, "Server error", "Contact ngoc");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            if (ofy().save().entity(credential).now() == null) {
                resp.setStatus(500);
                ResponseMsg msg = new ResponseMsg(500, "Server error", "Contact Admin");
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

    private void registerMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String content = RESTUtil.readStringInputStream(req.getInputStream());
            Member obj = RESTUtil.gson.fromJson(content, Member.class);
            HashMap<String,String> errors = obj.validate();
            Member check = ofy().load().type(Member.class).id(obj.getUserName()).now();
            if (check != null){
                resp.setStatus(409);
                ResponseMsg msg = new ResponseMsg(409, "Forbidden", RESTUtil.gson.toJson(errors));
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uriSplit[] = req.getRequestURI().split("/");
        if (uriSplit.length != 5) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "Invalid parameter");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        //If URI is ok.
        String id = uriSplit[uriSplit.length - 1];
        Member obj = ofy().load().type(Member.class).id(id).now();
        if (obj == null || obj.getStatus() == 0) {
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404, "Not Found", "Member is not exist or has been deleted");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        try {
            String content = RESTUtil.readStringInputStream(req.getInputStream());
            Member newObj = RESTUtil.gson.fromJson(content, Member.class);
            HashMap<String, String> errors = newObj.validate();
            if (errors.size() > 0) {
                resp.setStatus(400);
                ResponseMsg msg = new ResponseMsg(400, "Bad Request", "Data is not well-form");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            if(id.trim().equals(newObj.getUserName().trim())==false){
                resp.setStatus(409);
                ResponseMsg msg = new ResponseMsg(409,"Data conflict","The IDs is not match(in URI and in data sent)");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
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
        String key = uriSplit[uriSplit.length-2];
        if(uriSplit.length!=6 && key.equals("admin") == false){
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400,"Bad Request","Missing parameter");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        String id = uriSplit[uriSplit.length-1];

        Member obj = ofy().load().type(Member.class).id(id).now();
        if(obj==null||obj.getStatus()==0){
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404,"Not Found","Member is not exist or has been deleted");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        obj.setStatus(0);
        if(ofy().save().entity(obj).now()==null){
            resp.setStatus(500);
            ResponseMsg msg = new ResponseMsg(500,"Server Error","PLease contact admin for support");
            return;
        }
        ResponseMsg msg = new ResponseMsg(200,"OK","Delete successfully");
        resp.getWriter().print(RESTUtil.gson.toJson(msg));
    }
}
