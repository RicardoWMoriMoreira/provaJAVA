package com.ecommerce.view;

import com.ecommerce.controller.UserController;
import com.ecommerce.model.User;
import com.ecommerce.model.enums.UserRole;

import java.util.Optional;
import java.util.Scanner;

public class LoginView {

    private final Scanner sc;
    private final UserController uc=new UserController();

    public LoginView(Scanner s){ this.sc=s; }

    public Optional<User> showLoginMenu(){
        while(true){
            System.out.println("1 Login 2 Cadastro 3 Sair");
            System.out.println("=============================");
            System.out.print("Digite a opçao desejada: ");
            switch(sc.nextLine()){
                case "1"->{
                    System.out.print("Email: "); String e=sc.nextLine();
                    System.out.print("Senha: "); String p=sc.nextLine();
                    Optional<User> u=uc.login(e,p);
                    if(u.isEmpty()) System.out.println("Falha login");
                    else return u;
                }
                case "2"->register();
                case "3"-> { return Optional.empty(); }
            }
        }

    }

    private void register(){
        System.out.print("Nome: "); String n=sc.nextLine();
        System.out.print("Email: "); String e=sc.nextLine();
        System.out.print("Senha (Letras Maiúsculas, minúsculas e caracteres (ex.: @, $, #): "); String p=sc.nextLine();
        System.out.print("Confirme: "); String c=sc.nextLine();
        System.out.println("1 Cliente 2 Admin"); String r=sc.nextLine();
        UserRole role=r.equals("2")?UserRole.ADMIN:UserRole.CUSTOMER;
        uc.registerUser(e,p,c,n,role);
        System.out.println("=============================");
    }

}