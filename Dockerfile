FROM openjdk:11
ARG JAR_FILE=./build/libs/searchservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} searchservice.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/searchservice.jar"]
