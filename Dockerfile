#use official maven image as the parent image java 20
FROM --platform=linux/amd64 maven:3.9-eclipse-temurin-20
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests --quiet
EXPOSE 8080
CMD ["java", "-jar", "/usr/src/app/target/graph-service-1.0-SNAPSHOT.jar"]
