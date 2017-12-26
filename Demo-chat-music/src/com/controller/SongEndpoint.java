package com.controller;

import com.entity.GetListSong;
import com.entity.ResponseMsg;
import com.entity.Song;
import com.google.gson.JsonSyntaxException;
import com.googlecode.objectify.ObjectifyService;
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

public class SongEndpoint extends HttpServlet {
    static {
        ObjectifyService.register(Song.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int action = 1;//1-Fetch the whole list;2-Fetch 1 detail.
        String id = null;
        String key = null;
        String uriSplit[] = req.getRequestURI().split("/");
        if (uriSplit.length == 5){
            key = uriSplit[uriSplit.length - 1];
        }
        if (uriSplit.length == 6) {
            action = 2;
            id = uriSplit[uriSplit.length - 1];
            key = uriSplit[uriSplit.length - 2];
        }
        if (key.equals("get") == false){
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
                getDetail(req, resp, id);
                break;
            default:
                break;
        }
    }

    private void getDetail(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        if (id.length() == 0) {
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400, "Bad Request", "");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
        }
        Song objDetail = ofy().load().type(Song.class).id(id).now();
        if (objDetail == null || objDetail.getStatus() == 0) {
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404, "Not Found", "Song not Found");
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
        int totalItems = ofy().load().type(Song.class).filter("status", 1).count();
        List<Song> list = ofy().load().type(Song.class).limit(limit).offset((page - 1) * limit).filter("status", 1).list();
        GetListSong listSong = new GetListSong();
        listSong.setLimit(limit);
        listSong.setData(list);
        listSong.setPage(page);
        listSong.setTotalItems(totalItems);
        listSong.setTotalPage(totalItems / limit + ((totalItems % limit) > 0 ? 1 : 0));
        resp.getWriter().print(RESTUtil.gson.toJson(listSong));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uriSplit[] = req.getRequestURI().split("/");
        String key =  uriSplit[uriSplit.length - 1];
        if(key.equals("admin") == false){
            resp.setStatus(500);
            ResponseMsg msg = new ResponseMsg(500,"Internal Server Error","Please contact admin for more information");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        String content = RESTUtil.readStringInputStream(req.getInputStream());
        try {
            Song song = RESTUtil.gson.fromJson(content, Song.class);
            HashMap<String,String> errors = song.validate();
            if(errors.size()>0){
                resp.setStatus(400);
                ResponseMsg msg = new ResponseMsg(400,"Bad Request","THe data sent is not well-form");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            if(ofy().save().entity(song).now()==null){
                resp.setStatus(500);
                ResponseMsg msg = new ResponseMsg(500,"Internal Server Error","Please contact admin for more information");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            FullTextSearchHandle.add(song.toSearchDocument());
            resp.setStatus(200);
            resp.getWriter().print(RESTUtil.gson.toJson(song));
        }catch (JsonSyntaxException e){
            e.printStackTrace(System.err);
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400,"BAD REQUEST","The data is not well-form");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String urlSplit[] = req.getRequestURI().split("/");
        String key = urlSplit[urlSplit.length - 2];
        if(urlSplit.length!=6 || key.equals("admin") == false ){
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400,"Bad Request","Invalid parameter");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        String id = urlSplit[urlSplit.length-1];
        Song song = ofy().load().type(Song.class).id(id).now();
        if(song==null||song.getStatus()==0){
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404,"Not Found","Song was deleted or not exist");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        String content = RESTUtil.readStringInputStream(req.getInputStream());
        try {
            Song newSong = RESTUtil.gson.fromJson(content,Song.class);
            HashMap<String,String> errors = newSong.validate();
            if(errors.size()>0){
                resp.setStatus(400);
                ResponseMsg msg = new ResponseMsg(400,"Bad Request","Data is not well-form");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }
            if(id.trim().equals(newSong.getId().trim())==false){
                resp.setStatus(409);
                ResponseMsg msg = new ResponseMsg(409,"Data conflict","Id's are not the same");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }

            song.setId(newSong.getId());
            song.setName(newSong.getName());
            song.setSinger(newSong.getSinger());
            song.setUrlSource(newSong.getUrlSource());
            song.setListenCount(newSong.getListenCount());
            song.setImage(newSong.getImage());
            song.setStatus(newSong.getStatus());
            if(ofy().save().entity(song).now()==null){
                resp.setStatus(500);
                ResponseMsg msg = new ResponseMsg(500,"Internal server errors","Contact admin for support");
                resp.getWriter().print(RESTUtil.gson.toJson(msg));
                return;
            }


            FullTextSearchHandle.add(song.toSearchDocument());
            resp.setStatus(200);
        }catch (JsonSyntaxException e){
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400,"Bad request","data is not well-form");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uriSplit[] = req.getRequestURI().split("/");
        String key = uriSplit[uriSplit.length - 2];
        if(uriSplit.length!=6 || key.equals("admin") == false){
            resp.setStatus(400);
            ResponseMsg msg = new ResponseMsg(400,"Bad request","Missing parameter");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        String id = uriSplit[uriSplit.length-1];
        Song song = ofy().load().type(Song.class).id(id).now();
        if(song==null||song.getStatus()==0){
            resp.setStatus(404);
            ResponseMsg msg = new ResponseMsg(404,"Not Found","Song is not found or has been delete");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        song.setStatus(0);
        if (ofy().save().entity(song).now()==null){
            resp.setStatus(500);
            ResponseMsg msg = new ResponseMsg(500,"Server Error","Please contact admin");
            resp.getWriter().print(RESTUtil.gson.toJson(msg));
            return;
        }
        FullTextSearchHandle.delete(song.getId());
        resp.setStatus(200);
        ResponseMsg msg = new ResponseMsg(200,"Success","Delete successfully");
    }
}
