package ltd.idcu.est.llm.core.api;

import java.util.List;

public interface Tensor {
    
    int[] getShape();
    
    int getRank();
    
    int getSize();
    
    float get(int... indices);
    
    void set(float value, int... indices);
    
    Tensor slice(int[] starts, int[] ends);
    
    Tensor reshape(int... newShape);
    
    Tensor transpose(int... permutation);
    
    Tensor add(Tensor other);
    
    Tensor subtract(Tensor other);
    
    Tensor multiply(Tensor other);
    
    Tensor divide(Tensor other);
    
    Tensor matmul(Tensor other);
    
    Tensor scale(float scalar);
    
    Tensor sum(int axis);
    
    Tensor mean(int axis);
    
    Tensor max(int axis);
    
    Tensor softmax(int axis);
    
    Tensor relu();
    
    Tensor gelu();
    
    Tensor sigmoid();
    
    Tensor tanh();
    
    Tensor dropout(float dropoutRate, boolean training);
    
    Tensor layerNorm(float epsilon);
    
    float[] toArray();
    
    List<Tensor> split(int axis, int numSplits);
    
    Tensor concatenate(List<Tensor> tensors, int axis);
    
    Tensor clone();
    
    void initializeRandom(float mean, float std);
    
    void initializeXavier();
    
    void initializeHe();
    
    void initializeZeros();
    
    void initializeOnes();
}
