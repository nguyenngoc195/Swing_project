/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Entity;

import java.util.HashMap;

/**
 *
 * @author Lan
 */
public class Songs {
    String id;
    String name;
    String urlSource;
    int listenCount;
    String singer;
    String image;
    int status;
    
     public Songs() {
        status = 1;
        listenCount=0;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getListenCount() {
        return listenCount;
    }

    public void setListenCount(int listenCount) {
        this.listenCount = listenCount;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
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
        return errors;
    }
    
}
