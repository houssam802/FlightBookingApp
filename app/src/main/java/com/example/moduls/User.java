package com.example.moduls;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    int id;
    String Fname,Lname,email,mdp;

    public static String crypt(String pass)  {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    public static boolean decrypt(String mdp,String hash_mdp)  {
        if(hash_mdp.equals(crypt(mdp))) return true;
        else return false;
    }

    public static boolean is_email_valid(String email){
        return Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",email);
    }
}
