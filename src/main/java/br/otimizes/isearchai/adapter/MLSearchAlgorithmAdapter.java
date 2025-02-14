package br.otimizes.isearchai.adapter;

import br.otimizes.isearchai.interactive.InteractWithDM;
import br.otimizes.isearchai.core.MLSearchAlgorithm;

/**
 * The type Ml search algorithm adapter.
 */
public class MLSearchAlgorithmAdapter extends FrameworkAdapter<MLSearchAlgorithmAdapter> {

    /**
     * Instantiates a new Ml search algorithm adapter.
     */
    public MLSearchAlgorithmAdapter() {
        super(MLSearchAlgorithm.class);
    }


    /**
     * Add attributes ml search algorithm adapter.
     *
     * @return the ml search algorithm adapter
     */
    public MLSearchAlgorithmAdapter addAttributes() {
        Class clazzToAdd = InteractWithDM.class;
        String fieldName = "interaction";
        return addPrivateField(clazzToAdd, fieldName);
    }
}
