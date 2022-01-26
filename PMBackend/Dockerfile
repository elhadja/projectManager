FROM openjdk:8
ADD target/projectManager.jar app.jar
EXPOSE 9090 
ENTRYPOINT ["java","-jar","/app.jar"]