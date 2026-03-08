package ltd.idcu.est.llm.core;

import ltd.idcu.est.llm.core.api.Attention;
import ltd.idcu.est.llm.core.api.FeedForward;
import ltd.idcu.est.llm.core.api.Tensor;
import ltd.idcu.est.llm.core.api.TransformerBlock;

import java.util.ArrayList;
import java.util.List;

public class DefaultTransformerBlock implements TransformerBlock {
    
    private final int hiddenSize;
    private final Attention attention;
    private final FeedForward feedForward;
    
    private Tensor inputLayerNormWeights;
    private Tensor inputLayerNormBias;
    private Tensor postAttentionLayerNormWeights;
    private Tensor postAttentionLayerNormBias;
    
    private float layerNormEpsilon = 1e-5f;
    private boolean training = true;
    
    public DefaultTransformerBlock(int hiddenSize, int numHeads, int intermediateSize) {
        this.hiddenSize = hiddenSize;
        this.attention = new DefaultAttention(hiddenSize, numHeads);
        this.feedForward = new DefaultFeedForward(hiddenSize, intermediateSize);
        initializeWeights();
    }
    
    @Override
    public Tensor forward(Tensor hiddenStates) {
        return forward(hiddenStates, null);
    }
    
    @Override
    public Tensor forward(Tensor hiddenStates, Tensor attentionMask) {
        Tensor residual = hiddenStates;
        
        Tensor normalized = applyLayerNorm(hiddenStates, inputLayerNormWeights, inputLayerNormBias);
        Tensor attnOutput = attention.forward(normalized, attentionMask);
        hiddenStates = residual.add(attnOutput);
        
        residual = hiddenStates;
        normalized = applyLayerNorm(hiddenStates, postAttentionLayerNormWeights, postAttentionLayerNormBias);
        Tensor ffOutput = feedForward.forward(normalized);
        hiddenStates = residual.add(ffOutput);
        
        return hiddenStates;
    }
    
    private Tensor applyLayerNorm(Tensor x, Tensor weights, Tensor bias) {
        int[] shape = x.getShape();
        int lastAxis = shape.length - 1;
        
        Tensor mean = x.mean(lastAxis);
        Tensor var = computeVariance(x, mean, lastAxis);
        
        DefaultTensor result = new DefaultTensor(shape);
        
        for (int b = 0; b < shape[0]; b++) {
            for (int s = 0; s < shape[1]; s++) {
                float m = mean.get(b);
                float v = var.get(b);
                for (int h = 0; h < shape[2]; h++) {
                    float normalized = (x.get(b, s, h) - m) / (float) Math.sqrt(v + layerNormEpsilon);
                    float output = normalized * weights.get(h) + bias.get(h);
                    result.set(output, b, s, h);
                }
            }
        }
        
        return result;
    }
    
    private Tensor computeVariance(Tensor x, Tensor mean, int axis) {
        int[] meanShape = mean.getShape();
        DefaultTensor var = new DefaultTensor(meanShape);
        var.initializeZeros();
        
        int[] xShape = x.getShape();
        int count = xShape[axis];
        
        for (int b = 0; b < xShape[0]; b++) {
            float m = mean.get(b);
            float sum = 0;
            for (int s = 0; s < xShape[1]; s++) {
                for (int h = 0; h < xShape[2]; h++) {
                    float diff = x.get(b, s, h) - m;
                    sum += diff * diff;
                }
            }
            var.set(sum / (count * xShape[2]), b);
        }
        
        return var;
    }
    
    @Override
    public Attention getAttention() {
        return attention;
    }
    
    @Override
    public FeedForward getFeedForward() {
        return feedForward;
    }
    
    @Override
    public Tensor getInputLayerNormWeights() {
        return inputLayerNormWeights.clone();
    }
    
    @Override
    public Tensor getInputLayerNormBias() {
        return inputLayerNormBias.clone();
    }
    
    @Override
    public Tensor getPostAttentionLayerNormWeights() {
        return postAttentionLayerNormWeights.clone();
    }
    
    @Override
    public Tensor getPostAttentionLayerNormBias() {
        return postAttentionLayerNormBias.clone();
    }
    
    @Override
    public void setInputLayerNormWeights(Tensor weights) {
        this.inputLayerNormWeights = weights.clone();
    }
    
    @Override
    public void setInputLayerNormBias(Tensor bias) {
        this.inputLayerNormBias = bias.clone();
    }
    
    @Override
    public void setPostAttentionLayerNormWeights(Tensor weights) {
        this.postAttentionLayerNormWeights = weights.clone();
    }
    
    @Override
    public void setPostAttentionLayerNormBias(Tensor bias) {
        this.postAttentionLayerNormBias = bias.clone();
    }
    
    @Override
    public float getLayerNormEpsilon() {
        return layerNormEpsilon;
    }
    
    @Override
    public void setLayerNormEpsilon(float epsilon) {
        this.layerNormEpsilon = epsilon;
    }
    
    @Override
    public int getHiddenSize() {
        return hiddenSize;
    }
    
    @Override
    public void initializeWeights() {
        inputLayerNormWeights = new DefaultTensor(hiddenSize);
        inputLayerNormBias = new DefaultTensor(hiddenSize);
        postAttentionLayerNormWeights = new DefaultTensor(hiddenSize);
        postAttentionLayerNormBias = new DefaultTensor(hiddenSize);
        
        inputLayerNormWeights.initializeOnes();
        postAttentionLayerNormWeights.initializeOnes();
        inputLayerNormBias.initializeZeros();
        postAttentionLayerNormBias.initializeZeros();
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
        params.addAll(attention.getParameters());
        params.addAll(feedForward.getParameters());
        params.add(inputLayerNormWeights);
        params.add(inputLayerNormBias);
        params.add(postAttentionLayerNormWeights);
        params.add(postAttentionLayerNormBias);
        return params;
    }
    
    @Override
    public void setTraining(boolean training) {
        this.training = training;
        this.attention.setTraining(training);
        this.feedForward.setTraining(training);
    }
    
    @Override
    public boolean isTraining() {
        return training;
    }
}
