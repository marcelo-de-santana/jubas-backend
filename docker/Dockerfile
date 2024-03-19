FROM amazoncorretto:17-alpine as builder

WORKDIR /app

COPY .. /app

RUN ./mvnw clean install \
    -DskipTests=true \
    -Dmaven.test.skip=true \
    -Dmaven.test.dependency.skip=true

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY ../commands/prd_start.sh /app/commands/

COPY ../.env /app

COPY --from=builder /app/target/jubasbackend-0.0.1-SNAPSHOT.jar ./jubasbackend.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prd

CMD ["/app/commands/prd_start.sh"]
