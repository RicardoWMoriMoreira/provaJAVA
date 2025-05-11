package com.ecommerce.service.payment;

import com.ecommerce.model.enums.PaymentMethod;

import java.util.Scanner;

public class PaymentFactory {
    private static final Scanner SC=new Scanner(System.in);
    public static PaymentStrategy createPaymentStrategy(PaymentMethod m){
        return switch (m){
            case CREDIT_CARD -> new CreditCardPayment(SC);
            case BOLETO     -> new BoletoPayment();
            case PIX        -> new PixPayment(SC);
        };
    }
}