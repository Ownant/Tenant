# Stage 1
FROM --platform=linux/amd64 amazoncorretto:17-alpine as builder

WORKDIR /application
RUN adduser -D app && chown -R app /application

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java","-jar","/application.jar"]
RUN java -Djarmode=layertools -jar application.jar extract

# Stage 2
FROM builder

WORKDIR /application
COPY --from=builder application/dependencies/BOOT-INF/lib ./lib
COPY --from=builder application/application/META-INF ./
COPY --from=builder application/application/BOOT-INF/classes ./

EXPOSE 9000 9001
ENV APP_LOGGER=CONSOLE_JSON

USER app
ENTRYPOINT ["java", "-cp", ".:./lib/*", "org.ownant.tenant.TenantServiceApplication"]