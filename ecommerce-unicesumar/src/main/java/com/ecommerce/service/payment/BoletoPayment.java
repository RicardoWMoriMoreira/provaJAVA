package com.ecommerce.service.payment;

import com.ecommerce.model.Sale;

import java.util.Random;

public class BoletoPayment implements PaymentStrategy {
    private String code;

    @Override public boolean processPayment(Sale sale){
        System.out.printf("%n=== Pagamento Boleto – Total R$%.2f ===%n",sale.getTotalValue());
        code=generate();
        System.out.println("Código: "+code+"\nPague em 3 dias úteis.");
        return true;
    }
    @Override public String getPaymentDetails(){ return "Boleto "+code; }

    private String generate(){
        Random r=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<5;i++){
            for(int j=0;j<10;j++) sb.append(r.nextInt(10));
            if(i<4) sb.append(' ');
        }
        return sb.toString();
    }

}