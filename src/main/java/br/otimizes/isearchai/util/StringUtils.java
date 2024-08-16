package br.otimizes.isearchai.util;

public class StringUtils {

    public static String camelcase(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static boolean isMathOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("%");
    }
}
