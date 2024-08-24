package br.otimizes.isearchai.learning.encoding.integer;

public class NumberFormatException extends IllegalArgumentException {
    static final long serialVersionUID = -2848938806368998894L;

    /**
     * Constructs a <code>NumberFormatException</code> with no detail message.
     */
    public NumberFormatException () {
        super();
    }

    /**
     * Constructs a <code>NumberFormatException</code> with the
     * specified detail message.
     *
     * @param   s   the detail message.
     */
    public NumberFormatException (String s) {
        super (s);
    }

    /**
     * Factory method for making a <code>NumberFormatException</code>
     * given the specified input which caused the error.
     *
     * @param   s   the input causing the error
     */
    static java.lang.NumberFormatException forInputString(String s) {
        return new java.lang.NumberFormatException("For input string: \"" + s + "\"");
    }
}
