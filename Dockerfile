FROM openjdk:17-alpine

COPY target/*.jar url-shortener-service.jar

ENTRYPOINT ["java", "-jar",  "url-shortener-service.jar"]
