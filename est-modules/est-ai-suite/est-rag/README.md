# EST RAG - 检索增强生成模块

**版本**: 3.0.0  
**状态**: ✅ 开发中

---

## 📋 目录

1. [简介](#简介)
2. [快速开始](#快速开始)
3. [核心组件](#核心组件)
4. [API 参考](#api-参考)
5. [示例代码](#示例代码)

---

## 🎯 简介

EST RAG 是 EST AI Suite 的检索增强生成（Retrieval-Augmented Generation）模块，提供了完整的文档处理、向量存储、检索和生成功能。

### 主要特性

- 📄 **文档分块** - 支持多种分块策略（固定大小、语义分块）
- 🗄️ **向量存储** - 统一的向量存储接口，支持多种后端
- 🔍 **智能检索** - 支持向量相似度检索、混合检索
- 🤖 **生成增强** - 与 LLM 无缝集成，提供检索增强的问答能力
- 🔌 **可扩展** - 轻松添加自定义分块器、向量存储和嵌入模型

---

## 🚀 快速开始

### 添加依赖

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-rag-api</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-rag-impl</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>
```

### 最简单的用法

```java
import ltd.idcu.est.rag.api.*;
import ltd.idcu.est.rag.impl.*;

public class RagQuickStart {
    public static void main(String[] args) {
        RagEngine ragEngine = new DefaultRagEngine();
        
        Document doc = new Document("doc1", "EST Framework 是一个企业级 Java 开发框架。它提供了模块化设计、零依赖核心架构等特性。");
        ragEngine.addDocument(doc);
        
        String answer = ragEngine.retrieveAndGenerate("EST Framework 是什么？", 3);
        System.out.println(answer);
    }
}
```

### 完整配置

```java
import ltd.idcu.est.rag.api.*;
import ltd.idcu.est.rag.impl.*;
import ltd.idcu.est.llm.api.LlmClient;

public class RagFullExample {
    public static void main(String[] args) {
        TextSplitter splitter = new FixedSizeTextSplitter(1024, 200);
        EmbeddingModel embeddingModel = new SimpleEmbeddingModel();
        VectorStore vectorStore = new InMemoryVectorStore(embeddingModel);
        LlmClient llmClient = createYourLlmClient();
        
        DefaultRagEngine ragEngine = new DefaultRagEngine(
            vectorStore, splitter, embeddingModel, llmClient
        );
        
        Document doc1 = new Document("doc1", "EST AI Suite 提供了强大的 AI 功能支持...");
        Document doc2 = new Document("doc2", "EST Framework 支持多语言 SDK...");
        
        ragEngine.addDocuments(List.of(doc1, doc2));
        
        List<SearchResult> results = ragEngine.retrieve("EST AI", 5);
        for (SearchResult result : results) {
            System.out.println("得分: " + result.getScore());
            System.out.println("内容: " + result.getContent());
        }
        
        String answer = ragEngine.retrieveAndGenerate("EST AI 有什么功能？", 3);
        System.out.println(answer);
    }
}
```

---

## 🏗️ 核心组件

### 1. Document（文档）

表示一个完整的文档。

```java
Document doc = new Document();
doc.setId("doc-001");
doc.setTitle("EST Framework 文档");
doc.setContent("EST Framework 是一个...");
doc.setSource("https://est.example.com/docs");
doc.setContentType("text/markdown");
```

### 2. TextSplitter（文本分块器）

将文档分割成适合向量化的小块。

#### FixedSizeTextSplitter（固定大小分块器）

```java
FixedSizeTextSplitter splitter = new FixedSizeTextSplitter();
splitter.setChunkSize(512);
splitter.setChunkOverlap(100);

List<DocumentChunk> chunks = splitter.split(document);
```

### 3. EmbeddingModel（嵌入模型）

将文本转换为向量。

#### SimpleEmbeddingModel（简单嵌入模型）

```java
EmbeddingModel model = new SimpleEmbeddingModel();
float[] vector = model.embedToVector("Hello, world!");
```

### 4. VectorStore（向量存储）

存储和检索向量。

#### InMemoryVectorStore（内存向量存储）

```java
VectorStore store = new InMemoryVectorStore(embeddingModel);
store.addEmbedding(embedding);

List<SearchResult> results = store.search("查询文本", 5);
```

### 5. RagEngine（RAG 引擎）

整合所有组件，提供端到端的 RAG 功能。

```java
DefaultRagEngine engine = new DefaultRagEngine();
engine.addDocument(document);
String answer = engine.retrieveAndGenerate("问题", 3);
```

---

## 📚 API 参考

### RagEngine 接口

```java
public interface RagEngine {
    void addDocument(Document document);
    void addDocuments(List<Document> documents);
    List<SearchResult> retrieve(String query, int topK);
    String generate(String query, List<SearchResult> contexts);
    String retrieveAndGenerate(String query, int topK);
    
    void setVectorStore(VectorStore vectorStore);
    void setTextSplitter(TextSplitter textSplitter);
    void setEmbeddingModel(EmbeddingModel embeddingModel);
    
    VectorStore getVectorStore();
    TextSplitter getTextSplitter();
    EmbeddingModel getEmbeddingModel();
}
```

### VectorStore 接口

```java
public interface VectorStore {
    void addEmbeddings(List<Embedding> embeddings);
    void addEmbedding(Embedding embedding);
    List<SearchResult> search(float[] queryVector, int topK);
    List<SearchResult> search(String query, int topK);
    void delete(String id);
    void deleteByDocumentId(String documentId);
    void clear();
    int size();
    
    void setEmbeddingModel(EmbeddingModel embeddingModel);
    EmbeddingModel getEmbeddingModel();
}
```

### TextSplitter 接口

```java
public interface TextSplitter {
    List<DocumentChunk> split(Document document);
    List<DocumentChunk> split(String text, String documentId);
    void setChunkSize(int chunkSize);
    void setChunkOverlap(int chunkOverlap);
    int getChunkSize();
    int getChunkOverlap();
}
```

---

## 💡 示例代码

### 批量添加文档

```java
List<Document> docs = new ArrayList<>();
for (int i = 0; i < 100; i++) {
    Document doc = new Document("doc-" + i, "文档 " + i + " 的内容...");
    docs.add(doc);
}
ragEngine.addDocuments(docs);
```

### 自定义分块策略

```java
public class CustomSplitter implements TextSplitter {
    @Override
    public List<DocumentChunk> split(Document document) {
        List<DocumentChunk> chunks = new ArrayList<>();
        String[] paragraphs = document.getContent().split("\n\n");
        for (int i = 0; i < paragraphs.length; i++) {
            DocumentChunk chunk = new DocumentChunk(
                UUID.randomUUID().toString(),
                document.getId(),
                paragraphs[i],
                i
            );
            chunks.add(chunk);
        }
        return chunks;
    }
}
```

### 集成 LLM 客户端

```java
LlmClient llmClient = OpenAiLlmClient.builder()
    .apiKey("your-api-key")
    .model("gpt-4")
    .build();

DefaultRagEngine ragEngine = new DefaultRagEngine();
ragEngine.setLlmClient(llmClient);

String answer = ragEngine.retrieveAndGenerate("EST 是什么？", 5);
```

---

## 📖 相关文档

- [EST AI Suite README](../README.md) - AI Suite 总览
- [EST LLM README](../est-llm/README.md) - LLM 模块文档
- [EST AI Assistant README](../est-ai-assistant/README.md) - AI 助手文档

---

**文档生成时间**: 2026-03-10  
**文档作者**: EST AI Team
