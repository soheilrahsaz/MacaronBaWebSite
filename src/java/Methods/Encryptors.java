/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Methods;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import mehritco.ir.megnatis.security.codedsystem.DeCode;
import mehritco.ir.megnatis.security.codedsystem.EnCode;

/**
 *
 * @author Moses
 */
public class Encryptors {
    /**
     * returns md5 hashed of the text you give
     * @param text the text which you want it's hashed
     * @return md5hashed of the text
     * @throws NoSuchAlgorithmException 
     */
    public static String MD5Hasher(String text) throws NoSuchAlgorithmException
    {
        MessageDigest messagedigest=MessageDigest.getInstance("MD5");
        messagedigest.update(text.getBytes());
        byte[] digest=messagedigest.digest();
        String hashed=DatatypeConverter.printHexBinary(digest).toUpperCase();
        
        return hashed;
    }
    
    /**
     * returns encrypted by farshid's class
     * @param text the text which you want to encrypt
     * @return encrypted text
     */
    public static String Encrypt1(String text)
    {
        String encoded="";
        try {
            EnCode en=new EnCode(text);
            encoded=en.encode();
        } catch (Exception ex) {
            Logger.getLogger(Encryptors.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return encoded;
    }
    /**
     * returns encrypted by farshid's class
     * @param text the text which you want to encrypt
     * @return encrypted text
     */
    public static String Encrypt1(int text)
    {
        String txt=String.valueOf(text);
        String encoded="";
        try {
            EnCode en=new EnCode(txt);
            encoded=en.encode();
        } catch (Exception ex) {
        }
        
        return encoded;
    }
    
    /**
     * Decripts the text according to farshid's class
     * @param text the encrypted text by farshid's class
     * @return decripted text
     */
    public static String Decript1(String text)
    {
        String decrypted="";
        
        try {
            DeCode decode=new DeCode();
            decrypted=decode.decode(text);
        } catch (Exception e) 
        {
            
        }
        return decrypted;
    }
    
}
