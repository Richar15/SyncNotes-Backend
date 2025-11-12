# ---- Etapa 1: Compilación ----
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Crea carpeta de trabajo
WORKDIR /app

# Copia los archivos de Maven y dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el resto del código
COPY src ./src

# Empaqueta el proyecto (sin correr tests)
RUN mvn clean package -DskipTests

# ---- Etapa 2: Ejecución ----
FROM eclipse-temurin:17-jdk-alpine

# Carpeta de trabajo
WORKDIR /app

# Copiamos el jar generado desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Exponemos el puerto que usa tu aplicación (ajústalo si no es 8080)
EXPOSE 8081

# Comando para iniciar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
