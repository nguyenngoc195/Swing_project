package com.entity;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import java.util.HashMap;

@Entity
public class Song {
    @Id
    String id;
    @Index
    String name;
    @Index
    String urlSource;
    @Index
    String singer;
    @Index
    int listenCount;
    @Unindex
    String image;
    @Index
    int status;//1-active,0-dead.

    public Song() {
        listenCount=0;
        status = 1;
    }
    public Song(String id, String name, String urlSource, String singer, int listenCount, String image, int status) {
        this.id = id;
        this.name = name;
        this.urlSource = urlSource;
        this.singer = singer;
        this.listenCount = listenCount;
        this.image = image;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlSource() {
        return urlSource;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getListenCount() {
        return listenCount;
    }

    public void setListenCount(int listenCount) {
        this.listenCount = listenCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, String> validate() {
        HashMap<String, String> errors = new HashMap<>();
        if (this.id == null || this.id.length() == 0) {
            errors.put("id", "Id cannot be ignored!");
        }

        if (this.name == null || this.name.length() == 0) {
            errors.put("name", "Name is ommited");
        }

        if (this.urlSource == null || this.urlSource.length() == 0) {
            errors.put("urlSource", "The URL is ommited");
        }
        if(this.singer==null||this.singer.length()==0){
            errors.put("singer","Singer cannot be ommited");
        }
        if(this.image==null||this.image.length()==0){
            errors.put("avatar","avatar cannot be ommited");
        }
        if(this.status!=0&&this.status!=1){
            errors.put("status","Status can only be in range of 0 and 1");
        }
        return errors;
    }

    public Document toSearchDocument() {
        return
                Document.newBuilder()
                        .setId(this.id)
                        .addField(Field.newBuilder().setName("name").setText(this.getName()))
                        .addField(Field.newBuilder().setName("singer").setText(this.getSinger()))
                        .addField(Field.newBuilder().setName("urlSource").setText(this.getUrlSource()))
                        .addField(Field.newBuilder().setName("image").setText(this.getImage()))
                        .build();
    }
}
