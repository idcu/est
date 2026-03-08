package ltd.idcu.est.llm.core;

import ltd.idcu.est.llm.core.api.*;

import java.util.List;

public class LlmCoreExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST LLM Core Framework Example ===\n");
        
        example1_TensorBasics();
        example2_Tokenizer();
        example3_EmbeddingLayer();
        example4_AttentionMechanism();
        example5_CompleteLlmModel();
    }
    
    private static void example1_TensorBasics() {
        System.out.println("--- Example 1: Tensor Basics ---");
        
        Tensor a = new DefaultTensor(2, 3);
        a.initializeRandom(0, 1);
        System.out.println("Tensor A shape: " + ArraysToString(a.getShape()));
        
        Tensor b = new DefaultTensor(2, 3);
        b.initializeOnes();
        Tensor c = a.add(b);
        System.out.println("Tensor A + B completed");
        
        Tensor d = new DefaultTensor(3, 2);
        d.initializeXavier();
        Tensor e = c.matmul(d);
        System.out.println("Matrix multiplication result shape: " + ArraysToString(e.getShape()));
        
        System.out.println();
    }
    
    private static void example2_Tokenizer() {
        System.out.println("--- Example 2: Tokenizer ---");
        
        Tokenizer tokenizer = new DefaultTokenizer();
        String text = "Hello, this is a test of the EST LLM tokenizer!";
        
        List<Integer> tokens = tokenizer.encode(text);
        System.out.println("Original text: " + text);
        System.out.println("Encoded tokens: " + tokens);
        System.out.println("Number of tokens: " + tokens.size());
        
        String decoded = tokenizer.decode(tokens);
        System.out.println("Decoded text: " + decoded);
        
        System.out.println("Vocabulary size: " + tokenizer.getVocabSize());
        System.out.println();
    }
    
    private static void example3_EmbeddingLayer() {
        System.out.println("--- Example 3: Embedding Layer ---");
        
        int vocabSize = 1000;
        int embeddingDim = 128;
        int maxSeqLength = 512;
        
        Embedding embedding = new DefaultEmbedding(vocabSize, embeddingDim, maxSeqLength);
        
        Tensor inputIds = new DefaultTensor(1, 10);
        for (int i = 0; i < 10; i++) {
            inputIds.set(i, 0, i);
        }
        
        Tensor output = embedding.forward(inputIds);
        System.out.println("Embedding output shape: " + ArraysToString(output.getShape()));
        System.out.println();
    }
    
    private static void example4_AttentionMechanism() {
        System.out.println("--- Example 4: Attention Mechanism ---");
        
        int hiddenSize = 128;
        int numHeads = 8;
        
        Attention attention = new DefaultAttention(hiddenSize, numHeads);
        
        Tensor hiddenStates = new DefaultTensor(1, 10, hiddenSize);
        hiddenStates.initializeRandom(0, 0.1);
        
        Tensor output = attention.forward(hiddenStates);
        System.out.println("Attention output shape: " + ArraysToString(output.getShape()));
        System.out.println();
    }
    
    private static void example5_CompleteLlmModel() {
        System.out.println("--- Example 5: Complete LLM Model ---");
        
        int vocabSize = 100;
        int hiddenSize = 64;
        int numLayers = 2;
        int numHeads = 4;
        int maxPositionEmbeddings = 128;
        
        System.out.println("Creating LLM model...");
        System.out.println("  Vocab size: " + vocabSize);
        System.out.println("  Hidden size: " + hiddenSize);
        System.out.println("  Number of layers: " + numLayers);
        System.out.println("  Number of heads: " + numHeads);
        
        LlmModel model = new DefaultLlmModel(
            vocabSize, 
            hiddenSize, 
            numLayers, 
            numHeads, 
            maxPositionEmbeddings
        );
        
        System.out.println("\nModel created successfully!");
        System.out.println("  Total parameters count: " + model.getParameters().size());
        
        model.setTraining(false);
        
        String prompt = "Once upon a time";
        System.out.println("\nGenerating text from prompt: \"" + prompt + "\"");
        System.out.println("(Note: This is a randomly initialized model, output will be random)");
        
        GenerationConfig config = GenerationConfig.defaultConfig();
        config.setMaxNewTokens(20);
        config.setTemperature(0.7f);
        
        String generated = model.generateText(prompt, 20, config);
        System.out.println("Generated text: " + generated);
        
        System.out.println("\n=== Framework Overview ===");
        System.out.println("EST LLM Core Framework includes:");
        System.out.println("  - Tensor operations (add, multiply, matmul, softmax, etc.)");
        System.out.println("  - BPE Tokenizer with special tokens");
        System.out.println("  - Embedding layer (token + positional)");
        System.out.println("  - Multi-Head Attention mechanism");
        System.out.println("  - Feed-Forward Network");
        System.out.println("  - Transformer blocks with residual connections");
        System.out.println("  - Complete LLM model with text generation");
        System.out.println("  - Training and inference interfaces");
    }
    
    private static String ArraysToString(int[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
