package com.ecommerce.view;

import com.ecommerce.controller.ProductController;
import com.ecommerce.controller.SaleController;
import com.ecommerce.model.Product;
import com.ecommerce.model.Sale;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Interface de administração do sistema.
 */
public class AdminView {

    private final Scanner sc;
    private final ProductController pc = new ProductController();
    private final SaleController scSale = new SaleController();

    public AdminView(Scanner sc) {
        this.sc = sc;
    }

    public void showAdminMenu() {
        boolean run = true;
        while (run) {
            System.out.println("\n===== Menu Admin =====");
            System.out.println("1 Listar produtos");
            System.out.println("2 Cadastrar produto");
            System.out.println("3 Editar produto");
            System.out.println("4 Excluir produto");
            System.out.println("5 Buscar produto");
            System.out.println("6 Relatório de vendas");
            System.out.println("7 Sair");
            System.out.println("\n======================");
            System.out.print("Digite a opção escolhida: ");

            String op = sc.nextLine();
            switch (op) {
                case "1" -> listAll();
                case "2" -> add();
                case "3" -> edit();
                case "4" -> del();
                case "5" -> search();
                case "6" -> salesReport();
                case "7" -> run = false;
            }
        }
    }

    /* ---------------------------------------------------------------------- */
    /* --------------------------  CRUD de PRODUTOS  ------------------------ */
    /* ---------------------------------------------------------------------- */

    private void listAll() {
        List<Product> l = pc.getAllProducts();
        if (l.isEmpty()) System.out.println("Sem produtos.");
        else l.forEach(System.out::println);
    }

    private void add() {
        System.out.print("Nome: ");        String name = sc.nextLine();
        System.out.print("Preço: ");       double price = Double.parseDouble(sc.nextLine());
        System.out.print("Qtd: ");         int q = Integer.parseInt(sc.nextLine());
        System.out.print("Descrição: ");   String d = sc.nextLine();
        pc.addProduct(name, price, q, d);
        System.out.println("\n======================");
    }

    /**
     * Edição interativa do produto, permitindo escolher campo a ser alterado.
     */
    private void edit() {
        listAll();
        System.out.print("ID do produto a editar: ");
        String id = sc.nextLine();

        Optional<Product> opt = pc.getProductById(id);
        if (opt.isEmpty()) {

            System.out.println("Produto não encontrado.");
            return;
        }

        Product p = opt.get();
        boolean loop = true;

        while (loop) {
            System.out.println("\n=== Editar Produto ===");
            System.out.println("1 Nome        (" + p.getName()        + ")");
            System.out.println("2 Preço       (" + p.getPrice()       + ")");
            System.out.println("3 Quantidade  (" + p.getQuantity()    + ")");
            System.out.println("4 Descrição   (" + p.getDescription() + ")");
            System.out.println("5 Salvar e sair");
            System.out.println("6 Cancelar");
            System.out.print("Escolha: ");

            switch (sc.nextLine()) {
                case "1" -> {
                    System.out.print("Novo nome: ");
                    String n = sc.nextLine();
                    if (!n.isBlank()) p.setName(n);
                }
                case "2" -> {
                    System.out.print("Novo preço: ");
                    try {
                        double v = Double.parseDouble(sc.nextLine());
                        p.setPrice(v);
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido.");
                    }
                }
                case "3" -> {
                    System.out.print("Nova quantidade: ");
                    try {
                        int q = Integer.parseInt(sc.nextLine());
                        p.setQuantity(q);
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido.");
                    }
                }
                case "4" -> {
                    System.out.print("Nova descrição: ");
                    p.setDescription(sc.nextLine());
                }
                case "5" -> {                 // salva alterações e encerra
                    pc.updateProduct(p);
                    loop = false;
                }
                case "6" -> loop = false;     // cancela sem salvar
                default -> System.out.println("Opção inválida.");
            }
            System.out.println("\n======================");
        }
    }

    private void del() {
        listAll();
        System.out.print("ID: ");
        String id = sc.nextLine();
        pc.deleteProduct(id);
    }

    private void search() {
        System.out.print("Nome: ");
        String n = sc.nextLine();
        pc.searchProductsByName(n).forEach(System.out::println);
    }

    /* ---------------------------------------------------------------------- */
    /* ---------------------------  RELATÓRIOS  ----------------------------- */
    /* ---------------------------------------------------------------------- */

    private void salesReport() {
        List<Sale> s = scSale.getAllSales();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        double tot = 0;
        for (Sale sale : s) {
            System.out.println("\nID " + sale.getId() + " " + sdf.format(sale.getSaleDate()));
            sale.getItems().forEach(System.out::println);
            System.out.println("Total R$" + sale.getTotalValue());
            tot += sale.getTotalValue();
        }
        System.out.println("Receita total: R$" + tot);
    }

    private void top() {
        pc.getTopSellingProducts().forEach(System.out::println);
    }
}
