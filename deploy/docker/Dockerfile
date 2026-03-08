FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY est-core ./est-core
COPY est-base ./est-base
COPY est-modules ./est-modules
COPY est-app ./est-app
COPY est-tools ./est-tools
COPY est-demo ./est-demo

RUN mvn clean package -DskipTests -Dmaven.javadoc.skip=true

FROM eclipse-temurin:21-jre-slim

LABEL maintainer="idcu"
LABEL description="EST Framework Demo Application"
LABEL version="2.0.0"

RUN groupadd -r appgroup && useradd -r -g appgroup appuser

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /opt/app

COPY --from=builder /build/est-demo/target/est-demo-2.0.0.jar app.jar

RUN mkdir -p /opt/app/logs /opt/app/config \
    && chown -R appuser:appgroup /opt/app

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

ENV JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdump.hprof"
ENV APP_ENV=production
ENV APP_NAME=est-demo
ENV APP_VERSION=2.0.0

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
