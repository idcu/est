# EST Serverless Examples

This example demonstrates how to use EST Framework Serverless features.

## Supported Cloud Platforms

- **AWS Lambda** - Amazon Web Services serverless computing
- **Azure Functions** - Microsoft Azure serverless computing
- **Alibaba Cloud Function Compute** - Alibaba Cloud Serverless computing service
- **Google Cloud Functions** - Google Cloud Platform serverless computing

## Project Structure

```
est-examples-serverless/
├── src/main/java/ltd/idcu/est/examples/serverless/
│   ├── HelloWorldFunction.java          # Hello World Example Function
│   ├── CalculatorFunction.java          # Calculator Example Function
│   ├── OptimizedFunction.java           # Cold Start Optimization Example Function
│   ├── ServerlessLocalRunnerExample.java # Local Runner Example
│   ├── aws/                              # AWS Lambda Handlers
│   │   ├── HelloWorldLambdaHandler.java
│   │   └── CalculatorLambdaHandler.java
│   ├── azure/                            # Azure Functions Handlers
│   │   ├── HelloWorldAzureHandler.java
│   │   └── CalculatorAzureHandler.java
│   ├── alibaba/                          # Alibaba Cloud Function Compute Handlers
│   │   ├── HelloWorldFcHandler.java
│   │   └── CalculatorFcHandler.java
│   └── google/                           # Google Cloud Functions Handlers
│       ├── HelloWorldGoogleHandler.java
│       └── CalculatorGoogleHandler.java
├── pom.xml
└── README.md
```

## Quick Start

### Hello World Function

A simple greeting function that accepts a `name` parameter and returns a greeting.

**Request Example:**
```
GET /hello?name=EST
```

**Response Example:**
```json
{
  "message": "Hello, EST!",
  "version": "1.0.0"
}
```

### Calculator Function

A calculator function supporting add, subtract, multiply, and divide operations.

**Request Parameters:**
- `op`: Operation type (add/sub/mul/div)
- `a`: First number
- `b`: Second number

**Request Example:**
```
GET /calculator?op=add&a=10&b=5
GET /calculator?op=mul&a=3&b=4
GET /calculator?op=div&a=15&b=3
```

**Response Example:**
```json
{
  "operation": "addition",
  "a": 10.0,
  "b": 5.0,
  "result": 15.0
}
```

### Optimized Function

Optimized function example using ColdStartOptimizer, demonstrating cold start optimization, caching, and statistics features.

**Request Parameters:**
- `action`: Action type (status/cache/stats/reset)

**Request Example:**
```
GET /optimized?action=status
GET /optimized?action=cache
GET /optimized?action=stats
GET /optimized?action=reset
```

**Response Example (status):**
```json
{
  "status": "ok",
  "function": "optimized-function",
  "initialized": true,
  "cache_size": 2
}
```

**Response Example (stats):**
```json
{
  "totalColdStarts": 1,
  "totalWarmStarts": 5,
  "coldStartRate": 0.166,
  "functions": {...}
}
```

## Platform Usage Guides

### AWS Lambda

1. Build project
```bash
mvn clean package
```

2. Deploy to AWS Lambda
   - Use AWS SAM template: Reference `deploy/serverless/aws/template.yaml`
   - Or manually upload JAR package through AWS Console

3. Handler configuration
   - HelloWorld: `ltd.idcu.est.examples.serverless.aws.HelloWorldLambdaHandler`
   - Calculator: `ltd.idcu.est.examples.serverless.aws.CalculatorLambdaHandler`

### Azure Functions

1. Build project
```bash
mvn clean package
```

2. Deploy to Azure Functions
   - Reference configuration files in `deploy/serverless/azure/` directory
   - Use Azure Functions Core Tools for deployment

3. Handler configuration
   - HelloWorld: `ltd.idcu.est.examples.serverless.azure.HelloWorldAzureHandler`
   - Calculator: `ltd.idcu.est.examples.serverless.azure.CalculatorAzureHandler`

### Alibaba Cloud Function Compute

1. Build project
```bash
mvn clean package
```

2. Deploy to Alibaba Cloud Function Compute
   - Use Serverless Devs: Reference `deploy/serverless/alibaba/template.yml`
   - Or deploy through Alibaba Cloud console

3. Handler configuration
   - HelloWorld: `ltd.idcu.est.examples.serverless.alibaba.HelloWorldFcHandler`
   - Calculator: `ltd.idcu.est.examples.serverless.alibaba.CalculatorFcHandler`

### Google Cloud Functions

1. Build project
```bash
mvn clean package
```

2. Deploy to Google Cloud Functions
   - Use gcloud CLI for deployment
   - Reference `deploy/serverless/google/README.md`

3. Handler configuration
   - HelloWorld: `ltd.idcu.est.examples.serverless.google.HelloWorldGoogleHandler`
   - Calculator: `ltd.idcu.est.examples.serverless.google.CalculatorGoogleHandler`

## Create Your Own Serverless Function

