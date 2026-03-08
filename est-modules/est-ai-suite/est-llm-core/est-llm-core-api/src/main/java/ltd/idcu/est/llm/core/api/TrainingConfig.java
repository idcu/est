package ltd.idcu.est.llm.core.api;

public class TrainingConfig {
    
    private int batchSize = 32;
    private int epochs = 3;
    private float learningRate = 1e-4f;
    private float weightDecay = 0.01f;
    private float gradientClipNorm = 1.0f;
    private int warmupSteps = 100;
    private int loggingSteps = 10;
    private int saveSteps = 100;
    private int evalSteps = 100;
    private String outputDir = "./output";
    private boolean useMixedPrecision = false;
    private int numWorkers = 4;
    private int maxGradientsAccumulationSteps = 1;
    private float dropoutRate = 0.1f;
    private String optimizer = "AdamW";
    private String scheduler = "cosine";
    private float lrEndFactor = 0.1f;
    
    public TrainingConfig() {
    }
    
    public static TrainingConfig defaultConfig() {
        return new TrainingConfig();
    }
    
    public static TrainingConfig quickTestConfig() {
        TrainingConfig config = new TrainingConfig();
        config.setBatchSize(8);
        config.setEpochs(1);
        config.setLoggingSteps(1);
        config.setSaveSteps(10);
        config.setEvalSteps(10);
        return config;
    }
    
    public int getBatchSize() {
        return batchSize;
    }
    
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
    
    public int getEpochs() {
        return epochs;
    }
    
    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }
    
    public float getLearningRate() {
        return learningRate;
    }
    
    public void setLearningRate(float learningRate) {
        this.learningRate = learningRate;
    }
    
    public float getWeightDecay() {
        return weightDecay;
    }
    
    public void setWeightDecay(float weightDecay) {
        this.weightDecay = weightDecay;
    }
    
    public float getGradientClipNorm() {
        return gradientClipNorm;
    }
    
    public void setGradientClipNorm(float gradientClipNorm) {
        this.gradientClipNorm = gradientClipNorm;
    }
    
    public int getWarmupSteps() {
        return warmupSteps;
    }
    
    public void setWarmupSteps(int warmupSteps) {
        this.warmupSteps = warmupSteps;
    }
    
    public int getLoggingSteps() {
        return loggingSteps;
    }
    
    public void setLoggingSteps(int loggingSteps) {
        this.loggingSteps = loggingSteps;
    }
    
    public int getSaveSteps() {
        return saveSteps;
    }
    
    public void setSaveSteps(int saveSteps) {
        this.saveSteps = saveSteps;
    }
    
    public int getEvalSteps() {
        return evalSteps;
    }
    
    public void setEvalSteps(int evalSteps) {
        this.evalSteps = evalSteps;
    }
    
    public String getOutputDir() {
        return outputDir;
    }
    
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
    
    public boolean isUseMixedPrecision() {
        return useMixedPrecision;
    }
    
    public void setUseMixedPrecision(boolean useMixedPrecision) {
        this.useMixedPrecision = useMixedPrecision;
    }
    
    public int getNumWorkers() {
        return numWorkers;
    }
    
    public void setNumWorkers(int numWorkers) {
        this.numWorkers = numWorkers;
    }
    
    public int getMaxGradientsAccumulationSteps() {
        return maxGradientsAccumulationSteps;
    }
    
    public void setMaxGradientsAccumulationSteps(int maxGradientsAccumulationSteps) {
        this.maxGradientsAccumulationSteps = maxGradientsAccumulationSteps;
    }
    
    public float getDropoutRate() {
        return dropoutRate;
    }
    
    public void setDropoutRate(float dropoutRate) {
        this.dropoutRate = dropoutRate;
    }
    
    public String getOptimizer() {
        return optimizer;
    }
    
    public void setOptimizer(String optimizer) {
        this.optimizer = optimizer;
    }
    
    public String getScheduler() {
        return scheduler;
    }
    
    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }
    
    public float getLrEndFactor() {
        return lrEndFactor;
    }
    
    public void setLrEndFactor(float lrEndFactor) {
        this.lrEndFactor = lrEndFactor;
    }
}
