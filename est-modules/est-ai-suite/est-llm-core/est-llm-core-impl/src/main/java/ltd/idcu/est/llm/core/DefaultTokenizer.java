package ltd.idcu.est.llm.core;

import ltd.idcu.est.llm.core.api.Tokenizer;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultTokenizer implements Tokenizer {
    
    private Map<String, Integer> vocab;
    private Map<Integer, String> idToToken;
    private Set<String> specialTokens;
    private Map<String, Integer> merges;
    private List<String> mergeList;
    
    private String padToken = "[PAD]";
    private String bosToken = "[BOS]";
    private String eosToken = "[EOS]";
    private String unkToken = "[UNK]";
    private String maskToken = "[MASK]";
    
    private static final Pattern PATTERN = Pattern.compile(
        "'s|'t|'re|'ve|'m|'ll|'d| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+"
    );
    
    public DefaultTokenizer() {
        this.vocab = new HashMap<>();
        this.idToToken = new HashMap<>();
        this.specialTokens = new HashSet<>();
        this.merges = new HashMap<>();
        this.mergeList = new ArrayList<>();
        initializeDefaultVocab();
    }
    
    private void initializeDefaultVocab() {
        addSpecialToken(padToken);
        addSpecialToken(bosToken);
        addSpecialToken(eosToken);
        addSpecialToken(unkToken);
        addSpecialToken(maskToken);
        
        for (char c = 32; c < 127; c++) {
            addToken(String.valueOf(c));
        }
    }
    
    @Override
    public List<Integer> encode(String text) {
        return encode(text, true);
    }
    
    @Override
    public List<Integer> encode(String text, boolean addSpecialTokens) {
        List<Integer> tokens = new ArrayList<>();
        
        if (addSpecialTokens) {
            tokens.add(tokenToId(bosToken));
        }
        
        Matcher matcher = PATTERN.matcher(text);
        while (matcher.find()) {
            String token = matcher.group();
            List<String> bpeTokens = bpeTokenize(token);
            for (String bpeToken : bpeTokens) {
                tokens.add(tokenToId(bpeToken));
            }
        }
        
        if (addSpecialTokens) {
            tokens.add(tokenToId(eosToken));
        }
        
        return tokens;
    }
    
    private List<String> bpeTokenize(String token) {
        List<String> chars = new ArrayList<>();
        for (char c : token.toCharArray()) {
            chars.add(String.valueOf(c));
        }
        
        while (chars.size() > 1) {
            String bestMerge = findBestMerge(chars);
            if (bestMerge == null) {
                break;
            }
            
            List<String> newChars = new ArrayList<>();
            int i = 0;
            while (i < chars.size()) {
                if (i < chars.size() - 1) {
                    String pair = chars.get(i) + chars.get(i + 1);
                    if (pair.equals(bestMerge)) {
                        newChars.add(pair);
                        i += 2;
                    } else {
                        newChars.add(chars.get(i));
                        i++;
                    }
                } else {
                    newChars.add(chars.get(i));
                    i++;
                }
            }
            chars = newChars;
        }
        
        return chars;
    }
    
    private String findBestMerge(List<String> chars) {
        String bestMerge = null;
        int bestPriority = Integer.MAX_VALUE;
        
        for (int i = 0; i < chars.size() - 1; i++) {
            String pair = chars.get(i) + chars.get(i + 1);
            Integer priority = merges.get(pair);
            if (priority != null && priority < bestPriority) {
                bestPriority = priority;
                bestMerge = pair;
            }
        }
        
        return bestMerge;
    }
    
    @Override
    public String decode(List<Integer> tokens) {
        return decode(tokens, true);
    }
    
    @Override
    public String decode(List<Integer> tokens, boolean skipSpecialTokens) {
        StringBuilder sb = new StringBuilder();
        for (int tokenId : tokens) {
            String token = idToToken(tokenId);
            if (skipSpecialTokens && isSpecialToken(token)) {
                continue;
            }
            sb.append(token);
        }
        return sb.toString();
    }
    
    @Override
    public List<String> tokenize(String text) {
        List<String> result = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(text);
        while (matcher.find()) {
            result.addAll(bpeTokenize(matcher.group()));
        }
        return result;
    }
    
    @Override
    public int getVocabSize() {
        return vocab.size();
    }
    
    @Override
    public Map<String, Integer> getVocab() {
        return new HashMap<>(vocab);
    }
    
    @Override
    public String idToToken(int id) {
        String token = idToToken.get(id);
        return token != null ? token : unkToken;
    }
    
    @Override
    public int tokenToId(String token) {
        Integer id = vocab.get(token);
        return id != null ? id : vocab.get(unkToken);
    }
    
    @Override
    public boolean isSpecialToken(int id) {
        return isSpecialToken(idToToken(id));
    }
    
    @Override
    public boolean isSpecialToken(String token) {
        return specialTokens.contains(token);
    }
    
    @Override
    public int getPadTokenId() {
        return tokenToId(padToken);
    }
    
    @Override
    public int getBosTokenId() {
        return tokenToId(bosToken);
    }
    
    @Override
    public int getEosTokenId() {
        return tokenToId(eosToken);
    }
    
    @Override
    public int getUnkTokenId() {
        return tokenToId(unkToken);
    }
    
    @Override
    public int getMaskTokenId() {
        return tokenToId(maskToken);
    }
    
    @Override
    public String getPadToken() {
        return padToken;
    }
    
    @Override
    public String getBosToken() {
        return bosToken;
    }
    
    @Override
    public String getEosToken() {
        return eosToken;
    }
    
    @Override
    public String getUnkToken() {
        return unkToken;
    }
    
    @Override
    public String getMaskToken() {
        return maskToken;
    }
    
    @Override
    public void setPadToken(String token) {
        this.padToken = token;
        addSpecialToken(token);
    }
    
    @Override
    public void setBosToken(String token) {
        this.bosToken = token;
        addSpecialToken(token);
    }
    
    @Override
    public void setEosToken(String token) {
        this.eosToken = token;
        addSpecialToken(token);
    }
    
    @Override
    public void setUnkToken(String token) {
        this.unkToken = token;
        addSpecialToken(token);
    }
    
    @Override
    public void setMaskToken(String token) {
        this.maskToken = token;
        addSpecialToken(token);
    }
    
    private void addToken(String token) {
        if (!vocab.containsKey(token)) {
            int id = vocab.size();
            vocab.put(token, id);
            idToToken.put(id, token);
        }
    }
    
    private void addSpecialToken(String token) {
        addToken(token);
        specialTokens.add(token);
    }
    
    @Override
    public void loadVocab(Map<String, Integer> vocab) {
        this.vocab = new HashMap<>(vocab);
        this.idToToken = new HashMap<>();
        for (Map.Entry<String, Integer> entry : vocab.entrySet()) {
            this.idToToken.put(entry.getValue(), entry.getKey());
        }
    }
    
    @Override
    public void addSpecialTokens(List<String> tokens) {
        for (String token : tokens) {
            addSpecialToken(token);
        }
    }
    
    public void addMerges(List<String> merges) {
        this.mergeList = new ArrayList<>(merges);
        this.merges = new HashMap<>();
        for (int i = 0; i < merges.size(); i++) {
            this.merges.put(merges.get(i), i);
        }
    }
    
    @Override
    public void save(String path) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            writer.println("# Vocab");
            for (Map.Entry<String, Integer> entry : vocab.entrySet()) {
                writer.println(entry.getKey() + " " + entry.getValue());
            }
            
            writer.println("\n# Merges");
            for (String merge : mergeList) {
                writer.println(merge);
            }
            
            writer.println("\n# SpecialTokens");
            for (String token : specialTokens) {
                writer.println(token);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tokenizer", e);
        }
    }
    
    @Override
    public void load(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            String section = null;
            
            this.vocab = new HashMap<>();
            this.idToToken = new HashMap<>();
            this.specialTokens = new HashSet<>();
            this.mergeList = new ArrayList<>();
            this.merges = new HashMap<>();
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                if (line.startsWith("#")) {
                    section = line.substring(1).trim();
                    continue;
                }
                
                if ("Vocab".equals(section)) {
                    String[] parts = line.split(" ", 2);
                    if (parts.length == 2) {
                        String token = parts[0];
                        int id = Integer.parseInt(parts[1]);
                        vocab.put(token, id);
                        idToToken.put(id, token);
                    }
                } else if ("Merges".equals(section)) {
                    mergeList.add(line);
                    merges.put(line, mergeList.size() - 1);
                } else if ("SpecialTokens".equals(section)) {
                    specialTokens.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tokenizer", e);
        }
    }
}
