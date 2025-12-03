# --------------------------------------------------------------------------------
# ETAPA 1: BUILDER (Compilación del código Java)
# --------------------------------------------------------------------------------
# CORRECCIÓN: Usamos maven:3-jdk-17-alpine (estable y ligera)
FROM maven:3-jdk-17-alpine AS builder
WORKDIR /app

# Copia los archivos necesarios para la compilación
COPY pom.xml .
COPY src ./src
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Ejecuta la compilación, asegurando que los tests sean omitidos
RUN mvn clean package -DskipTests

# --------------------------------------------------------------------------------
# ETAPA 2: RUNTIME (Ejecución)
# --------------------------------------------------------------------------------
# CORRECCIÓN: Usamos openjdk:17-alpine (estable y ligera)
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Define el nombre del JAR
ARG JAR_FILE=target/Proyecto1-0.0.1-SNAPSHOT.jar

# Copia el JAR compilado de la 'etapa builder'
COPY --from=builder /app/${JAR_FILE} app.jar

# CREACIÓN DEL DIRECTORIO CORREGIDO (RUTA LINUX)
RUN mkdir -p /app/uploads 

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]