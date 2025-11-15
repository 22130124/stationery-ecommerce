FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Cài bash
RUN apk add --no-cache bash

# Copy jar và script
COPY target/*.jar app.jar
COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

EXPOSE 8082

ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "app.jar"]