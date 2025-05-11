package com.ecommerce.service.payment;

import com.ecommerce.model.Sale;

import java.util.Scanner;

public class CreditCardPayment implements PaymentStrategy {

    private final Scanner sc;
    private String card;

    public CreditCardPayment(Scanner sc){ this.sc=sc; }

    @Override public boolean processPayment(Sale sale){
        System.out.printf("%n=== Cartão – Total R$%.2f ===%n",sale.getTotalValue());
        System.out.print("Número: "); card=sc.nextLine().trim();
        System.out.print("Nome: ");    sc.nextLine();
        System.out.print("Validade: "); sc.nextLine();
        System.out.print("CVV: ");      sc.nextLine();
        boolean ok=card.matches("\\d{13,19}");
        System.out.println(ok?"Pagamento aprovado!":"Dados inválidos.");
        return ok;
    }
    @Override public String getPaymentDetails(){ return "Cartão ****‑"+card.substring(card.length()-4); }

}