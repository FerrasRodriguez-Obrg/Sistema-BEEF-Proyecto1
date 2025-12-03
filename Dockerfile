# 1. Usa una imagen base de Java 17, ligera para menor tamaño
FROM openjdk:17-jdk-slim

# 2. Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# 3. Copia el archivo JAR compilado
# Utilizamos el nombre exacto que generó Maven
COPY target/Proyecto1-0.0.1-SNAPSHOT.jar app.jar

# 4. El puerto que Spring Boot expone (por defecto: 8080)
EXPOSE 8080

# 5. Comando para ejecutar la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]