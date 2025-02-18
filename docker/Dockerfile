FROM eclipse-temurin:21-jre-jammy


ARG GOOGLE_CLIENT_ID
ARG GOOGLE_CLIENT_SECRET
ENV GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
ENV GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}

WORKDIR /app

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# 설정 파일 복사
COPY src/main/resources/application.yml /app/application.yml
COPY src/main/resources/application-local.yml /app/application-local.yml

ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
