FROM maven:3-amazoncorretto-8
WORKDIR /travelsystem
ADD . /travelsystem
RUN mvn compile
CMD java -jar target/travel-system-1.0-SNAPSHOT.jar