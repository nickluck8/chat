FROM eclipse-temurin:17

LABEL mentainer="java"

WORKDIR /app

COPY target/chat-0.0.1-SNAPSHOT.jar /app/chat.jar

ENTRYPOINT ["java", "-jar", "chat.jar"]
