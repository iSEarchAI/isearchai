package br.otimizes.isearchai.learning.nautilus;

import br.otimizes.isearchai.learning.MLElement;
import org.uma.jmetal.util.binarySet.BinarySet;

public class MLBinarySet extends BinarySet implements MLElement {

    private boolean freezed;

    /**
     * Constructor
     *
     * @param numberOfBits
     */
    public MLBinarySet(int numberOfBits) {
        super(numberOfBits);
    }

    @Override
    public boolean setFreezeFromDM(boolean v) {
        this.freezed = v;
        return v;
    }

    @Override
    public boolean isFreezeByDM() {
        return freezed;
    }
}