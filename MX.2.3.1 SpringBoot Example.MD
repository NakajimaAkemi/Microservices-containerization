# Analyze a real project  - Java, Docker and Spring boot

### Basic More Significant Examples
**Dockerfile**
- With **FROM** we specify the base image for the Docker image - e.g.: ```FROM openjdk:11-jre-slim```.
- With **WORKDIR** we set the working directory inside the container and all subsequent commands will be run in this directory - e.g.: ```WORKDIR /app```.
- With **COPY** we copy the file(s) from the host machine into the container’s working directory. - e.g.```COPY --chown=user:group host_file.xyz /path/container_file.xyz```.
- With **ADD** we copy file(s) from the host machine to the container (it can manage remote URLs and directly extract compressed files) [ADD is a COPY with additional functionalities] - e.g. ```ADD file.xyz /file.xyz```.
- With **RUN** we can run some commands, e.g. for clear up and/or install something inside the container - e.g. ```RUN java -Djarmode=layertools -jar app.jar extract```.
```dockerfile
FROM eclipse-temurin:21-jdk as builder
WORKDIR extracted
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract
EXPOSE 8080
```

**Docker compose**
This file defines the services that will be part of this Docker Compose application. **Each service runs a container**.
- We **name the service** (such as, in our example, ```producer``` or ```lavinmq```) - custom name that you can use to refer to the service within the _docker-compose_ configuration.
- With **build** we specify the directory containing the Dockerfile to build the image for this service (e.g. ```producer-end``` for the  ```producer```).
- **mem_limit** sets a memory limit for the container (in megabytes).
- To specifies environment variables for the service we use **environment**.
- **depends_on** instead specifies dependencies for the service (e.g. in this case, _producer_ service depends on the _lavinmq_ service - **_with the conditions that_** the producer service will start only after the lavinmq service is considered healthy).
- **image** specifies the Docker image to use for the service.
- Then map **ports** from the container to the host; this allows access to the service from outside the container (e.g. ```5672:5672``` maps port _5672_ on the host to port _5672_ in the container).
- **healthcheck** defines a health check for the service and is used to determine if the service is healthy - e.g. with test: ```[ "CMD", "lavinmqctl", "status" ]``` we run the ```lavinmqctl status``` command every 5 seconds (_interval_), the maximum time to wait is expressed with _timeout_, we have an important tag that is _retries_, and it gives the number of consecutive failures needed to consider the service as unhealthy.

```yml
services:
  producer:
    build: producer-end
    mem_limit: 512m
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      lavinmq:
        condition: service_healthy
        
  lavinmq:
    image: cloudamqp/lavinmq:latest
    mem_limit: 512m
    ports:
      - 5672:5672
      - 15672:15672
    healthcheck:
      test: [ "CMD", "lavinmqctl", "status" ]
      interval: 5s
      timeout: 2s
      retries: 60
```

**Application (application.yml)**\
It is a YAML-based configuration file used in Spring Boot to externalize application settings - hierarchically properties, for a more readable configuration.\This file is used to configure **various profiles** and settings for the application.\
Spring Boot automatically loads these properties at runtime - flexible and centralized management. 
_It is usually located in the resources package._

It is a central place to configure the application.
- **server.port**: sets the port on which the Spring Boot application will listen, obviously.
- **spring.rabbitmq**: is the section where all the features of RabbitMQ message broker connection is set, for exaple:
  - host: the hostname or IP address of the RabbitMQ server (e.g. in the following case is ```localhost```);
  - port: the same as we explain a few lines before;
  - then ```username``` and ```password``` to connect.
- With ```---``` we separate **different profiles** in Spring Boot, it allows you to have different configurations for different environments.
- **spring.config.activate.on-profile** is needed to specify the name of the profile.
- **spring.cloud.stream.bindings.[...]**: it refers to a framework that helps  build messageg-driven microservices, it is a particular case - but we put the attention ot the fact that there are two of this because each one correspond to different profiles that is activated in different runtime scenario. \
  With **instanceIndex** we are partitioning message processing to specify for which partition instance is responsible this consumer.

