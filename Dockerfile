FROM maven:3.8.5-eclipse-temurin-17-alpine as maven

COPY ./pom.xml ./pom.xml

COPY ./tg-bot/pom.xml ./tg-bot/pom.xml

WORKDIR /tg-bot

RUN mvn clean dependency:resolve
#RUN mvn clean package -Dmaven.main.skip -Dmaven.test.skip && rm -r target

COPY ./tg-bot/src ./src

#RUN mvn clean package -Dmaven.test.skip

RUN mvn compile assembly:single --no-snapshot-updates

FROM openjdk:17-alpine

WORKDIR /baraholka-app

COPY --from=maven ./tg-bot/target/tg-bot-*.jar ./tg-bot/baraholka-bot.jar

CMD ["java","-Xmx512m", "-jar", "./tg-bot/baraholka-bot.jar"]