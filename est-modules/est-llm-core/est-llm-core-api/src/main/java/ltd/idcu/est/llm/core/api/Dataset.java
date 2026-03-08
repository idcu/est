package ltd.idcu.est.llm.core.api;

import java.util.Iterator;
import java.util.List;

public interface Dataset extends Iterable<Batch> {
    
    int size();
    
    Batch get(int index);
    
    List<Batch> getAll();
    
    Iterator<Batch> iterator();
    
    Dataset shuffle();
    
    Dataset shuffle(long seed);
    
    Dataset split(float trainRatio);
    
    Dataset split(float trainRatio, float validationRatio);
    
    Dataset batch(int batchSize);
    
    boolean isEmpty();
    
    void add(Batch batch);
    
    void addAll(List<Batch> batches);
    
    void clear();
    
    void loadFromFile(String path);
    
    void saveToFile(String path);
}
