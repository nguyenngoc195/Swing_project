/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Entity;

/**
 *
 * @author Lan
 */
public class SongCate {
    
private String id;
    
    private String idSong;
    
    private String idCate;

public  SongCate(){
id = String.valueOf(System.currentTimeMillis());
}

    public String getIdSong() {
        return idSong;
    }

    public void setIdSong(String idSong) {
        this.idSong = idSong;
    }

    public String getIdCate() {
        return idCate;
    }

    public void setIdCate(String idCate) {
        this.idCate = idCate;
    }



    
}
