package br.otimizes.isearchai.generator.model;

/**
 * The type Interaction.
 * <p>
 * Specify the max interactions, the interval (after x generations the user interacts), and which is the first generation
 * of interaction. Note that this numbers can be another features of search process besides the generation.
 */
public class Interaction {
    private Integer max = 3;
    private Integer interval = 3;
    private Integer first = 3;

    /**
     * Instantiates a new Interaction.
     */
    public Interaction() {
    }


    /**
     * Gets max.
     *
     * @return the max
     */
    public Integer getMax() {
        return max;
    }

    /**
     * Sets max.
     *
     * @param max the max
     */
    public void setMax(Integer max) {
        this.max = max;
    }

    /**
     * Gets interval.
     *
     * @return the interval
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * Sets interval.
     *
     * @param interval the interval
     */
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    /**
     * Gets first.
     *
     * @return the first
     */
    public Integer getFirst() {
        return first;
    }

    /**
     * Sets first.
     *
     * @param first the first
     */
    public void setFirst(Integer first) {
        this.first = first;
    }
}
