# cmltemplate

[![Unit Tests](https://github.com/CMLTeam/cmltemplate/actions/workflows/run-tests.yml/badge.svg)](https://github.com/CMLTeam/cmltemplate/actions/workflows/run-tests.yml)

## Usage
Use this template as a base for your Spring Boot app @ CML

### Run
```bash
curl -sSL "https://raw.githubusercontent.com/CMLTeam/cmltemplate/master/init.sh?$(date +%T)" | bash
```

and follow instructions. 

The project folder with scaffolded code will be created in current folder. 

### Analyze with CML SonarQube instance
First, log into [SonarQube UI](https://sonar.cmlteam.com) and generate an access token for the project.
Then, run this command in project root folder:
```bash
mvn clean verify sonar:sonar -Dsonar-login-token=TOKEN
```                                                    

### Update mvnw

```bash
./mvnw -N io.takari:maven:0.7.7:wrapper
```  

### Swagger

Swagger UI available at http://localhost:8080/swagger-ui/index.html 


### MySQL

Run DB

```
docker-compose -f docker-compose-mysql.yml up
```

Connect via CLI:
```
docker exec -it mysql8 mysql cmltemplate -ucmltemplate -pcmltemplate
``` 

### PostgreSQL

Run DB

```
docker-compose -f docker-compose-postgresql.yml up
```

Connect via CLI:
```                                        
docker exec -it postgres13 psql --username=cmltemplate --dbname=cmltemplate
``` 

### Enable global CORS

Run either with `--cors.enabled=true` program argument OR with `CORS_ENABLED=true` environment variable. 

### RabbitMQ
            
If you need to develop with RabbitMQ, in [application.yml](src/main/resources/application.yml) set the property `rabbitmq.enabled` to `true`.

Then run RabbitMQ server:

```
docker-compose -f docker-compose-rabbitmq.yml up
```

Admin UI for RabbitMQ can be accessed by link http://localhost:15672/. The default credentials is: 

```
login   : guest
password: guest
```

To test how messaging works make this call in java code with appropriate data
```
rabbitTemplate.convertAndSend(routingKey, "Prepared message"); 
```
And listener will return to you this message: `Message read from the queue : Prepared message`

### Spring Security

#### If you need activate spring-security set in [application.yml](src/main/resources/application.yml):
```yml
jwt:
  enabled: true
```
And add to external configuration file next fields:
```yml
jwt:
  secret: YourSecret
  expirationInMs: YourExpirationTime
```

#### If you want to remove extra file of spring security you have to remove next files
* [security/*](src/main/java/com/cmlteam/cmltemplate/security)
* [services/CustomerAuthenticationService.java](src/main/java/com/cmlteam/cmltemplate/services/CustomerAuthenticationService.java)
* from [entities/User.java](src/main/java/com/cmlteam/cmltemplate/entities/User.java) remove `implements UserDetails` and all `@Override` methods
* remove useless dependencies from [pom.xml](pom.xml)
```xml
    <!-- security: -->
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-config</artifactId>
        <version>5.1.5.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-web</artifactId>
        <version>5.1.5.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
        <version>0.9.1</version>
    </dependency>
```
