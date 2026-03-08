package ltd.idcu.est.llm.core.api;

import java.util.List;
import java.util.Map;

public interface Tokenizer {
    
    List<Integer> encode(String text);
    
    List<Integer> encode(String text, boolean addSpecialTokens);
    
    String decode(List<Integer> tokens);
    
    String decode(List<Integer> tokens, boolean skipSpecialTokens);
    
    List<String> tokenize(String text);
    
    int getVocabSize();
    
    Map<String, Integer> getVocab();
    
    String idToToken(int id);
    
    int tokenToId(String token);
    
    boolean isSpecialToken(int id);
    
    boolean isSpecialToken(String token);
    
    int getPadTokenId();
    
    int getBosTokenId();
    
    int getEosTokenId();
    
    int getUnkTokenId();
    
    int getMaskTokenId();
    
    String getPadToken();
    
    String getBosToken();
    
    String getEosToken();
    
    String getUnkToken();
    
    String getMaskToken();
    
    void setPadToken(String token);
    
    void setBosToken(String token);
    
    void setEosToken(String token);
    
    void setUnkToken(String token);
    
    void setMaskToken(String token);
    
    void loadVocab(Map<String, Integer> vocab);
    
    void addSpecialTokens(List<String> tokens);
    
    void save(String path);
    
    void load(String path);
}
