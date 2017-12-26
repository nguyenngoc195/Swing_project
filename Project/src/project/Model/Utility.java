/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Lan
 */
public class Utility {
    
    public static String digestPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] afterDigest = md.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < afterDigest.length; i++) {
                sb.append(Integer.toHexString(0xff & afterDigest[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

}
