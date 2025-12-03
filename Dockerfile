# --------------------------------------------------------------------------------
# ETAPA 1: BUILDER (Compilación del código Java)
# --------------------------------------------------------------------------------
# Usa una imagen que ya incluye Maven y Java para compilar tu proyecto.
FROM maven:3.9.6-openjdk-17-slim AS builder
WORKDIR /app

# Copia el pom.xml y el código fuente.
COPY pom.xml .
COPY src ./src

# Copia los wrappers de Maven si los usas localmente (para mayor compatibilidad)
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Ejecuta la compilación, asegurando que los tests sean omitidos
RUN mvn clean package -DskipTests

# --------------------------------------------------------------------------------
# ETAPA 2: RUNTIME (Contenedor de ejecución final, más ligero)
# --------------------------------------------------------------------------------
# Usamos una imagen de Java pura, sin Maven.
FROM openjdk:17-jdk-slim
WORKDIR /app

# Define el nombre del JAR
ARG JAR_FILE=target/Proyecto1-0.0.1-SNAPSHOT.jar

# Copia el JAR compilado de la 'etapa builder' a esta etapa 'runtime'
COPY --from=builder /app/${JAR_FILE} app.jar

# CORRECCIÓN FINAL: Crea el directorio de uploads para la ruta /app/uploads
RUN mkdir -p /app/uploads 

EXPOSE 8080

# Comando para ejecutar la aplicación al inicio
ENTRYPOINT ["java", "-jar", "app.jar"]