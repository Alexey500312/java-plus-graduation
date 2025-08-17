FROM eclipse-temurin:21-jre-jammy

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

VOLUME /tmp

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]