FROM openjdk:17-alpine
EXPOSE 8080
ADD /build/libs/QYoga-0.0.1-SNAPSHOT.jar QYoga.jar
ENTRYPOINT ["java", "-jar", "QYoga.jar"]