```yml
server.port: 8082

spring.rabbitmq:
  host: 127.0.0.1
  port: 5672
  username: guest
  password: guest
---
spring.config.activate.on-profile: docker
spring.rabbitmq.host: lavinmq
---
spring.config.activate.on-profile: partitioned_instance_0
spring.cloud.stream.bindings.messageProcessor-in-0.consumer:
  instanceIndex: 0
---
spring.config.activate.on-profile: partitioned_instance_1
spring.cloud.stream.bindings.messageProcessor-in-0.consumer:
  instanceIndex: 1
```

Then we have to work from the **terminal**:
```
docker-compose build
docker-compose up
```

____
To run all with a **Maven project**, we must write from terminal:\
(First of all - to delete previously compiled files and resources)
```
mvn clean
```
Now we have to tell to Maven that has to compiles the source code, processes the resources, and packages into a JAR (if we want we can put the flag so we can skip the tests).
```
mvn package -Dmaven.test.skip=true
```
-If we have not already done-
```
docker-compose build
```
Then we have to run all with docker - in the environment
```
docker-compose up
docker-compose down
```


In this paragraph we analyze the Chapter 6 of the [Magnus Larsson repository](https://github.com/PacktPublishing/Microservices-with-Spring-Boot-and-Spring-Cloud-Third-Edition/tree/main/Chapter06) focussing on the containerization part.\
We will analyze deeply Dockerfile, Docker-compose and Application (yaml) to comprehend how they work.

It is important to notice that in the microservices folder there are 4 services. Let's analyze each Dockerfile, then we can watch the Docker Compose whose use this files.
___
First of all lets analyze the **Dockerfile**s (all the microservices have the same Dockerfile).
```dockerfile
FROM eclipse-temurin:17.0.5_8-jre-focal as builder
WORKDIR extracted
ADD ./build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR application
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
```
In the first line we specify **the base image** to use `eclipse-temurin` (`17.0.5_8-jre-focal` version) - Debian Focal Fossa image whose includes the Java Runtime Environment (JRE) needed to run Java applications.\
Then we set the **working directory** inside the container: `/extracted`; from now on all commands will be executed in this directory.\
With the 3rd line we copy all the JARs file (in the `./build/libs` directory of the host machine) into the container with the name of `app.jar`.\
Then we do the `java -Djarmode=layertools -jar app.jar extract` with `RUN`. It extracts the contents of the JAR file into the current directory (`/extracted` as we say before).

Now we have the second part. The base image is the same as before. The working directory is now `/application`.\
We have also some copies to do. We go on the directory mentioned in the previous part (`builder`) and copy only the necessary things in the final image (dependencies and code) to reduce the size.\
Finally specify that the application will use port `8080` and that the default command to execute when the container starts with `ENTRYPOINT`.
___
Now we can analyze the **Docker Compose**.
```yml
version: '2.1'

services:
  product:
    build: microservices/product-service
    mem_limit: 512m
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      mongodb:
        condition: service_healthy

  recommendation:
    build: microservices/recommendation-service
    mem_limit: 512m
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      mongodb:
        condition: service_healthy

  review:
    build: microservices/review-service
    mem_limit: 512m
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      mysql:
        condition: service_healthy

  product-composite:
    build: microservices/product-composite-service
    mem_limit: 512m
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker

  mongodb:
    image: mongo:6.0.4
    mem_limit: 512m
    ports:
      - "27017:27017"
    command: mongod
    healthcheck:
      test: "mongostat -n 1"
      interval: 5s
      timeout: 2s
      retries: 60

  mysql:
    image: mysql:8.0.32
    mem_limit: 512m
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=rootpwd
      - MYSQL_DATABASE=review-db
      - MYSQL_USER=user
      - MYSQL_PASSWORD=pwd
    healthcheck:
      test: "/usr/bin/mysql --user=user --password=pwd --execute \"SHOW DATABASES;\""
      interval: 5s
      timeout: 2s
      retries: 60
```

The file is particularly large -it has many services-, so let’s comment it for parts.
First of all: the first line specifies the Docker Compose version used - `2.1`. Then there are the services. Below we will analyze them individually.

```yaml
product:
  build: microservices/product-service
  mem_limit: 512m
  environment:
    - SPRING_PROFILES_ACTIVE=docker
  depends_on:
    mongodb:
      condition: service_healthy
```
```yaml
recommendation:
  build: microservices/recommendation-service
  mem_limit: 512m
  environment:
    - SPRING_PROFILES_ACTIVE=docker
  depends_on:
    mongodb:
      condition: service_healthy
```
```yaml
review:
  build: microservices/review-service
  mem_limit: 512m
  environment:
    - SPRING_PROFILES_ACTIVE=docker
  depends_on:
    mysql:
      condition: service_healthy
```

The first three are very similar to each other. With what explained in the previous part we are perfectly able to understand them.\
In particular:
- for building the **container image**, each of them use a Dockerfile located at `microservices/[...]` (the path of each microservice);
- there is a **limit of memory** usage to 512MB;
- there are each time the same **environment variable** (`SPRING_PROFILES_ACTIVE` whose value is `docker`). \
  It is necessary to focus on this environment variable. **SPRING_PROFILES_ACTIVE** determines which profiles are active. The profiles are defined in the **application**(**.yml**) file that we analyze later.
- Every service has a **dependency**: the first two have to wait for the mongodb service to become **healthy** before starting, the last one the same but with mysql.

Now we have _our_ last service: product-composite
```yaml
product-composite:
  build: microservices/product-composite-service
  mem_limit: 512m
  ports:
    - "8080:8080"
  environment:
    - SPRING_PROFILES_ACTIVE=docker
```
Also for this service we create the image from our dockerfile, we have the environment variable and the memory limit, but in addition we expose the port to connect to the service (`8080`).


Now there are two _"pre-packaged"_ services: **MongoDB** and **MySQL**.

```yaml
mongodb:
  image: mongo:6.0.4
  mem_limit: 512m
  ports:
    - "27017:27017"
  command: mongod
  healthcheck:
    test: "mongostat -n 1"
    interval: 5s
    timeout: 2s
    retries: 60
```
```yaml
mysql:
  image: mysql:8.0.32
  mem_limit: 512m
  ports:
    - "3306:3306"
  environment:
    - MYSQL_ROOT_PASSWORD=rootpwd
    - MYSQL_DATABASE=review-db
    - MYSQL_USER=user
    - MYSQL_PASSWORD=pwd
  healthcheck:
    test: "/usr/bin/mysql --user=user --password=pwd --execute \"SHOW DATABASES;\""
    interval: 5s
    timeout: 2s
    retries: 60
```

They start from a standard base image (`mongo` and `mysql` respectively) but has the memory limitation as the personalized services that we have seen before. Each service expose a specific port (the standard port for service - in this particular case: `27017` for mongodb and `3306` for mysql).\
For MongoDB we execute the `mongod` command (to launch the primary daemon process for MongoDB).\
In MySQL instead we set some environment variables to log correctly (user, password, db, root superuser account).\
Then in both cases a test is made to do the **healthcheck**.

___
**Application** (**yml**)

You can set the active profiles in various ways including through system properties, environment variables, or directly in the application's configuration files as this **application.yml**.\
Spring Boot and Spring frameworks natively supports application.yml files.\
Let's analyze the files.
**product-service** and **recommendation-service** have practically the same application.yml.

_product-service:_
```yaml
server.port: 7001
server.error.include-message: always

spring.data.mongodb:
  host: localhost
  port: 27017
  database: product-db

logging:
  level:
    root: INFO
    se.magnus: DEBUG
    org.springframework.data.mongodb.core.MongoTemplate: DEBUG

---
spring.config.activate.on-profile: docker

server.port: 8080

spring.data.mongodb.host: mongodb
```
_recommendation-service:_
```yaml
server.port: 7002
server.error.include-message: always

spring.data.mongodb:
  host: localhost
  port: 27017
  database: recommendation-db

logging:
  level:
    root: INFO
    se.magnus: DEBUG
    org.springframework.data.mongodb.core.MongoTemplate: DEBUG

---
spring.config.activate.on-profile: docker

server.port: 8080

spring.data.mongodb.host: mongodb
```
In the first line we can see the first difference between the two files. The first application is configured to run on `port` 7001 and the second one on the 7002.\
With the second line we configure the server to always include error messages in responses.\
Then, with `spring.data.mongodb` -and the following lines-, we specify the connection details to MongoDB (such as the username and the pw, but also the database that we want to use - and here we spot the 2nd difference because the two applications use two different db).\
In the next part we can notice that there are configured different logging levels for different packages (`INFO` for `root`, and `DEBUG` for `se.magnus` and `org.springframework.data.mongodb.core.MongoTemplate`).

Finally, we arrived at the so-called **_profiles_**. This configuration activates when the `docker` profile is active, it will listen on the `8080` port.\
The last line is very important: in a development environment on your local machine, MongoDB might be running locally, so localhost is appropriate; _however_, in a **Dockerized environment**, MongoDB might be running in a different container.\
For this reason, in such cases, you typically use the service name (e.g., `mongodb` in this case) to refer to the MongoDB container.

There is a bit of difference regarding **review-service**.
```yaml
server.port: 7003
server.error.include-message: always

# Strongly recommend to set this property to "none" in a production environment!
spring.jpa.hibernate.ddl-auto: update

spring.datasource:
  url: jdbc:mysql://localhost/review-db
  username: user
  password: pwd

spring.datasource.hikari.initializationFailTimeout: 60000

logging:
  level:
    root: INFO
    se.magnus: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE

---
spring.config.activate.on-profile: docker

server.port: 8080

spring.datasource:
  url: jdbc:mysql://mysql/review-db
```
We have already seen the first two lines. The third insted sets the automatic updates the database schema but, as Larson says, it is not recommended in production since it might lead to unexpected schema changes.
Then, with `spring.datasource`, we specify the URL and the credentials for the MySQL database. The line after this will tell to wait for 60 seconds before failing if the database connection initialization fails.\
_The next section is the logging part, and we analyze it in the past application.yml._\
Now we have the profile for this application, that is more or less the same as the last profiles analyzed but differs by `spring.datasource` that change the URL when `docker` profile is active.

For **product-composite-service** instead is more long - because it wants to manage the api, and it tries to put all the services together (as we will see in the `app` tag) - but nevertheless not so much more complex. Let’s look at it.
```yml
springdoc:
  swagger-ui.path: /openapi/swagger-ui.html
  api-docs.path: /openapi/v3/api-docs
  packagesToScan: se.magnus.microservices.composite.product
  pathsToMatch: /**

api:

  common:
    version: 1.0.0
    title: Sample API
    description: Description of the API...
    termsOfService: MY TERMS OF SERVICE
    license: MY LICENSE
    licenseUrl: MY LICENSE URL

    externalDocDesc: MY WIKI PAGE
    externalDocUrl: MY WIKI URL
    contact:
      name: NAME OF CONTACT
      url: URL TO CONTACT
      email: contact@mail.com

  responseCodes:
    ok.description: OK
    badRequest.description: Bad Request, invalid format of the request. See response message for more information
    notFound.description: Not found, the specified id does not exist
    unprocessableEntity.description: Unprocessable entity, input parameters caused the processing to fail. See response message for more information

  product-composite:

    get-composite-product:
      description: Returns a composite view of the specified product id
      notes: |
        # Normal response
        If the requested product id is found the method will return information regarding:
        1. Product information
        1. Reviews
        1. Recommendations
        1. Service Addresses\n(technical information regarding the addresses of the microservices that created the response)

        # Expected partial and error responses
        1. If no product information is found, a **404 - Not Found** error will be returned
        1. In no recommendations or reviews are found for a product, a partial response will be returned

    create-composite-product:
      description: Creates a composite product
      notes: |
        # Normal response
        The composite product information posted to the API will be split up and stored as separate product-info, recommendation and review entities.

        # Expected error responses
        1. If a product with the same productId as specified in the posted information already exists,
           an **422 - Unprocessable Entity** error with a "duplicate key" error message will be returned

    delete-composite-product:
      description: Deletes a product composite
      notes: |
        # Normal response
        Entities for product information, recommendations and reviews related to the specified productId will be deleted.
        The implementation of the delete method is idempotent, i.e. it can be called several times with the same response.

        This means that a delete request of a non-existing product will return **200 Ok**.

server.port: 7000
server.error.include-message: always

app:
  product-service:
    host: localhost
    port: 7001
  recommendation-service:
    host: localhost
    port: 7002
  review-service:
    host: localhost
    port: 7003

logging:
  level:
    root: INFO
    se.magnus: DEBUG

---
spring.config.activate.on-profile: docker

server.port: 8080

app:
  product-service:
    host: product
    port: 8080
  recommendation-service:
    host: recommendation
    port: 8080
  review-service:
    host: review
    port: 8080
```

The first part is to manage the API documentation. **Springdoc** is an open-source library that simplifies the generation of API documentation for Spring Boot RESTful Web Services based on the OpenAPI 3 specification.
```yml
springdoc:
  swagger-ui.path: /openapi/swagger-ui.html
  api-docs.path: /openapi/v3/api-docs
  packagesToScan: se.magnus.microservices.composite.product
  pathsToMatch: /**
```
The only more or less important line is the 3rd one. It defines the package to scan for Spring components to be documented by OpenAPI.

This piece of code provides **metadata** about the API - such as version, title, description, license information etc.; and then we have the response codes with their descriptions.
```yml
api:
  common:
    version: 1.0.0
    title: Sample API
    description: Description of the API...
    termsOfService: MY TERMS OF SERVICE
    license: MY LICENSE
    licenseUrl: MY LICENSE URL

    externalDocDesc: MY WIKI PAGE
    externalDocUrl: MY WIKI URL
    contact:
      name: NAME OF CONTACT
      url: URL TO CONTACT
      email: contact@mail.com
    
  responseCodes:
    ok.description: OK
    badRequest.description: Bad Request, invalid format of the request. See response message for more information
    notFound.description: Not found, the specified id does not exist
    unprocessableEntity.description: Unprocessable entity, input parameters caused the processing to fail. See response message for more information
```

This section instead defines three endpoints related to the **product-composite**.
```yml
  product-composite:
    get-composite-product:
      description: Returns a composite view of the specified product id
      notes: |
        # Normal response
        If the requested product id is found the method will return information regarding:
        1. Product information
        1. Reviews
        1. Recommendations
        1. Service Addresses\n(technical information regarding the addresses of the microservices that created the response)

        # Expected partial and error responses
        1. If no product information is found, a **404 - Not Found** error will be returned
        1. In no recommendations or reviews are found for a product, a partial response will be returned

    create-composite-product:
      description: Creates a composite product
      notes: |
        # Normal response
        The composite product information posted to the API will be split up and stored as separate product-info, recommendation and review entities.

        # Expected error responses
        1. If a product with the same productId as specified in the posted information already exists,
           an **422 - Unprocessable Entity** error with a "duplicate key" error message will be returned

    delete-composite-product:
      description: Deletes a product composite
      notes: |
        # Normal response
        Entities for product information, recommendations and reviews related to the specified productId will be deleted.
        The implementation of the delete method is idempotent, i.e. it can be called several times with the same response.

        This means that a delete request of a non-existing product will return **200 Ok**.
```
As we can see, this part is absolutely readable, there are three actions of **CRUD** with some notes: _read_, _create_ and _delete_ (update -instead- is missing).

Then, _as we see in the previous application.yml_, we configure the server to run on port 7000 and the "type" of the error messages.
```yml
server.port: 7000
server.error.include-message: always
```


Finally, we have the juicy part.
```yml
app:
  product-service:
    host: localhost
    port: 7001
  recommendation-service:
    host: localhost
    port: 7002
  review-service:
    host: localhost
    port: 7003
```
The application communicates with the three microservices that we analyzed before (which all run on localhost on the ports `7001`, `7002` and `7003`).

_We have already seen in the previous files the logging part._
```yml
logging:
  level:
    root: INFO
    se.magnus: DEBUG
```

Now we have the profile. This section is activated when the `docker` profile is used, **and** -key-point- **changes the server port to 8080**!
```yml
---
spring.config.activate.on-profile: docker

server.port: 8080

app:
  product-service:
    host: product
    port: 8080
  recommendation-service:
    host: recommendation
    port: 8080
  review-service:
    host: review
    port: 8080
```

____
This project is not do with Maven so to tun the project we can't use the commands that are explained before in this guide.
But with gradle is very similar: `./gradlew clean` and then `./gradlew build`.\
But, as we see in class, we can use intelliJ and use the section **services** to run all (obviously after open Docker  or Docker Desktop).
