# --------------------------------------------------------------------------------
# ETAPA 1: BUILDER (Compilación del código Java)
# --------------------------------------------------------------------------------
# Usa una imagen que ya incluye Maven y Java para compilar tu proyecto.
FROM maven:3.9.6-openjdk-17-slim AS builder
WORKDIR /app

# Copia el archivo pom.xml para que Maven cachee las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código fuente y compila la aplicación Spring Boot
COPY src ./src
# Comando que genera el archivo JAR en target/
RUN mvn clean package -DskipTests

# --------------------------------------------------------------------------------
# ETAPA 2: RUNTIME (Contenedor de ejecución final, más ligero)
# --------------------------------------------------------------------------------
# Usamos una imagen de Java pura, sin Maven, para el contenedor final.
FROM openjdk:17-jdk-slim
WORKDIR /app

# Define el nombre del JAR generado en la etapa BUILDER
ARG JAR_FILE=target/Proyecto1-0.0.1-SNAPSHOT.jar

# Copia el JAR compilado de la 'etapa builder' a esta etapa 'runtime'
COPY --from=builder /app/${JAR_FILE} app.jar

# Crea el directorio de uploads requerido por tu application.properties
# Render necesita esta ruta aunque sea un Windows path temporal en el properties.
RUN mkdir -p /app/c:/tmp

# Puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación al inicio
ENTRYPOINT ["java", "-jar", "app.jar"]