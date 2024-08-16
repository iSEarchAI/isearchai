package br.otimizes.isearchai.adapter;

import br.otimizes.isearchai.learning.MLSearchAlgorithm;

public class MLSearchAlgorithmAdapter extends FrameworkAdapter<MLSearchAlgorithmAdapter> {

    public MLSearchAlgorithmAdapter() {
        super(MLSearchAlgorithm.class);
    }


    public MLSearchAlgorithmAdapter addAttributes() {
        return this;
    }
}
