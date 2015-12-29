/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webcrawler.bal;
import java.security.MessageDigest;
/**
 *
 * @author AshishPanigrahi
 */
public class Hasher {
    public static String toSha256(String inString) throws Exception{
            MessageDigest md  = MessageDigest.getInstance("SHA-256");
            String text = inString.toLowerCase();
            md.update(text.getBytes("ASCII"));
            byte[] hash = md.digest();
            StringBuilder sb = new StringBuilder();
            
            for(byte b: hash){
             sb.append(String.format("%02x", b));
            }
            return sb.toString().toUpperCase();  
    }
}
