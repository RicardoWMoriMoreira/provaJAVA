package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductController {

    private final ProductRepository productRepository = new ProductRepository();

    public boolean addProduct(String name, double price, int quantity, String description) {

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Nome do produto não pode ser vazio.");
            return false;
        }
        if (price <= 0) {
            System.out.println("Preço deve ser maior que zero.");
            return false;
        }
        if (quantity < 0) {
            System.out.println("Quantidade não pode ser negativa.");
            return false;
        }

        Product product = new Product(name, price, quantity, description);

        try {
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao adicionar produto: " + e.getMessage());
            return false;
        }
    }

    public List<Product> getAllProducts()                    { return productRepository.findAll(); }
    public Optional<Product> getProductById(String id)       { return productRepository.findById(id); }
    public List<Product> searchProductsByName(String name)   { return productRepository.findByName(name); }
    public List<Product> getTopSellingProducts()             { return productRepository.findTopSellingProducts(); }

    public boolean updateProduct(Product product) {

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            System.out.println("Nome do produto não pode ser vazio.");
            return false;
        }
        if (product.getPrice() <= 0) {
            System.out.println("Preço deve ser maior que zero.");
            return false;
        }
        if (product.getQuantity() < 0) {
            System.out.println("Quantidade não pode ser negativa.");
            return false;
        }

        try {
            return productRepository.update(product);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(String id) {
        try {
            return productRepository.delete(id);
        } catch (Exception e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProductQuantity(String id, int newQuantity) {

        if (newQuantity < 0) {
            System.out.println("Quantidade não pode ser negativa.");
            return false;
        }
        try {
            return productRepository.updateQuantity(id, newQuantity);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar quantidade do produto: " + e.getMessage());
            return false;
        }
    }

}