FROM bellsoft/liberica-openjdk-alpine:21
ARG JAR_FILE=target/web-0.0.1.jar
COPY ${JAR_FILE} app_web.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app_web.jar"]