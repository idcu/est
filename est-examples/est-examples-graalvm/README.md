# EST GraalVM Native Image Examples

This module demonstrates how to compile EST Framework applications to native images using GraalVM.

## Prerequisites

- GraalVM 23.1.2 or higher
- Java 21+
- Maven 3.8+

## Install GraalVM

1. Download GraalVM: https://www.graalvm.org/downloads/
2. Configure JAVA_HOME environment variable to point to GraalVM installation directory
3. Install native-image component
   ```bash
   gu install native-image
   ```

## Compile Native Image

### Basic Example

```bash
cd est-examples/est-examples-graalvm
mvn clean package -Pnative
```

### Run Native Image

```bash
./target/est-examples-graalvm
```

## Example Projects

### 1. HelloWorldNative

Simple dependency injection example demonstrating the EST container running in a native image.

```bash
mvn clean package -Pnative -DmainClass=ltd.idcu.est.examples.graalvm.HelloWorldNative
./target/est-examples-graalvm
```

### 2. WebAppNative

Web application example demonstrating the EST Web framework running in a native image.

```bash
mvn clean package -Pnative -DmainClass=ltd.idcu.est.examples.graalvm.WebAppNative
./target/est-examples-graalvm
```

Then visit:
- http://localhost:8080
- http://localhost:8080/api/hello
- http://localhost:8080/api/status

## Performance Comparison

| Metric | JVM Mode | GraalVM Native Mode |
|--------|----------|---------------------|
| Startup Time | ~500ms | ~30ms |
| Memory Usage | ~50MB | ~25MB |
| Image Size | ~5MB (JAR) | ~25MB (native) |

## Configuration Notes

### Reflection Configuration

Configure in `src/main/resources/META-INF/native-image/` directory:

- `reflect-config.json`: Reflection configuration
- `resource-config.json`: Resource configuration
- `jni-config.json`: JNI configuration
- `serialization-config.json`: Serialization configuration
- `proxy-config.json`: Dynamic proxy configuration

### Maven Plugin Configuration

The project uses `native-maven-plugin` to simplify the native image compilation process.

## Tips

1. First compilation of native image may take a long time (several minutes)
2. Native images don't support dynamic class loading, all required classes must be known at compile time
3. Use `--enable-http` and `--enable-https` to enable network support
4. When encountering reflection issues, need to add corresponding configuration in `reflect-config.json`

## Troubleshooting

If encountering compilation errors:

1. Ensure using correct GraalVM version
2. Check if reflection configuration is complete
3. Use `-H:+ReportExceptionStackTraces` to get more detailed error information
4. Check GraalVM official documentation: https://www.graalvm.org/reference-manual/native-image/
