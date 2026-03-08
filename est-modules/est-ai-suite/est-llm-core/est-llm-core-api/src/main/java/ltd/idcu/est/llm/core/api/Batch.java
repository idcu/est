package ltd.idcu.est.llm.core.api;

public class Batch {
    
    private Tensor inputIds;
    private Tensor attentionMask;
    private Tensor tokenTypeIds;
    private Tensor labels;
    private Tensor positionIds;
    
    public Batch() {
    }
    
    public Batch(Tensor inputIds) {
        this.inputIds = inputIds;
    }
    
    public Batch(Tensor inputIds, Tensor attentionMask) {
        this.inputIds = inputIds;
        this.attentionMask = attentionMask;
    }
    
    public Batch(Tensor inputIds, Tensor attentionMask, Tensor labels) {
        this.inputIds = inputIds;
        this.attentionMask = attentionMask;
        this.labels = labels;
    }
    
    public Tensor getInputIds() {
        return inputIds;
    }
    
    public void setInputIds(Tensor inputIds) {
        this.inputIds = inputIds;
    }
    
    public Tensor getAttentionMask() {
        return attentionMask;
    }
    
    public void setAttentionMask(Tensor attentionMask) {
        this.attentionMask = attentionMask;
    }
    
    public Tensor getTokenTypeIds() {
        return tokenTypeIds;
    }
    
    public void setTokenTypeIds(Tensor tokenTypeIds) {
        this.tokenTypeIds = tokenTypeIds;
    }
    
    public Tensor getLabels() {
        return labels;
    }
    
    public void setLabels(Tensor labels) {
        this.labels = labels;
    }
    
    public Tensor getPositionIds() {
        return positionIds;
    }
    
    public void setPositionIds(Tensor positionIds) {
        this.positionIds = positionIds;
    }
    
    public boolean hasAttentionMask() {
        return attentionMask != null;
    }
    
    public boolean hasTokenTypeIds() {
        return tokenTypeIds != null;
    }
    
    public boolean hasLabels() {
        return labels != null;
    }
    
    public boolean hasPositionIds() {
        return positionIds != null;
    }
}
