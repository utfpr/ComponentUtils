package com.m4rc310.ui.gui.utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcelo
 */
public class MD5Utils {

    
    
    
    public static String getHashMD5(File file) {
        try (InputStream is = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //byte[] buffer = new byte[80192];
            byte[] buffer = new byte[(int)file.length()];
            int read;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            
            byte[] md5sum = digest.digest();
            BigInteger bi = new BigInteger(1, md5sum);
            String out = bi.toString(16);
            return out;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static String getHashMD5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger bi = new BigInteger(1, md.digest(string.getBytes()));
            return bi.toString(16);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5Utils.class
                    .getName()).log(Level.SEVERE, null, ex);

            return "";
        }
    }
}
