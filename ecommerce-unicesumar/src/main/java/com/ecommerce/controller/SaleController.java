package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.model.Sale;
import com.ecommerce.model.SaleItem;
import com.ecommerce.model.User;
import com.ecommerce.model.enums.PaymentMethod;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.SaleRepository;
import com.ecommerce.service.payment.PaymentFactory;
import com.ecommerce.service.payment.PaymentStrategy;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SaleController {

    private final SaleRepository saleRepo = new SaleRepository();
    private final ProductRepository productRepo = new ProductRepository();

    public boolean createSale(User user, List<SaleItem> items, PaymentMethod paymentMethod) {

        if (user == null || items == null || items.isEmpty()) {
            System.out.println("Dados da venda inválidos.");
            return false;
        }

        for (SaleItem item : items) {
            Optional<Product> opt = productRepo.findById(item.getProductId());
            if (opt.isEmpty() || opt.get().getQuantity() < item.getQuantity()) {
                System.out.println("Estoque insuficiente para: " + item.getProductName());
                return false;
            }
        }

        PaymentStrategy strategy = PaymentFactory.createPaymentStrategy(paymentMethod);

        Sale sale = new Sale(user.getId(), user.getName(), paymentMethod);
        sale.setSaleDate(new Date());
        items.forEach(sale::addItem);

        if (!strategy.processPayment(sale)) {
            System.out.println("Pagamento não aprovado.");
            return false;
        }

        try {
            saleRepo.saveWithStockUpdate(sale);
            System.out.println("Venda registrada com sucesso.");
            return true;
        } catch (Exception ex) {
            System.out.println("Falha ao registrar venda: " + ex.getMessage());
            return false;
        }
    }

    public List<Sale> getAllSales()            { return saleRepo.findAll(); }
    public List<Sale> getSalesByUser(String u) { return saleRepo.findByUserId(u); }

}