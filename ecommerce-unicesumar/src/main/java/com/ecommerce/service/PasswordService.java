package com.ecommerce.service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.security.SecureRandom;
public class PasswordService {

    public static class InvalidPasswordException extends Exception{
        public InvalidPasswordException(String m){ super(m); }
    }

    private static final int SALT_BYTES=16, ITER=10_000;

    public String registerUser(String email,String pwd)
            throws InvalidPasswordException,NoSuchAlgorithmException{
        validate(pwd);
        byte[] salt=genSalt();
        byte[] hash=hash(pwd.toCharArray(),salt);
        return enc(salt)+"$"+enc(hash);
    }

    public boolean verifyPassword(String cand,String stored)throws NoSuchAlgorithmException{
        String[] parts=stored.split("\\$");
        byte[] salt=dec(parts[0]), hash=dec(parts[1]);
        byte[] chash=hash(cand.toCharArray(),salt);
        if(hash.length!=chash.length) return false;
        int diff=0; for(int i=0;i<hash.length;i++) diff|=hash[i]^chash[i];
        return diff==0;
    }

    private void validate(String p)throws InvalidPasswordException{
        boolean ok=p.length()>=8&&p.matches(".*[A-Z].*")&&p.matches(".*[a-z].*")
                &&p.matches(".*\\d.*")&&p.matches(".*[^A-Za-z0-9].*");
        if(!ok) throw new InvalidPasswordException("Senha fraca.");
    }
    private byte[] genSalt(){ byte[] s=new byte[SALT_BYTES]; new SecureRandom().nextBytes(s); return s; }
    private byte[] hash(char[] pwd,byte[] salt)throws NoSuchAlgorithmException{
        MessageDigest md=MessageDigest.getInstance("SHA-256"); md.update(salt);
        byte[] bytes=new String(pwd).getBytes(StandardCharsets.UTF_8);
        byte[] dig=md.digest(bytes);
        for(int i=1;i<ITER;i++){ md.reset(); dig=md.digest(dig); }
        return dig;
    }
    private String enc(byte[] b){ return Base64.getEncoder().encodeToString(b); }
    private byte[] dec(String s){ return Base64.getDecoder().decode(s); }

}