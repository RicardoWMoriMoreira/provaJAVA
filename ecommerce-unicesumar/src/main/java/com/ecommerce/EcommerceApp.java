package com.ecommerce;

import com.ecommerce.util.DatabaseUtil;
import com.ecommerce.view.MainView;

public class EcommerceApp {
    public static void main(String[] args){
        DatabaseUtil.initializeDatabase();
        new MainView().start();
    }
}