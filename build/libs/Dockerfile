FROM openjdk:11-jdk
ARG JAR_FILE=./build/libs/shop-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} docker-shop.jar
ENTRYPOINT ["java","-jar","/docker-shop.jar"]
