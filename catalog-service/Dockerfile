# --- Etapa 1: build ---
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copiamos primero solo los pom.xml (para aprovechar el cache de capas de Docker)
COPY pom.xml .
COPY shared/pom.xml shared/pom.xml
COPY catalog-service/pom.xml catalog-service/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY auth-service/pom.xml auth-service/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml

# Copiamos el código fuente real
COPY shared/src shared/src
COPY catalog-service/src catalog-service/src

# Compilamos solo shared y catalog-service (y sus dependencias) usando el reactor de Maven
RUN mvn -pl catalog-service -am clean package -DskipTests

# --- Etapa 2: runtime ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /build/catalog-service/target/catalog-service-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]