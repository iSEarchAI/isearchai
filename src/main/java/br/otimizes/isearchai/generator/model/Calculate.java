package br.otimizes.isearchai.generator.model;

import java.util.List;


/**
 * The type Calculate.
 * <p>
 * Indicates how will be the formula of your objective functions.
 * Example: ["Sum","/","Cost"]
 */
public class Calculate {
    private Boolean invert;
    private List<String> expression;

    /**
     * Instantiates a new Calculate.
     */
    public Calculate() {
    }

    /**
     * Gets invert.
     *
     * @return the invert
     */
    public Boolean getInvert() {
        return invert != null && invert;
    }

    /**
     * Sets invert.
     *
     * @param invert the invert
     */
    public void setInvert(Boolean invert) {
        this.invert = invert;
    }

    /**
     * Gets expression.
     *
     * @return the expression
     */
    public List<String> getExpression() {
        return expression;
    }

    /**
     * Sets expression.
     *
     * @param expression the expression
     */
    public void setExpression(List<String> expression) {
        this.expression = expression;
    }
}
