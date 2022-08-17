FROM openjdk:11
VOLUME /app
ADD target/event-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]