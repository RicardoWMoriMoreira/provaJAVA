package com.ecommerce.view;



import com.ecommerce.controller.ProductController;
import com.ecommerce.controller.SaleController;
import com.ecommerce.model.Product;
import com.ecommerce.model.SaleItem;
import com.ecommerce.model.User;
import com.ecommerce.model.enums.PaymentMethod;

import java.util.*;

public class CustomerView {

    private final Scanner sc;
    private final User user;
    private final ProductController pc=new ProductController();
    private final SaleController scSale=new SaleController();

    public CustomerView(Scanner sc,User u){ this.sc=sc; this.user=u; }

    public void showCustomerMenu(){
        boolean run=true;
        while(run){
            System.out.println("\n===== Cliente =====");
            System.out.println("1 Listar produtos");
            System.out.println("2 Buscar produto");
            System.out.println("3 Comprar");
            System.out.println("4 Minhas compras");
            System.out.println("5 Sair");
            System.out.println("\n===================");
            String op=sc.nextLine();
            switch(op){
                case "1"->pc.getAllProducts().stream().filter(p->p.getQuantity()>0).forEach(System.out::println);
                case "2"->find();
                case "3"->buy();
                case "4"->myPurchases();
                case "5"->run=false;
            }
        }
    }

    private void find(){
        System.out.print("Nome: "); String n=sc.nextLine();
        pc.searchProductsByName(n).forEach(System.out::println);
    }

    private void buy(){
        List<SaleItem> cart=new ArrayList<>();
        while(true){
            pc.getAllProducts().stream().filter(p->p.getQuantity()>0).forEach(System.out::println);
            System.out.print("ID ou 'fim': "); String in=sc.nextLine();
            if(in.equalsIgnoreCase("fim")) break;
            Optional<Product> op=pc.getProductById(in);
            if(op.isEmpty()){ System.out.println("Inválido."); continue; }
            Product p=op.get();
            System.out.print("Qtd: "); int q=Integer.parseInt(sc.nextLine());
            if(q<=0||q>p.getQuantity()){ System.out.println("Qtd inválida."); continue; }
            cart.add(new SaleItem(p.getId(),p.getName(),p.getPrice(),q));
        }
        if(cart.isEmpty()) return;
        double total=cart.stream().mapToDouble(SaleItem::getTotal).sum();
        System.out.printf("Total R$%.2f%n",total);
        System.out.println("1 Cartão 2 Boleto 3 PIX"); int op=Integer.parseInt(sc.nextLine());
        PaymentMethod pm=PaymentMethod.fromInt(op);
        scSale.createSale(user,cart,pm);
    }

    private void myPurchases(){
        scSale.getSalesByUser(user.getId()).forEach(System.out::println);
    }

}