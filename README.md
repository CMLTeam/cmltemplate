# cmltemplate

[![Unit Tests](https://github.com/CMLTeam/cmltemplate/actions/workflows/run-tests.yml/badge.svg)](https://github.com/CMLTeam/cmltemplate/actions/workflows/run-tests.yml)

Use this template as a base for your Spring Boot app @ [CML](https://www.cmlteam.com)
   
### Versions

| Spring Boot | Java | MySQL | Postgres |
|-------------|------|-------|----------|
| 3.x         | 17   | 8     | 13       |


### Run
```bash
bash -c "$(curl -sSL "https://raw.githubusercontent.com/CMLTeam/cmltemplate/master/init.sh?token=$(date +%T)")"
```

and follow instructions. 

The project folder with scaffolded code will be created in current folder. 

### Analyze with CML SonarQube instance
First, log into [SonarQube UI](https://sonar.cmlteam.com) and generate an access token for the project.
Then, run this command in project root folder:
```bash
./mvnw clean verify sonar:sonar -Dsonar.login=TOKEN
```

Sonar for cmltemplate: [link](https://sonar.cmlteam.com/dashboard?id=cmltemplate).
                      
### Makesure

This project includes the lightweight build system [makesure](https://github.com/xonixx/makesure).

Nothing is needed to install. The tool is bundled as a single script `makesure` in root folder.
The build script is located in [Makesurefile](Makesurefile).

Issue `./makesure -l` to show a list of available goals.

### Update mvnw

```
./makesure mvnw_update
```  

### Swagger

Swagger UI available at http://localhost:8080/swagger-ui/index.html 

### Enable global CORS

Run either with `--cors.enabled=true` program argument OR with `CORS_ENABLED=true` environment variable.
    
### Docker

See [README-docker.md](docker-compose/README-docker.md)

### MySQL

Run DB

```
./makesure mysqld
```

Connect via CLI:

```
./makesure mysql
``` 

### PostgreSQL

Run DB

```
./makesure postgresd
```

Connect via CLI:
```                                        
./makesure postgres
``` 

### MongoDB

Run DB

```
./makesure mongod
```

Connect via CLI:
```                                        
./makesure mongo
``` 

### RabbitMQ
            
If you need to develop with RabbitMQ, in [application.yml](src/main/resources/application.yml) set the property `rabbitmq.enabled` to `true`.

Then run RabbitMQ server:

```
./makesure rabbitmq
```

Admin UI for RabbitMQ can be accessed by link http://localhost:15672/. The default credentials is: 

```
login   : guest
password: guest
```

To test how messaging works make this call in java code with appropriate data
```java
rabbitTemplate.convertAndSend(routingKey, "Prepared message"); 
```
And listener will return to you this message: `Message read from the queue : Prepared message`

### Useful links

- [Modern Java/JVM Build Practices](https://github.com/binkley/modern-java-practices)