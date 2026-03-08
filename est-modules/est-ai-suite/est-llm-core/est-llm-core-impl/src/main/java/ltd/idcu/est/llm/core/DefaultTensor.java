package ltd.idcu.est.llm.core;

import ltd.idcu.est.llm.core.api.Tensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DefaultTensor implements Tensor {
    
    private final float[] data;
    private final int[] shape;
    private final int[] strides;
    private final Random random = new Random();
    
    public DefaultTensor(int... shape) {
        this.shape = shape.clone();
        this.strides = computeStrides(shape);
        int size = computeSize(shape);
        this.data = new float[size];
    }
    
    public DefaultTensor(float[] data, int... shape) {
        this.shape = shape.clone();
        this.strides = computeStrides(shape);
        int size = computeSize(shape);
        if (data.length != size) {
            throw new IllegalArgumentException("Data length doesn't match shape");
        }
        this.data = data.clone();
    }
    
    private int[] computeStrides(int[] shape) {
        int[] strides = new int[shape.length];
        int stride = 1;
        for (int i = shape.length - 1; i >= 0; i--) {
            strides[i] = stride;
            stride *= shape[i];
        }
        return strides;
    }
    
    private int computeSize(int[] shape) {
        int size = 1;
        for (int dim : shape) {
            size *= dim;
        }
        return size;
    }
    
    private int computeIndex(int... indices) {
        if (indices.length != shape.length) {
            throw new IllegalArgumentException("Number of indices doesn't match rank");
        }
        int index = 0;
        for (int i = 0; i < indices.length; i++) {
            index += indices[i] * strides[i];
        }
        return index;
    }
    
    @Override
    public int[] getShape() {
        return shape.clone();
    }
    
    @Override
    public int getRank() {
        return shape.length;
    }
    
    @Override
    public int getSize() {
        return data.length;
    }
    
    @Override
    public float get(int... indices) {
        return data[computeIndex(indices)];
    }
    
    @Override
    public void set(float value, int... indices) {
        data[computeIndex(indices)] = value;
    }
    
    @Override
    public Tensor slice(int[] starts, int[] ends) {
        if (starts.length != shape.length || ends.length != shape.length) {
            throw new IllegalArgumentException("Starts and ends must match shape length");
        }
        
        int[] newShape = new int[shape.length];
        for (int i = 0; i < shape.length; i++) {
            newShape[i] = ends[i] - starts[i];
        }
        
        DefaultTensor result = new DefaultTensor(newShape);
        copySlice(this, result, starts, new int[shape.length], newShape);
        return result;
    }
    
    private void copySlice(DefaultTensor src, DefaultTensor dst, int[] srcStart, int[] dstStart, int[] size) {
        int rank = size.length;
        int[] srcIndices = srcStart.clone();
        int[] dstIndices = dstStart.clone();
        
        copySliceRecursive(src, dst, srcIndices, dstIndices, size, 0);
    }
    
    private void copySliceRecursive(DefaultTensor src, DefaultTensor dst, int[] srcIndices, int[] dstIndices, int[] size, int dim) {
        if (dim == size.length) {
            dst.set(src.get(srcIndices), dstIndices);
            return;
        }
        
        for (int i = 0; i < size[dim]; i++) {
            srcIndices[dim] = srcIndices[dim] + i;
            dstIndices[dim] = dstIndices[dim] + i;
            copySliceRecursive(src, dst, srcIndices, dstIndices, size, dim + 1);
            srcIndices[dim] = srcIndices[dim] - i;
            dstIndices[dim] = dstIndices[dim] - i;
        }
    }
    
    @Override
    public Tensor reshape(int... newShape) {
        int newSize = computeSize(newShape);
        if (newSize != data.length) {
            throw new IllegalArgumentException("New shape size doesn't match data size");
        }
        return new DefaultTensor(data.clone(), newShape);
    }
    
    @Override
    public Tensor transpose(int... permutation) {
        if (permutation.length != shape.length) {
            throw new IllegalArgumentException("Permutation length must match rank");
        }
        
        int[] newShape = new int[shape.length];
        for (int i = 0; i < shape.length; i++) {
            newShape[i] = shape[permutation[i]];
        }
        
        DefaultTensor result = new DefaultTensor(newShape);
        int[] indices = new int[shape.length];
        transposeRecursive(this, result, permutation, indices, 0);
        return result;
    }
    
    private void transposeRecursive(DefaultTensor src, DefaultTensor dst, int[] permutation, int[] indices, int dim) {
        if (dim == src.shape.length) {
            int[] dstIndices = new int[indices.length];
            for (int i = 0; i < indices.length; i++) {
                dstIndices[permutation[i]] = indices[i];
            }
            dst.set(src.get(indices), dstIndices);
            return;
        }
        
        for (int i = 0; i < src.shape[dim]; i++) {
            indices[dim] = i;
            transposeRecursive(src, dst, permutation, indices, dim + 1);
        }
    }
    
    @Override
    public Tensor add(Tensor other) {
        checkShapeCompatibility(other);
        DefaultTensor result = new DefaultTensor(shape);
        for (int i = 0; i < data.length; i++) {
            result.data[i] = this.data[i] + ((DefaultTensor) other).data[i];
        }
        return result;
    }
    
    @Override
    public Tensor subtract(Tensor other) {
        checkShapeCompatibility(other);
        DefaultTensor result = new DefaultTensor(shape);
        for (int i = 0; i < data.length; i++) {
            result.data[i] = this.data[i] - ((DefaultTensor) other).data[i];
        }
        return result;
    }
    
    @Override
    public Tensor multiply(Tensor other) {
        checkShapeCompatibility(other);
        DefaultTensor result = new DefaultTensor(shape);
        for (int i = 0; i < data.length; i++) {
            result.data[i] = this.data[i] * ((DefaultTensor) other).data[i];
        }
        return result;
    }
    
    @Override
    public Tensor divide(Tensor other) {
        checkShapeCompatibility(other);
        DefaultTensor result = new DefaultTensor(shape);
        for (int i = 0; i < data.length; i++) {
            result.data[i] = this.data[i] / ((DefaultTensor) other).data[i];
        }
        return result;
    }
    
    @Override
    public Tensor matmul(Tensor other) {
        if (this.shape.length != 2 || other.getShape().length != 2) {
            throw new IllegalArgumentException("Matrix multiplication requires 2D tensors");
        }
        if (this.shape[1] != other.getShape()[0]) {
            throw new IllegalArgumentException("Inner dimensions don't match");
        }
        
        int m = this.shape[0];
        int k = this.shape[1];
        int n = other.getShape()[1];
        
        DefaultTensor result = new DefaultTensor(m, n);
        DefaultTensor o = (DefaultTensor) other;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                float sum = 0;
                for (int l = 0; l < k; l++) {
                    sum += this.get(i, l) * o.get(l, j);
                }
                result.set(sum, i, j);
            }
        }
        return result;
    }
    
    @Override
    public Tensor scale(float scalar) {
        DefaultTensor result = new DefaultTensor(shape);
        for (int i = 0; i < data.length; i++) {
            result.data[i] = this.data[i] * scalar;
        }
        return result;
    }
    
    @Override
    public Tensor sum(int axis) {
        if (axis < 0 || axis >= shape.length) {
            throw new IllegalArgumentException("Invalid axis");
        }
        
        int[] newShape = new int[shape.length - 1];
        int idx = 0;
        for (int i = 0; i < shape.length; i++) {
            if (i != axis) {
                newShape[idx++] = shape[i];
            }
        }
        
        DefaultTensor result = new DefaultTensor(newShape);
        int[] indices = new int[shape.length];
        sumRecursive(result, indices, 0, axis);
        return result;
    }
    
    private void sumRecursive(DefaultTensor result, int[] indices, int dim, int sumAxis) {
        if (dim == shape.length) {
            int[] resultIndices = new int[indices.length - 1];
            int idx = 0;
            for (int i = 0; i < indices.length; i++) {
                if (i != sumAxis) {
                    resultIndices[idx++] = indices[i];
                }
            }
            float current = resultIndices.length == 0 ? result.data[0] : result.get(resultIndices);
            result.set(current + this.get(indices), resultIndices);
            return;
        }
        
        if (dim == sumAxis) {
            for (int i = 0; i < shape[dim]; i++) {
                indices[dim] = i;
                sumRecursive(result, indices, dim + 1, sumAxis);
            }
        } else {
            for (int i = 0; i < shape[dim]; i++) {
                indices[dim] = i;
                sumRecursive(result, indices, dim + 1, sumAxis);
            }
        }
    }
    
    @Override
    public Tensor mean(int axis) {
        Tensor sum = sum(axis);
        int count = shape[axis];
        return sum.scale(1.0f / count);
    }
    
    @Override
    public Tensor max(int axis) {
        if (axis < 0 || axis >= shape.length) {
            throw new IllegalArgumentException("Invalid axis");
        }
        
        int[] newShape = new int[shape.length - 1];
        int idx = 0;
        for (int i = 0; i < shape.length; i++) {
            if (i != axis) {
                newShape[idx++] = shape[i];
            }
        }
        
        DefaultTensor result = new DefaultTensor(newShape);
        for (int i = 0; i < result.data.length; i++) {
            result.data[i] = Float.NEGATIVE_INFINITY;
        }
        
        int[] indices = new int[shape.length];
        maxRecursive(result, indices, 0, axis);
        return result;
    }
    
    private void maxRecursive(DefaultTensor result, int[] indices, int dim, int maxAxis) {
        if (dim == shape.length) {
            int[] resultIndices = new int[indices.length - 1];
            int idx = 0;
            for (int i = 0; i < indices.length; i++) {
                if (i != maxAxis) {
                    resultIndices[idx++] = indices[i];
                }
            }
            float current = resultIndices.length == 0 ? result.data[0] : result.get(resultIndices);
            float value = this.get(indices);
            if (value > current) {
                result.set(value, resultIndices);
            }
            return;
        }
        
        if (dim == maxAxis) {
            for (int i = 0; i < shape[dim]; i++) {
                indices[dim] = i;
                maxRecursive(result, indices, dim + 1, maxAxis);
            }
        } else {
            for (int i = 0; i < shape[dim]; i++) {
                indices[dim] = i;
                maxRecursive(result, indices, dim + 1, maxAxis);
            }
        }
    }
    
    @Override
    public Tensor softmax(int axis) {
        Tensor maxVal = max(axis);
        Tensor expValues = subtractAndExp(maxVal, axis);
        Tensor sumExp = expValues.sum(axis);
        return expValues.divideBroadcast(sumExp, axis);
    }
    
    private Tensor subtractAndExp(Tensor maxVal, int axis) {
        DefaultTensor result = new DefaultTensor(shape);
        int[] indices = new int[shape.length];
        subtractAndExpRecursive((DefaultTensor) maxVal, result, indices, 0, axis);
        return result;
    }
    
    private void subtractAndExpRecursive(DefaultTensor maxVal, DefaultTensor result, int[] indices, int dim, int axis) {
        if (dim == shape.length) {
            int[] maxIndices = new int[indices.length - 1];
            int idx = 0;
            for (int i = 0; i < indices.length; i++) {
                if (i != axis) {
                    maxIndices[idx++] = indices[i];
                }
            }
            float m = maxIndices.length == 0 ? maxVal.data[0] : maxVal.get(maxIndices);
            float exp = (float) Math.exp(this.get(indices) - m);
            result.set(exp, indices);
            return;
        }
        
        for (int i = 0; i < shape[dim]; i++) {
            indices[dim] = i;
            subtractAndExpRecursive(maxVal, result, indices, dim + 1, axis);
        }
    }
    
    private Tensor divideBroadcast(Tensor sumExp, int axis) {
        DefaultTensor result = new DefaultTensor(shape);
        int[] indices = new int[shape.length];
        divideBroadcastRecursive((DefaultTensor) sumExp, result, indices, 0, axis);
        return result;
    }
    
    private void divideBroadcastRecursive(DefaultTensor sumExp, DefaultTensor result, int[] indices, int dim, int axis) {
        if (dim == shape.length) {
            int[] sumIndices = new int[indices.length - 1];
            int idx = 0;
            for (int i = 0; i < indices.length; i++) {
                if (i != axis) {
                    sumIndices[idx++] = indices[i];
                }
            }
            float s = sumIndices.length == 0 ? sumExp.data[0] : sumExp.get(sumIndices);
            result.set(this.get(indices) / s, indices);
            return;
        }
        
        for (int i = 0; i < shape[dim]; i++) {
            indices[dim] = i;
            divideBroadcastRecursive(sumExp, result, indices, dim + 1, axis);
        }
    }
    
    @Override
    public Tensor relu() {
        DefaultTensor result = new DefaultTensor(shape);
        for (int i = 0; i < data.length; i++) {
            result.data[i] = Math.max(0, data[i]);
        }
        return result;
    }
    
    @Override
    public Tensor gelu() {
        DefaultTensor result = new DefaultTensor(shape);
        for (int i = 0; i < data.length; i++) {
            float x = data[i];
            double cdf = 0.5 * (1.0 + Math.tanh(Math.sqrt(2.0 / Math.PI) * (x + 0.044715 * Math.pow(x, 3))));
            result.data[i] = (float) (x * cdf);
        }
        return result;
    }
    
    @Override
    public Tensor sigmoid() {
        DefaultTensor result = new DefaultTensor(shape);
        for (int i = 0; i < data.length; i++) {
            result.data[i] = (float) (1.0 / (1.0 + Math.exp(-data[i])));
        }
        return result;
    }
    
    @Override
    public Tensor tanh() {
        DefaultTensor result = new DefaultTensor(shape);
        for (int i = 0; i < data.length; i++) {
            result.data[i] = (float) Math.tanh(data[i]);
        }
        return result;
    }
    
    @Override
    public Tensor dropout(float dropoutRate, boolean training) {
        if (!training) {
            return this;
        }
        
        DefaultTensor result = new DefaultTensor(shape);
        float scale = 1.0f / (1.0f - dropoutRate);
        for (int i = 0; i < data.length; i++) {
            if (random.nextFloat() > dropoutRate) {
                result.data[i] = data[i] * scale;
            } else {
                result.data[i] = 0;
            }
        }
        return result;
    }
    
    @Override
    public Tensor layerNorm(float epsilon) {
        int lastAxis = shape.length - 1;
        Tensor mean = mean(lastAxis);
        Tensor var = variance(mean, lastAxis);
        
        DefaultTensor result = new DefaultTensor(shape);
        int[] indices = new int[shape.length];
        layerNormRecursive((DefaultTensor) mean, (DefaultTensor) var, epsilon, result, indices, 0, lastAxis);
        return result;
    }
    
    private Tensor variance(Tensor mean, int axis) {
        DefaultTensor result = new DefaultTensor(mean.getShape());
        int[] indices = new int[shape.length];
        varianceRecursive((DefaultTensor) mean, result, indices, 0, axis);
        return result;
    }
    
    private void varianceRecursive(DefaultTensor mean, DefaultTensor result, int[] indices, int dim, int axis) {
        if (dim == shape.length) {
            int[] meanIndices = new int[indices.length - 1];
            int idx = 0;
            for (int i = 0; i < indices.length; i++) {
                if (i != axis) {
                    meanIndices[idx++] = indices[i];
                }
            }
            float m = meanIndices.length == 0 ? mean.data[0] : mean.get(meanIndices);
            float diff = this.get(indices) - m;
            float var = meanIndices.length == 0 ? result.data[0] : result.get(meanIndices);
            result.set(var + diff * diff, meanIndices);
            return;
        }
        
        if (dim == axis) {
            for (int i = 0; i < shape[dim]; i++) {
                indices[dim] = i;
                varianceRecursive(mean, result, indices, dim + 1, axis);
            }
            if (result.shape.length > 0) {
                int[] resultIndices = new int[shape.length - 1];
                for (int i = 0; i < resultIndices.length; i++) {
                    resultIndices[i] = 0;
                }
                normalizeVariance(result, resultIndices, 0, shape[axis]);
            }
        } else {
            for (int i = 0; i < shape[dim]; i++) {
                indices[dim] = i;
                varianceRecursive(mean, result, indices, dim + 1, axis);
            }
        }
    }
    
    private void normalizeVariance(DefaultTensor result, int[] indices, int dim, int count) {
        if (dim == result.shape.length) {
            float current = indices.length == 0 ? result.data[0] : result.get(indices);
            result.set(current / count, indices);
            return;
        }
        
        for (int i = 0; i < result.shape[dim]; i++) {
            indices[dim] = i;
            normalizeVariance(result, indices, dim + 1, count);
        }
    }
    
    private void layerNormRecursive(DefaultTensor mean, DefaultTensor var, float epsilon, DefaultTensor result, int[] indices, int dim, int axis) {
        if (dim == shape.length) {
            int[] mvIndices = new int[indices.length - 1];
            int idx = 0;
            for (int i = 0; i < indices.length; i++) {
                if (i != axis) {
                    mvIndices[idx++] = indices[i];
                }
            }
            float m = mvIndices.length == 0 ? mean.data[0] : mean.get(mvIndices);
            float v = mvIndices.length == 0 ? var.data[0] : var.get(mvIndices);
            float normalized = (this.get(indices) - m) / (float) Math.sqrt(v + epsilon);
            result.set(normalized, indices);
            return;
        }
        
        for (int i = 0; i < shape[dim]; i++) {
            indices[dim] = i;
            layerNormRecursive(mean, var, epsilon, result, indices, dim + 1, axis);
        }
    }
    
    @Override
    public float[] toArray() {
        return data.clone();
    }
    
    @Override
    public List<Tensor> split(int axis, int numSplits) {
        if (axis < 0 || axis >= shape.length) {
            throw new IllegalArgumentException("Invalid axis");
        }
        if (shape[axis] % numSplits != 0) {
            throw new IllegalArgumentException("Axis size not divisible by numSplits");
        }
        
        int splitSize = shape[axis] / numSplits;
        List<Tensor> results = new ArrayList<>();
        
        for (int i = 0; i < numSplits; i++) {
            int[] starts = new int[shape.length];
            int[] ends = new int[shape.length];
            for (int j = 0; j < shape.length; j++) {
                if (j == axis) {
                    starts[j] = i * splitSize;
                    ends[j] = (i + 1) * splitSize;
                } else {
                    starts[j] = 0;
                    ends[j] = shape[j];
                }
            }
            results.add(slice(starts, ends));
        }
        
        return results;
    }
    
    @Override
    public Tensor concatenate(List<Tensor> tensors, int axis) {
        if (tensors.isEmpty()) {
            throw new IllegalArgumentException("Cannot concatenate empty list");
        }
        
        int[] firstShape = tensors.get(0).getShape();
        for (Tensor t : tensors) {
            int[] s = t.getShape();
            if (s.length != firstShape.length) {
                throw new IllegalArgumentException("All tensors must have same rank");
            }
            for (int i = 0; i < s.length; i++) {
                if (i != axis && s[i] != firstShape[i]) {
                    throw new IllegalArgumentException("All tensors must have same dimensions except axis");
                }
            }
        }
        
        int[] newShape = firstShape.clone();
        int totalAxisSize = 0;
        for (Tensor t : tensors) {
            totalAxisSize += t.getShape()[axis];
        }
        newShape[axis] = totalAxisSize;
        
        DefaultTensor result = new DefaultTensor(newShape);
        int[] indices = new int[newShape.length];
        int offset = 0;
        
        for (Tensor t : tensors) {
            concatenateRecursive((DefaultTensor) t, result, offset, indices, 0, axis);
            offset += t.getShape()[axis];
        }
        
        return result;
    }
    
    private void concatenateRecursive(DefaultTensor src, DefaultTensor dst, int offset, int[] indices, int dim, int axis) {
        if (dim == src.shape.length) {
            int[] dstIndices = indices.clone();
            dstIndices[axis] += offset;
            dst.set(src.get(indices), dstIndices);
            return;
        }
        
        for (int i = 0; i < src.shape[dim]; i++) {
            indices[dim] = i;
            concatenateRecursive(src, dst, offset, indices, dim + 1, axis);
        }
    }
    
    @Override
    public Tensor clone() {
        return new DefaultTensor(data.clone(), shape);
    }
    
    @Override
    public void initializeRandom(float mean, float std) {
        for (int i = 0; i < data.length; i++) {
            data[i] = (float) (random.nextGaussian() * std + mean);
        }
    }
    
    @Override
    public void initializeXavier() {
        float scale = (float) Math.sqrt(2.0 / (shape[0] + shape[shape.length - 1]));
        initializeRandom(0, scale);
    }
    
    @Override
    public void initializeHe() {
        float scale = (float) Math.sqrt(2.0 / shape[0]);
        initializeRandom(0, scale);
    }
    
    @Override
    public void initializeZeros() {
        Arrays.fill(data, 0);
    }
    
    @Override
    public void initializeOnes() {
        Arrays.fill(data, 1);
    }
    
    private void checkShapeCompatibility(Tensor other) {
        if (!Arrays.equals(this.shape, other.getShape())) {
            throw new IllegalArgumentException("Shape mismatch");
        }
    }
}
