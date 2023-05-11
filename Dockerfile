FROM maven:3.8.5-eclipse-temurin-17-alpine as maven

COPY ./pom.xml ./pom.xml

COPY ./src ./src

RUN mvn clean compile assembly:single

FROM openjdk:17-alpine

WORKDIR /baraholka-app

COPY --from=maven target/baraholka-bot-*.jar ./baraholka-bot.jar

CMD ["java", "-jar", "./baraholka-bot.jar"]