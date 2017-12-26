/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Entity;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Lan
 */
public class Member {

    String userName;
    String password;
    String fullName;
    String address;
    String email;
    String avatar;
    int status;

    public Member() {
        avatar = "DEFAULT";
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
            errors.put("uesrname", "username is not well-formed");
        }
        if (this.password == null || this.password.length() == 0) {
            errors.put("password", "password is not well-formed");
        }

        if (this.fullName == null || this.fullName.length() == 0) {
            errors.put("fullname", "fullname is not well-formed");
        }
        if (this.address == null || this.address.length() == 0) {
            errors.put("address", "address is not well-formed");
        }
        if (!matcherEmail.matches()) {
            errors.put("email", "Email is not well-formed");
        }

        return errors;
    }
}
