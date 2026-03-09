# Getting Started

Welcome to EST Framework!

## Prerequisites

- Java 21 or higher
- Maven 3.8+

## Installation

Add EST Framework to your Maven project:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est</artifactId>
            <version>2.2.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## Your First Application

```java
import ltd.idcu.est.core.container.api.Container;
import ltd.idcu.est.core.container.api.Component;

@Component
public class HelloWorld {
    public String sayHello() {
        return "Hello, EST!";
    }
}

public class App {
    public static void main(String[] args) {
        Container container = Container.create();
        HelloWorld hello = container.get(HelloWorld.class);
        System.out.println(hello.sayHello());
    }
}
```

## Next Steps

- Explore the [Modules](../modules/README.md)
- Check out the [Examples](../examples/README.md)
- Read the [Architecture](../architecture/README.md) documentation
