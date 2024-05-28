# Usa una imagen base de Alpine Linux
FROM alpine:3.19.1

# Etiqueta adicional para identificar el autor
LABEL author="ezequielbolzi"


# Actualiza los paquetes y luego instala OpenJDK 11
RUN apk update && \
    apk add openjdk11

# Copia tu archivo JAR al contenedor
COPY target/demo-0.0.1-SNAPSHOT.jar /app/app.jar

# Configura el directorio de trabajo
WORKDIR /app

# Ejecuta tu aplicación con Java
CMD ["java", "-jar", "app.jar"]
