FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY pom.xml .

RUN apk update && \
    apk add --no-cache maven && \
    mvn -e -B dependency:resolve

COPY . .

RUN mvn clean install

ENTRYPOINT ["java", "-jar", "/app/target/green-supermarket-backend-0.0.1-SNAPSHOT.jar"]
