package br.otimizes.isearchai.learning.encoding.binary;

import br.otimizes.isearchai.core.MLElement;
import org.uma.jmetal.util.binarySet.BinarySet;

/**
 * The type Ml binary set.
 */
public class MLBinarySet extends BinarySet implements MLElement {

    private boolean freezed;

    /**
     * Constructor
     *
     * @param numberOfBits the number of bits
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
