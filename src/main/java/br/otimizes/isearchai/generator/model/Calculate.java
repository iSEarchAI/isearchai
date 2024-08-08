package br.otimizes.isearchai.generator.model;

public class Calculate {
    private String type;
    private CalculateItem a;
    private CalculateItem b;
    private Boolean invert;

    public Calculate() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CalculateItem getA() {
        return a;
    }

    public void setA(CalculateItem a) {
        this.a = a;
    }

    public CalculateItem getB() {
        return b;
    }

    public void setB(CalculateItem b) {
        this.b = b;
    }

    public Boolean getInvert() {
        return invert;
    }

    public void setInvert(Boolean invert) {
        this.invert = invert;
    }
}