### 1. Implement HttpServerlessFunction

```java
public class MyFunction implements HttpServerlessFunction {
    
    @Override
    public ServerlessResponse handle(ServerlessRequest request, Map<String, Object> context) {
        String body = "{\"message\":\"Hello from my function!\"}";
        ServerlessResponse response = ServerlessResponse.ok(body);
        response.addHeader("Content-Type", "application/json");
        return response;
    }
}
```

### 2. Create Platform-Specific Handler

**AWS Lambda:**
```java
public class MyLambdaHandler extends AwsLambdaHandler {
    public MyLambdaHandler() {
        super(new MyFunction());
    }
}
```

**Azure Functions:**
```java
public class MyAzureHandler extends AzureFunctionHandler {
    public MyAzureHandler() {
        super(new MyFunction());
    }
}
```

**Alibaba Cloud Function Compute:**
```java
public class MyFcHandler extends AlibabaFcHandler {
    public MyFcHandler() {
        super(new MyFunction());
    }
}
```

**Google Cloud Functions:**
```java
public class MyGoogleHandler extends GoogleCloudFunctionHandler {
    public MyGoogleHandler() {
        super(new MyFunction());
    }
}
```

## ServerlessRequest API

```java
ServerlessRequest request = ...;

// Get HTTP method
String method = request.getMethod();

// Get path
String path = request.getPath();

// Get query parameters
Map<String, String> queryParams = request.getQueryParameters();
String value = request.getQueryParameters().get("key");

// Get request headers
Map<String, String> headers = request.getHeaders();
String header = request.getHeaders().get("Content-Type");

// Get request body
String body = request.getBody();
```

## ServerlessResponse API

```java
// 200 OK response
ServerlessResponse.ok("{\"status\":\"ok\"}");

// 400 Bad Request
ServerlessResponse.badRequest("{\"error\":\"Invalid input\"}");

// 500 Internal Server Error
ServerlessResponse.serverError("{\"error\":\"Something went wrong\"}");

// Custom status code
ServerlessResponse.create(201, "{\"created\":true}");

// Add response headers
ServerlessResponse response = ServerlessResponse.ok(body);
response.addHeader("Content-Type", "application/json");
response.addHeader("X-Custom-Header", "value");
```

## Local Running and Testing

### Using ServerlessLocalRunner

EST Framework provides ServerlessLocalRunner to test and debug Serverless functions locally without deploying to cloud platforms.

**Run local example:**
```bash
mvn compile exec:java -Dexec.mainClass="ltd.idcu.est.examples.serverless.ServerlessLocalRunnerExample"
```

**Local runner features:**
- Register multiple functions
- Programmatically call functions
- Interactive command line mode
- Complete context support

**Interactive commands:**
```
> list              - List all registered functions
> invoke <name> [input] - Invoke function
> help              - Show help
> exit/quit         - Exit
```

## Cold Start Optimization

### Using ColdStartOptimizer

EST Framework provides ColdStartOptimizer to help optimize cold start performance of Serverless functions.

**Core features:**
- Cold start/warm start statistics
- Pre-warm determination
- Resource pre-warming
- Performance statistics

**Usage example:**
```java
public class OptimizedFunction implements HttpServerlessFunction {
    
    private final ColdStartOptimizer optimizer = ColdStartOptimizer.getInstance();
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    
    @Override
    public void initialize(Map<String, Object> config) {
        optimizer.recordColdStart("my-function");
        
        optimizer.preWarm("my-function", () -> {
            cache.put("key", "value");
        });
    }
    
    @Override
    public ServerlessResponse handle(ServerlessRequest request, Map<String, Object> context) {
        optimizer.recordWarmStart("my-function");
        
        Map<String, Object> stats = optimizer.getStatistics();
        // Use statistics...
    }
}
```

## Best Practices

### 1. Cold Start Optimization
- Keep `initialize()` method concise
- Use ColdStartOptimizer for pre-warming
- Lazy load non-essential resources
- Use connection pools and caching
- Consider using provisioned concurrency (AWS Lambda)

### 2. Error Handling
- Catch all exceptions
- Return appropriate HTTP status codes
- Log detailed error logs
- Provide meaningful error messages

### 3. Security
- Use environment variables for sensitive information
- Validate all inputs
- Implement proper authentication and authorization
- Avoid logging sensitive data

### 4. Performance
- Minimize dependencies
- Use efficient data structures
- Consider using GraalVM native images
- Monitor performance metrics

## More Information

- Check platform-specific deployment documentation in `deploy/serverless/` directory
- Reference EST Framework official documentation
- Check Serverless official documentation of each cloud platform

## Related Resources

- [AWS Lambda Documentation](https://docs.aws.amazon.com/lambda/)
- [Azure Functions Documentation](https://learn.microsoft.com/azure/azure-functions/)
- [Alibaba Cloud Function Compute Documentation](https://help.aliyun.com/product/50980.html)
- [Google Cloud Functions Documentation](https://cloud.google.com/functions)
