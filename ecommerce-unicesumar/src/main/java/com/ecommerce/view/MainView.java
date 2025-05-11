package com.ecommerce.view;

import com.ecommerce.model.User;
import com.ecommerce.model.enums.UserRole;

import java.util.Optional;
import java.util.Scanner;

public class MainView {

    private final Scanner sc=new Scanner(System.in);
    private final LoginView lv=new LoginView(sc);

    public void start(){
        System.out.println("=============================");
        System.out.println("Bem‑vindo!");
        System.out.println("=============================");
        Optional<User> u=lv.showLoginMenu();
        if(u.isPresent()){
            User user=u.get();
            if(user.getRole()==UserRole.ADMIN) new AdminView(sc).showAdminMenu();
            else new CustomerView(sc,user).showCustomerMenu();
        }
        System.out.println("Encerrado... Volte sempre!!!");
    }

}