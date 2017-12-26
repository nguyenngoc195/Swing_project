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
public class Admin {
    String userName;
    String password;
    String name;
    String email;
    String phone;
    int status;
    
     public Admin() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
    
    
      public HashMap<String, String> validate() {
        HashMap<String, String> errors = new HashMap<>();
     
        if (this.userName == null || this.userName.length() == 0) {
            errors.put("userName", "Name is ommited");
        }
        if (this.password == null || this.password.length() == 0) {
            errors.put("urlSource", "The URL is ommited");
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
        return errors;
    }
      
        public HashMap<String, String> login() {
        HashMap<String, String> errors = new HashMap<>();
     
        if (this.userName == null || this.userName.length() == 0) {
            errors.put("userName", "Name is ommited");
        }
        if (this.password == null || this.password.length() == 0) {
            errors.put("urlSource", "The URL is ommited");
        }
        return errors;
    }
}
