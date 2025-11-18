FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Không cần bash hay wait-for-it.sh nếu không chờ service nào
COPY target/*.jar app.jar

EXPOSE 8080

# ENTRYPOINT: Chạy ứng dụng Java trực tiếp
ENTRYPOINT ["java", "-jar", "app.jar"]