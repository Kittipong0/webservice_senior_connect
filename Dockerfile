# STEP 1: สร้าง jar ไฟล์จาก Maven (ใช้ multi-stage build)
FROM maven:3.9.3-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# STEP 2: รัน Spring Boot App
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
