package com.entity;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Member {
    @Id
    String userName;
    @Index
    String password;
    @Index
    String fullName;
    @Index
    String address;
    @Index
    String email;
    @Unindex
    String avatar;
    @Index
    int status; //1-active,0-deactive.


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Member() {
        avatar= "DEFAULT";
        status = 1;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, String> validate() {
        HashMap<String, String> errors = new HashMap<>();
        String patternEmailStr = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern patternEmail = Pattern.compile(patternEmailStr, Pattern.CASE_INSENSITIVE);
        Matcher matcherEmail = patternEmail.matcher(this.email);

        String patterUserName = "^[a-zA-Z0-9._-]{3,}$";
        Pattern patternUserName = Pattern.compile(patterUserName, Pattern.CASE_INSENSITIVE);
        Matcher matcherUserName = patternUserName.matcher(this.userName);

        if (!matcherUserName.matches()) {
            errors.put("userName", "username is not well-formed");
        }

        if (this.password == null || this.password.length() == 0) {
            errors.put("password", "password cannot be ignored");
        }

        if (this.fullName == null || this.fullName.length() == 0) {
            errors.put("fullName", "fullname is compulsory");
        }
        if (!matcherEmail.matches()) {
            errors.put("email", "Email is not well-formed");
        }
        if (this.address == null || this.address.length() == 0) {
            errors.put("address", "address is compulsory");
        }
        if(this.status!=0&&this.status!=1){
            errors.put("status","Status can only be in range of 0 and 1");
        }

        return errors;
    }

}
