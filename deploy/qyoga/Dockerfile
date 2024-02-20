FROM eclipse-temurin:21-alpine
EXPOSE 8080

WORKDIR /app
ADD /app/build/libs/qyoga.jar /app/qyoga.jar
ENTRYPOINT ["java", "-jar", "qyoga.jar"]