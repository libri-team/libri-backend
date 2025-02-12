FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY ../build/libs/*.jar app.jar

# JVM 튜닝 옵션
ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
