package ltd.idcu.est.llm.core;

import ltd.idcu.est.llm.core.api.Embedding;
import ltd.idcu.est.llm.core.api.Tensor;

import java.util.ArrayList;
import java.util.List;

public class DefaultEmbedding implements Embedding {
    
    private final int vocabSize;
    private final int embeddingDim;
    private final int maxPositionEmbeddings;
    
    private Tensor tokenEmbeddings;
    private Tensor positionEmbeddings;
    
    private boolean training = true;
    
    public DefaultEmbedding(int vocabSize, int embeddingDim) {
        this(vocabSize, embeddingDim, 512);
    }
    
    public DefaultEmbedding(int vocabSize, int embeddingDim, int maxPositionEmbeddings) {
        this.vocabSize = vocabSize;
        this.embeddingDim = embeddingDim;
        this.maxPositionEmbeddings = maxPositionEmbeddings;
        initializeWeights();
    }
    
    @Override
    public Tensor forward(Tensor inputIds) {
        int[] shape = inputIds.getShape();
        int batchSize = shape[0];
        int seqLength = shape[1];
        
        Tensor tokenEmb = gatherTokenEmbeddings(inputIds);
        Tensor posEmb = gatherPositionEmbeddings(seqLength);
        
        return tokenEmb.add(posEmb);
    }
    
    private Tensor gatherTokenEmbeddings(Tensor inputIds) {
        int[] shape = inputIds.getShape();
        int batchSize = shape[0];
        int seqLength = shape[1];
        
        DefaultTensor result = new DefaultTensor(batchSize, seqLength, embeddingDim);
        
        for (int b = 0; b < batchSize; b++) {
            for (int s = 0; s < seqLength; s++) {
                int tokenId = (int) inputIds.get(b, s);
                for (int e = 0; e < embeddingDim; e++) {
                    float value = tokenEmbeddings.get(tokenId, e);
                    result.set(value, b, s, e);
                }
            }
        }
        
        return result;
    }
    
    private Tensor gatherPositionEmbeddings(int seqLength) {
        int[] shape = tokenEmbeddings.getShape();
        int batchSize = 1;
        
        DefaultTensor result = new DefaultTensor(batchSize, seqLength, embeddingDim);
        
        for (int s = 0; s < seqLength && s < maxPositionEmbeddings; s++) {
            for (int e = 0; e < embeddingDim; e++) {
                float value = positionEmbeddings.get(s, e);
                result.set(value, 0, s, e);
            }
        }
        
        return result;
    }
    
    @Override
    public Tensor getTokenEmbeddings() {
        return tokenEmbeddings.clone();
    }
    
    @Override
    public Tensor getPositionEmbeddings() {
        return positionEmbeddings.clone();
    }
    
    @Override
    public int getVocabSize() {
        return vocabSize;
    }
    
    @Override
    public int getEmbeddingDim() {
        return embeddingDim;
    }
    
    @Override
    public void setTokenEmbeddings(Tensor embeddings) {
        this.tokenEmbeddings = embeddings.clone();
    }
    
    @Override
    public void setPositionEmbeddings(Tensor embeddings) {
        this.positionEmbeddings = embeddings.clone();
    }
    
    @Override
    public void initializeWeights() {
        this.tokenEmbeddings = new DefaultTensor(vocabSize, embeddingDim);
        this.tokenEmbeddings.initializeXavier();
        
        this.positionEmbeddings = new DefaultTensor(maxPositionEmbeddings, embeddingDim);
        initializeSinusoidalPositionEmbeddings();
    }
    
    private void initializeSinusoidalPositionEmbeddings() {
        for (int pos = 0; pos < maxPositionEmbeddings; pos++) {
            for (int i = 0; i < embeddingDim; i += 2) {
                double divTerm = Math.pow(10000, (2.0 * (i / 2)) / embeddingDim);
                float sinVal = (float) Math.sin(pos / divTerm);
                float cosVal = (float) Math.cos(pos / divTerm);
                
                positionEmbeddings.set(sinVal, pos, i);
                if (i + 1 < embeddingDim) {
                    positionEmbeddings.set(cosVal, pos, i + 1);
                }
            }
        }
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
        params.add(tokenEmbeddings);
        params.add(positionEmbeddings);
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
