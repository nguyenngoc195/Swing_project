/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


import com.google.gson.Gson;
import java.awt.print.Book;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import project.Entity.Songs;

/**
 *
 * @author Administrator
 */
public class Support {

    private static Songs currentProductFrame;
    private static Gson gson = new Gson();

    private static String s1 = "Hà nội Hà Nam Hà Đông";
    private static String n;

    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").toLowerCase();
    }
}
