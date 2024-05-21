# Definição de build para a imagem do Spring Boot
FROM openjdk:17 as build

WORKDIR /app

# Copiar arquivos necessários para a construção
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Garantir que o script mvnw tenha finais de linha Unix e permissão de execução
RUN sed -i 's/\r$//' mvnw
RUN chmod +x ./mvnw

# Faça o download das dependências do pom.xml
RUN ./mvnw dependency:go-offline -B

COPY src src

# Construir o projeto, ignorando os testes
RUN ./mvnw package -DskipTests

# Extrair o JAR para a pasta target/dependency
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Definição de produção para a imagem do Spring Boot
FROM amazoncorretto:17-alpine-jdk
LABEL maintainer="baeldung.com"
ARG DEPENDENCY=/app/target/dependency

# Copiar as dependências para o build artifact
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Definir variável de ambiente para ativar o perfil de produção
ENV SPRING_PROFILES_ACTIVE=prd

# Rodar a aplicação Spring Boot
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "br.com.fiap.pedidos.PedidosApplication"]
