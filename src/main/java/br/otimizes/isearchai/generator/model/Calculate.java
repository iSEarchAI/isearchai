package br.otimizes.isearchai.generator.model;

import java.util.List;

public class Calculate {
    private Boolean invert;
    private List<String> expression;

    public Calculate() {
    }

    public Boolean getInvert() {
        return invert != null && invert;
    }

    public void setInvert(Boolean invert) {
        this.invert = invert;
    }

    public List<String> getExpression() {
        return expression;
    }

    public void setExpression(List<String> expression) {
        this.expression = expression;
    }
}
