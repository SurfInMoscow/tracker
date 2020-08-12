FROM openjdk:11

WORKDIR /usr/src/app

COPY tracker-0.0.1-Release.jar .

EXPOSE 8080

CMD ["java", "-jar", "tracker-0.0.1-Release.jar", "--spring.profiles.active=dev,jpa" ]