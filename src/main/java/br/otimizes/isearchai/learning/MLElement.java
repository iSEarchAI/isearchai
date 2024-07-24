package br.otimizes.isearchai.learning;

public interface MLElement {
    public boolean setFreezeFromDM(double v);

    public boolean setFreezeFromDM();

    public boolean isFreezeByDM();

    public void setFreezedByCluster();

    public <E extends MLElement> boolean totalyEquals(E e);

    public float getNumberId();
}
