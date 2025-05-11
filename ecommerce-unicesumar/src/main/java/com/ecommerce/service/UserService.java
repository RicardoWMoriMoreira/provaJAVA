package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.model.enums.UserRole;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.util.EmailValidator;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class UserService {

    private final UserRepository repo=new UserRepository();
    private final PasswordService pass=new PasswordService();

    public boolean registerUser(String email,String pwd,String confirm,
                                String name,UserRole role){
        if(email==null||!EmailValidator.isValid(email)){ System.out.println("Email inválido."); return false; }
        if(repo.emailExists(email)){ System.out.println("Email existente."); return false; }
        if(name==null||name.isBlank()){ System.out.println("Nome obrigatório."); return false; }
        if(!pwd.equals(confirm)){ System.out.println("Senhas não coincidem."); return false; }
        try{
            String salted=pass.registerUser(email,pwd);
            repo.save(new User(email,salted,name,role));
            return true;
        }catch(Exception e){ System.out.println(e.getMessage()); return false; }
    }

    public Optional<User> login(String email,String pwd){
        return repo.findByEmail(email).filter(u->{
            try{ return pass.verifyPassword(pwd,u.getPassword()); }catch(Exception e){ return false; }
        });
    }
    public Optional<User> findByEmail(String email){ return repo.findByEmail(email); }

}