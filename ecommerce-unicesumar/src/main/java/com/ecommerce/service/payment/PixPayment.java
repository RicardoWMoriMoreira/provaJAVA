package com.ecommerce.service.payment;

import com.ecommerce.model.Sale;

import java.security.SecureRandom;
import java.util.Scanner;

public class PixPayment implements PaymentStrategy {

    private final Scanner sc; private String code;

    public PixPayment(Scanner sc){ this.sc=sc; }

    @Override public boolean processPayment(Sale sale){
        System.out.printf("%n=== PIX – Total R$%.2f ===%n",sale.getTotalValue());
        code=gen();
        System.out.println("Chave: "+code);
        System.out.print("Cole para confirmar: ");
        boolean ok=code.equals(sc.nextLine().trim());
        System.out.println(ok?"Confirmado!":"Chave inválida.");
        return ok;
    }
    @Override public String getPaymentDetails(){ return "PIX "+code; }

    private String gen(){
        String c="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        SecureRandom r=new SecureRandom(); StringBuilder sb=new StringBuilder(50);
        for(int i=0;i<50;i++) sb.append(c.charAt(r.nextInt(c.length())));
        return sb.toString();
    }

}