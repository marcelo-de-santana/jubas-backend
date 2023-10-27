# GENERATOR FOR DEPLOYMENT
FROM amazoncorretto:17-alpine

WORKDIR /home/jubas-backend

COPY . /home/jubas-backend

ENV SPRING_PROFILES_ACTIVE=prd

EXPOSE 8080

CMD ["/home/jubas-backend/.docker/start.sh"]