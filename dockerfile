FROM openjdk:22-ea-122  


WORKDIR /app

ENV USERNAME=${USERNAME}
ENV PASSWORD=${PASSWORD}

COPY target/BackEnd-0.0.2-SNAPSHOT.jar /app/BackEnd-0.0.2-SNAPSHOT.jar


EXPOSE 8080

CMD ["java", "-jar", "BackEnd-0.0.2-SNAPSHOT.jar"]

# docker build -t backend-service .
# docker run -p 8080:8080 backend-service
# docker run -p 8080:8080 -e USERNAME=root -e PASSWORD=12345678 --name backend-service backend-service
