# Usa la imagen base de OpenJDK
FROM openjdk:22-jdk

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos necesarios para construir el proyecto
COPY . /app

# Ejecuta Maven para compilar y empaquetar el JAR
RUN ./mvnw clean package -DskipTests

# Expone el puerto de la aplicación
EXPOSE 8080

# Define el comando para ejecutar el JAR generado
ENTRYPOINT ["java", "-jar", "target/eventos-0.0.1-SNAPSHOT.jar"]
