package br.otimizes.isearchai.adapter;

import br.otimizes.isearchai.interactive.InteractWithDM;
import br.otimizes.isearchai.learning.ml.MLSearchAlgorithm;

public class MLSearchAlgorithmAdapter extends FrameworkAdapter<MLSearchAlgorithmAdapter> {

    public MLSearchAlgorithmAdapter() {
        super(MLSearchAlgorithm.class);
    }


    public MLSearchAlgorithmAdapter addAttributes() {
        Class clazzToAdd = InteractWithDM.class;
        String fieldName = "interaction";
        return addPrivateField(clazzToAdd, fieldName);
    }
}
