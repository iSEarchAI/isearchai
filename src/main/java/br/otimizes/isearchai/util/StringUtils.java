package br.otimizes.isearchai.util;

/**
 * The type String utils.
 */
public class StringUtils {

    /**
     * Camelcase string.
     *
     * @param fieldName the field name
     * @return the string
     */
    public static String camelcase(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * Is math operator boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isMathOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("%");
    }
}
