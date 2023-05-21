FROM maven:3-amazoncorretto-8
WORKDIR /travelsystem
ADD . /travelsystem
RUN mvn test clean compile assembly:single
CMD java -jar target/travel-system-1.0-SNAPSHOT-jar-with-dependencies.jar