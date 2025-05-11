package com.ecommerce.util;

import java.util.regex.*;

public class EmailValidator {
    private static final Pattern P=Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    public static boolean isValid(String e){ return e!=null && P.matcher(e).matches(); }
}