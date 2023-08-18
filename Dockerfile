FROM openjdk:17
COPY build/libs/*.jar test1.jar
ENTRYPOINT ["java", "-jar", "/test1.jar"]