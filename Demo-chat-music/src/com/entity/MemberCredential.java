package com.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import java.util.UUID;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Entity

public class MemberCredential {

    @Index
    private String tokenKey;
    @Unindex
    private String secretToken;
    @Id
    private String userName;
    @Index
    private long createdTime;
    @Index
    private long expiredTime;
    @Index
    private int status; //1-active, 2-dead.

    public MemberCredential() {
    }

    public MemberCredential(String userName) {
        this.tokenKey = UUID.randomUUID().toString();
        this.secretToken = this.tokenKey;
        this.userName = userName;
        this.createdTime = System.currentTimeMillis();
        this.expiredTime = this.createdTime+86400*1000;
        this.status = 1;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
        this.secretToken = tokenKey;
    }


    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public static MemberCredential loadCredential(String secretToken){
        if(secretToken==null){
            return null;
        }
        MemberCredential memberCredential = ofy().load().type(MemberCredential.class).filter("tokenKey",secretToken).first().now();
        if (memberCredential ==null){
            return null;
        }
        if (memberCredential.getExpiredTime()<System.currentTimeMillis()){
            memberCredential.setStatus(0);
            ofy().save().entity(memberCredential).now();
            return null;
        }
        return memberCredential;
    }
}
