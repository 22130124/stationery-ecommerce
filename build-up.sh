#!/bin/bash

# Build tất cả service cùng lúc
mvn -f api-gateway/pom.xml clean package -DskipTests
mvn -f auth-service/pom.xml clean package -DskipTests
mvn -f product-service/pom.xml clean package -DskipTests
mvn -f category-service/pom.xml clean package -DskipTests
mvn -f supplier-service/pom.xml clean package -DskipTests
mvn -f brand-service/pom.xml clean package -DskipTests
mvn -f upload-service/pom.xml clean package -DskipTests
mvn -f cart-service/pom.xml clean package -DskipTests
mvn -f profile-service/pom.xml clean package -DskipTests

# Build và up docker
docker compose up -d --build