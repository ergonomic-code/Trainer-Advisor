FROM openjdk:17-alpine
EXPOSE 8080

WORKDIR /app
ADD /build/libs/QYoga-0.0.1-SNAPSHOT.jar /app/QYoga.jar
ENTRYPOINT ["java", "-jar", "QYoga.jar"]