FROM openjdk:11

WORKDIR /usr/src/app

COPY tracker-0.0.2-Release.war .

EXPOSE 8080

CMD ["java", "-jar", "tracker-0.0.2-Release.war", "--spring.profiles.active=dev,jpa" ]