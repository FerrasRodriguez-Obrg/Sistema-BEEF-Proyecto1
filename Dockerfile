# Usa una imagen base de Java 17
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR generado por el Build Command de Render
COPY target/Proyecto1-0.0.1-SNAPSHOT.jar app.jar

# Crea el directorio de uploads (RUTA LINUX CORREGIDA)
RUN mkdir -p /app/uploads

# Puerto y ejecución
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]