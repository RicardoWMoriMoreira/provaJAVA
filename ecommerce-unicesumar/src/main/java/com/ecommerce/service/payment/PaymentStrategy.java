package com.ecommerce.service.payment;

import com.ecommerce.model.Sale;

public interface PaymentStrategy {
    boolean processPayment(Sale sale);
    String getPaymentDetails();
}