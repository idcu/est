package ltd.idcu.est.llm.core;

import ltd.idcu.est.llm.core.api.FeedForward;
import ltd.idcu.est.llm.core.api.Tensor;

import java.util.ArrayList;
import java.util.List;

public class DefaultFeedForward implements FeedForward {
    
    private final int hiddenSize;
    private final int intermediateSize;
    
    private Tensor firstLayerWeights;
    private Tensor firstLayerBias;
    private Tensor secondLayerWeights;
    private Tensor secondLayerBias;
    
    private String activationFunction = "gelu";
    private boolean training = true;
    
    public DefaultFeedForward(int hiddenSize, int intermediateSize) {
        this.hiddenSize = hiddenSize;
        this.intermediateSize = intermediateSize;
        initializeWeights();
    }
    
    @Override
    public Tensor forward(Tensor hiddenStates) {
        Tensor x = linear(hiddenStates, firstLayerWeights, firstLayerBias);
        x = applyActivation(x);
        x = linear(x, secondLayerWeights, secondLayerBias);
        return x;
    }
    
    private Tensor linear(Tensor input, Tensor weights, Tensor bias) {
        int[] inputShape = input.getShape();
        int batchSize = inputShape[0];
        int seqLength = inputShape[1];
        int inputDim = inputShape[2];
        int outputDim = weights.getShape()[1];
        
        DefaultTensor result = new DefaultTensor(batchSize, seqLength, outputDim);
        
        for (int b = 0; b < batchSize; b++) {
            for (int s = 0; s < seqLength; s++) {
                for (int o = 0; o < outputDim; o++) {
                    float sum = bias != null ? bias.get(o) : 0;
                    for (int i = 0; i < inputDim; i++) {
                        sum += input.get(b, s, i) * weights.get(i, o);
                    }
                    result.set(sum, b, s, o);
                }
            }
        }
        
        return result;
    }
    
    private Tensor applyActivation(Tensor x) {
        switch (activationFunction.toLowerCase()) {
            case "relu":
                return x.relu();
            case "gelu":
                return x.gelu();
            case "sigmoid":
                return x.sigmoid();
            case "tanh":
                return x.tanh();
            default:
                return x.gelu();
        }
    }
    
    @Override
    public int getHiddenSize() {
        return hiddenSize;
    }
    
    @Override
    public int getIntermediateSize() {
        return intermediateSize;
    }
    
    @Override
    public Tensor getFirstLayerWeights() {
        return firstLayerWeights.clone();
    }
    
    @Override
    public Tensor getFirstLayerBias() {
        return firstLayerBias != null ? firstLayerBias.clone() : null;
    }
    
    @Override
    public Tensor getSecondLayerWeights() {
        return secondLayerWeights.clone();
    }
    
    @Override
    public Tensor getSecondLayerBias() {
        return secondLayerBias != null ? secondLayerBias.clone() : null;
    }
    
    @Override
    public void setFirstLayerWeights(Tensor weights) {
        this.firstLayerWeights = weights.clone();
    }
    
    @Override
    public void setFirstLayerBias(Tensor bias) {
        this.firstLayerBias = bias != null ? bias.clone() : null;
    }
    
    @Override
    public void setSecondLayerWeights(Tensor weights) {
        this.secondLayerWeights = weights.clone();
    }
    
    @Override
    public void setSecondLayerBias(Tensor bias) {
        this.secondLayerBias = bias != null ? bias.clone() : null;
    }
    
    @Override
    public String getActivationFunction() {
        return activationFunction;
    }
    
    @Override
    public void setActivationFunction(String activation) {
        this.activationFunction = activation;
    }
    
    @Override
    public void initializeWeights() {
        firstLayerWeights = new DefaultTensor(hiddenSize, intermediateSize);
        secondLayerWeights = new DefaultTensor(intermediateSize, hiddenSize);
        firstLayerBias = new DefaultTensor(intermediateSize);
        secondLayerBias = new DefaultTensor(hiddenSize);
        
        firstLayerWeights.initializeHe();
        secondLayerWeights.initializeHe();
        firstLayerBias.initializeZeros();
        secondLayerBias.initializeZeros();
    }
    
    @Override
    public void loadWeights(String path) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public void saveWeights(String path) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @Override
    public List<Tensor> getParameters() {
        List<Tensor> params = new ArrayList<>();
        params.add(firstLayerWeights);
        params.add(secondLayerWeights);
        if (firstLayerBias != null) params.add(firstLayerBias);
        if (secondLayerBias != null) params.add(secondLayerBias);
        return params;
    }
    
    @Override
    public void setTraining(boolean training) {
        this.training = training;
    }
    
    @Override
    public boolean isTraining() {
        return training;
    }
}
