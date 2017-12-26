package com.utility;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RESTUtil {
    public static Gson gson = new Gson();
    private static Logger LOGGER = Logger.getLogger(RESTUtil.class.getName());

    public static String readStringInputStream(InputStream is) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        StringBuffer buffer = new StringBuffer();
        String line;
        while((line=br.readLine())!=null){
            buffer.append(line);
        }
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("String: "+buffer.toString());
        return buffer.toString();
    }
}
