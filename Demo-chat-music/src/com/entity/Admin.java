package com.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import java.util.HashMap;

@Entity
public class Admin {
    @Id
    String userName;
    @Index
    String password;
    @Index
    int status;
    @Index
    String name;
    @Unindex
    String email;
    @Unindex
    String phone;

    public Admin() {
        status = 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, String> validate() {
        HashMap<String, String> errors = new HashMap<>();
        if (this.userName == null || this.userName.length() == 0) {
            errors.put("userName", "username is invalid");
        }
        if (this.password == null || this.password.length() == 0) {
            errors.put("password", "password is not well-form");
        }
        if (this.name == null || this.name.length() == 0) {
            errors.put("name", "name is not well-form");
        }
        if (this.email == null || this.email.length() == 0) {
            errors.put("email", "email is not well-form");
        }
        if (this.phone == null || this.phone.length() == 0) {
            errors.put("phone", "phone is not well-form");
        }
        if(this.status!=0&&this.status!=1){
            errors.put("status","Status can only be in range of 0 and 1");
        }

        return errors;
    }

}
