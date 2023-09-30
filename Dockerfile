FROM amazoncorretto:17-alpine

WORKDIR /home/jubas-backend

COPY . /home/jubas-backend

ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8080

RUN ./mvnw clean install

CMD ["java","-jar","./target/jubasbackend-0.0.1-SNAPSHOT.jar"]