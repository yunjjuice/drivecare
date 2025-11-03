# ---- build stage ----
FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app
COPY . .
# RUN gradle clean bootJar -x test
# gradlew에 실행 권한 추가
RUN chmod +x gradlew
# gradle 빌드 수행 (테스트 및 restdocs 포함)
RUN ./gradlew clean test asciidoctor bootJar --no-daemon

# ---- runtime stage ----
FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV TZ=Asia/Seoul
EXPOSE 12432

ENTRYPOINT ["java", "-jar", "app.jar"]