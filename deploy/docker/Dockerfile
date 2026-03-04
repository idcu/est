FROM openjdk:21-jdk-slim as builder

WORKDIR /build

COPY pom.xml .
COPY est-core ./est-core
COPY est-patterns ./est-patterns
COPY est-utils ./est-utils
COPY est-test ./est-test
COPY est-collection ./est-collection
COPY est-features ./est-features
COPY est-plugin ./est-plugin
COPY est-web ./est-web
COPY est-scaffold ./est-scaffold
COPY est-examples ./est-examples

RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

LABEL maintainer="idcu"
LABEL description="EST Framework Web Application"

RUN groupadd -r appgroup && useradd -r -g appgroup appuser

WORKDIR /opt/app

COPY --from=builder /build/est-examples/est-examples-web/target/est-examples-web-1.3.0-SNAPSHOT.jar app.jar

RUN chown -R appuser:appgroup /opt/app

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD java -cp app.jar ltd.idcu.est.examples.web.BasicWebAppExample || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
