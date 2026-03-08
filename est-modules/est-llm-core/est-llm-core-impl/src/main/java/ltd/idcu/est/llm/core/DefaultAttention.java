package ltd.idcu.est.llm.core;

import ltd.idcu.est.llm.core.api.Attention;
import ltd.idcu.est.llm.core.api.Tensor;

import java.util.ArrayList;
import java.util.List;

public class DefaultAttention implements Attention {
    
    private final int hiddenSize;
    private final int numHeads;
    private final int headDim;
    
    private Tensor queryWeights;
    private Tensor keyWeights;
    private Tensor valueWeights;
    private Tensor outputWeights;
    
    private Tensor queryBias;
    private Tensor keyBias;
    private Tensor valueBias;
    private Tensor outputBias;
    
    private boolean causal = true;
    private boolean training = true;
    
    public DefaultAttention(int hiddenSize, int numHeads) {
        this.hiddenSize = hiddenSize;
        this.numHeads = numHeads;
        this.headDim = hiddenSize / numHeads;
        initializeWeights();
    }
    
    @Override
    public Tensor forward(Tensor hiddenStates) {
        return forward(hiddenStates, null, null);
    }
    
    @Override
    public Tensor forward(Tensor hiddenStates, Tensor attentionMask) {
        return forward(hiddenStates, attentionMask, null);
    }
    
