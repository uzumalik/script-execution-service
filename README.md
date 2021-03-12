# script-execution-service

This Service contains REST API to run scripts

### Service Configuration

Service uses 'Spring Profiles' and have different properties file per environment

- [application.yaml]
- [application-dev.yaml]

use below arguments to activate profile on startup

-Dspring.profiles.active=dev

Use below arguments to run service with security manager
-Dspring.profiles.active=dev -Djava.security.debug=access,stacktrace -Djava.security.manager -Djava.security.policy=resources/security/permissions.policy

### Run as jar executable
Run on the Terminal
1. clone the repository and cd script-execution-service
2. ./gradlew clean build
3. java -jar -Dspring.profiles.active=dev ./build/libs/script-execution-service-0.0.1-SNAPSHOT.jar


### Run as dockerized application
Run on the Terminal

1. clone the repository and cd script-execution-service
2. ./gradlew bootBuildImage   --- Build the image and store it in docker local registry
3. docker run -p 9080:9080 -e "SPRING_PROFILES_ACTIVE=dev" --name script-execution-service script-execution-service:0.0.1-SNAPSHOT 

#### Docker commands:
1. docker ps -a  --- List the docker containers status

2. docker images --- List the docker images present in local registry

3. docker stop <\<container name>> --- Stop the docker running container

4. docker rm <\<container name>> --- Remove the docker running container

#### Prerequisite:
Docker needs to be installed.

### API Docs
 Local : http://localhost:9080/script-engine/swagger-ui.html#/
 
### Service Health and Info

Health : http://localhost:9080/script-engine/actuator/health

Info : http://localhost:9080/script-engine/actuator/info

