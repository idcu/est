package ltd.idcu.est.features.cache.memory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter<K> {
    
    private final BitSet bitSet;
    private final int bitSetSize;
    private final int numHashFunctions;
    private final MessageDigest digest;
    
    public BloomFilter(int expectedElements, double falsePositiveProbability) {
        if (expectedElements <= 0) {
            throw new IllegalArgumentException("Expected elements must be positive");
        }
        if (falsePositiveProbability <= 0 || falsePositiveProbability >= 1) {
            throw new IllegalArgumentException("False positive probability must be between 0 and 1");
        }
        
        this.bitSetSize = optimalBitSetSize(expectedElements, falsePositiveProbability);
        this.numHashFunctions = optimalNumOfHashFunctions(expectedElements, bitSetSize);
        this.bitSet = new BitSet(bitSetSize);
        
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    private int optimalBitSetSize(int n, double p) {
        double m = -(n * Math.log(p)) / (Math.log(2) * Math.log(2));
        return (int) Math.ceil(m);
    }
    
    private int optimalNumOfHashFunctions(int n, int m) {
        double k = (double) m / n * Math.log(2);
        return Math.max(1, (int) Math.round(k));
    }
    
    public void add(K key) {
        byte[] bytes = key.toString().getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < numHashFunctions; i++) {
            int hash = hash(bytes, i);
            int index = Math.abs(hash % bitSetSize);
            bitSet.set(index);
        }
    }
    
    public boolean mightContain(K key) {
        byte[] bytes = key.toString().getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < numHashFunctions; i++) {
            int hash = hash(bytes, i);
            int index = Math.abs(hash % bitSetSize);
            if (!bitSet.get(index)) {
                return false;
            }
        }
        return true;
    }
    
    private int hash(byte[] bytes, int seed) {
        digest.reset();
        digest.update((byte) (seed & 0xFF));
        digest.update((byte) ((seed >> 8) & 0xFF));
        digest.update((byte) ((seed >> 16) & 0xFF));
        digest.update((byte) ((seed >> 24) & 0xFF));
        byte[] hashBytes = digest.digest(bytes);
        
        int result = 0;
        for (int i = 0; i < 4 && i < hashBytes.length; i++) {
            result = (result << 8) | (hashBytes[i] & 0xFF);
        }
        return result;
    }
    
    public void clear() {
        bitSet.clear();
    }
    
    public int getBitSetSize() {
        return bitSetSize;
    }
    
    public int getNumHashFunctions() {
        return numHashFunctions;
    }
}
