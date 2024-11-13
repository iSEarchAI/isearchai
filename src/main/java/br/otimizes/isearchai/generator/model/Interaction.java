package br.otimizes.isearchai.generator.model;

public class Interaction {
    private Integer max = 3;
    private Integer interval = 3;
    private Integer first = 3;

    public Interaction() {
    }


    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }
}