    @Override
    public Tensor forward(Tensor hiddenStates, Tensor attentionMask, Tensor keyValueStates) {
        int[] shape = hiddenStates.getShape();
        int batchSize = shape[0];
        int seqLength = shape[1];
        
        Tensor q = linear(hiddenStates, queryWeights, queryBias);
        Tensor k = linear(hiddenStates, keyWeights, keyBias);
        Tensor v = linear(hiddenStates, valueWeights, valueBias);
        
        q = splitHeads(q, batchSize, seqLength);
        k = splitHeads(k, batchSize, seqLength);
        v = splitHeads(v, batchSize, seqLength);
        
        Tensor attentionScores = computeAttentionScores(q, k);
        
        if (causal) {
            attentionScores = applyCausalMask(attentionScores, seqLength);
        }
        
        if (attentionMask != null) {
            attentionScores = applyAttentionMask(attentionScores, attentionMask);
        }
        
        Tensor attentionWeights = attentionScores.softmax(-1);
        Tensor output = attentionWeights.matmul(v);
        
        output = mergeHeads(output, batchSize, seqLength);
        
        return linear(output, outputWeights, outputBias);
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
    
    private Tensor splitHeads(Tensor x, int batchSize, int seqLength) {
        DefaultTensor result = new DefaultTensor(batchSize, numHeads, seqLength, headDim);
        
        for (int b = 0; b < batchSize; b++) {
            for (int s = 0; s < seqLength; s++) {
                for (int h = 0; h < numHeads; h++) {
                    for (int d = 0; d < headDim; d++) {
                        int idx = h * headDim + d;
                        result.set(x.get(b, s, idx), b, h, s, d);
                    }
                }
            }
        }
        
        return result;
    }
    
    private Tensor mergeHeads(Tensor x, int batchSize, int seqLength) {
        DefaultTensor result = new DefaultTensor(batchSize, seqLength, hiddenSize);
        
        for (int b = 0; b < batchSize; b++) {
            for (int s = 0; s < seqLength; s++) {
                for (int h = 0; h < numHeads; h++) {
                    for (int d = 0; d < headDim; d++) {
                        int idx = h * headDim + d;
                        result.set(x.get(b, h, s, d), b, s, idx);
                    }
                }
            }
        }
        
        return result;
    }
    
    private Tensor computeAttentionScores(Tensor q, Tensor k) {
        int batchSize = q.getShape()[0];
        int numHeads = q.getShape()[1];
        int seqLengthQ = q.getShape()[2];
        int seqLengthK = k.getShape()[2];
        
        DefaultTensor scores = new DefaultTensor(batchSize, numHeads, seqLengthQ, seqLengthK);
        float scale = 1.0f / (float) Math.sqrt(headDim);
        
        for (int b = 0; b < batchSize; b++) {
            for (int h = 0; h < numHeads; h++) {
                for (int i = 0; i < seqLengthQ; i++) {
                    for (int j = 0; j < seqLengthK; j++) {
                        float sum = 0;
                        for (int d = 0; d < headDim; d++) {
                            sum += q.get(b, h, i, d) * k.get(b, h, j, d);
                        }
                        scores.set(sum * scale, b, h, i, j);
                    }
                }
            }
        }
        
        return scores;
    }
    
    private Tensor applyCausalMask(Tensor scores, int seqLength) {
        DefaultTensor result = (DefaultTensor) scores.clone();
        
        for (int b = 0; b < result.getShape()[0]; b++) {
            for (int h = 0; h < result.getShape()[1]; h++) {
                for (int i = 0; i < seqLength; i++) {
                    for (int j = i + 1; j < seqLength; j++) {
                        result.set(Float.NEGATIVE_INFINITY, b, h, i, j);
                    }
                }
            }
        }
        
        return result;
    }
    
    private Tensor applyAttentionMask(Tensor scores, Tensor mask) {
        DefaultTensor result = (DefaultTensor) scores.clone();
        
        int[] scoreShape = scores.getShape();
        int[] maskShape = mask.getShape();
        
        for (int b = 0; b < scoreShape[0]; b++) {
            for (int h = 0; h < scoreShape[1]; h++) {
                for (int i = 0; i < scoreShape[2]; i++) {
                    for (int j = 0; j < scoreShape[3]; j++) {
                        float m = maskShape.length == 2 ? mask.get(b, j) : mask.get(b, i, j);
                        if (m == 0) {
                            result.set(Float.NEGATIVE_INFINITY, b, h, i, j);
                        }
                    }
                }
            }
        }
        
        return result;
    }
    
    @Override
    public int getHiddenSize() {
        return hiddenSize;
    }
    
    @Override
    public int getNumHeads() {
        return numHeads;
    }
    
    @Override
    public int getHeadDim() {
        return headDim;
    }
    
    @Override
    public boolean isCausal() {
        return causal;
    }
    
    @Override
    public void setCausal(boolean causal) {
        this.causal = causal;
    }
    
    @Override
    public Tensor getQueryWeights() {
        return queryWeights.clone();
    }
    
    @Override
    public Tensor getKeyWeights() {
        return keyWeights.clone();
    }
    
    @Override
    public Tensor getValueWeights() {
        return valueWeights.clone();
    }
    
    @Override
    public Tensor getOutputWeights() {
        return outputWeights.clone();
    }
    
    @Override
    public Tensor getQueryBias() {
        return queryBias != null ? queryBias.clone() : null;
    }
    
    @Override
    public Tensor getKeyBias() {
        return keyBias != null ? keyBias.clone() : null;
    }
    
    @Override
    public Tensor getValueBias() {
        return valueBias != null ? valueBias.clone() : null;
    }
    
    @Override
    public Tensor getOutputBias() {
        return outputBias != null ? outputBias.clone() : null;
    }
    
    @Override
    public void setQueryWeights(Tensor weights) {
        this.queryWeights = weights.clone();
    }
    
    @Override
    public void setKeyWeights(Tensor weights) {
        this.keyWeights = weights.clone();
    }
    
    @Override
    public void setValueWeights(Tensor weights) {
        this.valueWeights = weights.clone();
    }
    
    @Override
    public void setOutputWeights(Tensor weights) {
        this.outputWeights = weights.clone();
    }
    
    @Override
    public void setQueryBias(Tensor bias) {
        this.queryBias = bias != null ? bias.clone() : null;
    }
    
    @Override
    public void setKeyBias(Tensor bias) {
        this.keyBias = bias != null ? bias.clone() : null;
    }
    
    @Override
    public void setValueBias(Tensor bias) {
        this.valueBias = bias != null ? bias.clone() : null;
    }
    
    @Override
    public void setOutputBias(Tensor bias) {
        this.outputBias = bias != null ? bias.clone() : null;
    }
    
    @Override
    public void initializeWeights() {
        queryWeights = new DefaultTensor(hiddenSize, hiddenSize);
        keyWeights = new DefaultTensor(hiddenSize, hiddenSize);
        valueWeights = new DefaultTensor(hiddenSize, hiddenSize);
        outputWeights = new DefaultTensor(hiddenSize, hiddenSize);
        
        queryBias = new DefaultTensor(hiddenSize);
        keyBias = new DefaultTensor(hiddenSize);
        valueBias = new DefaultTensor(hiddenSize);
        outputBias = new DefaultTensor(hiddenSize);
        
        queryWeights.initializeXavier();
        keyWeights.initializeXavier();
        valueWeights.initializeXavier();
        outputWeights.initializeXavier();
        
        queryBias.initializeZeros();
        keyBias.initializeZeros();
        valueBias.initializeZeros();
        outputBias.initializeZeros();
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
        params.add(queryWeights);
        params.add(keyWeights);
        params.add(valueWeights);
        params.add(outputWeights);
        if (queryBias != null) params.add(queryBias);
        if (keyBias != null) params.add(keyBias);
        if (valueBias != null) params.add(valueBias);
        if (outputBias != null) params.add(outputBias);
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
