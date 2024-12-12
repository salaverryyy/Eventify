FROM openjdk:22-jdk
WORKDIR /app

# Copia los archivos del proyecto al contenedor
COPY . /app

# Otorga permisos de ejecución al archivo mvnw
RUN chmod +x ./mvnw

# Ejecuta Maven para compilar y empaquetar el JAR
RUN ./mvnw clean package -DskipTests

# Establece el archivo JAR generado como entrada
CMD ["java", "-jar", "target/eventos-0.0.1-SNAPSHOT.jar"]
