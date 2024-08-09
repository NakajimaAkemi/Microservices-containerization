# Docker Overview
Docker is an open-source platform that enables developers to automate the deployment, scaling, and management of applications within lightweight, portable containers.\
This ensures that the application will run on any others machine regardless of any customized settings that machine might have that could differ from the machine used for writing and testing the code.

### Docker Architecture
<p align="center">
  <img src="images/Screen-Shot-2019-03-20-at-11.23.50-AM-1024x543.png" alt="Esempio di immagine" />
</p>

- **Docker CLI**: is the interface through which users interact with Docker; users input commands here to manage Docker containers, images, networks, and more (we will analyse some commands in the following sections). 
- **Socket**: is the communication bridge between the Docker CLI and the Docker Daemon; it allows the CLI commands to be sent to the Daemon.
- **Docker Daemon**/**Docker Engine**: is the core part of Docker that runs on the host machine, it is responsible for creating, running, and managing containers; it also listens for Docker API requests and manages Docker objects such as images, containers, networks, and volumes.
- **App**: applications that run inside containers, multiple applications can run in isolation from each other within their own containers.
- **Image**: a lightweight, standalone, and executable package that includes everything needed to run a piece of software - including the code, runtime, libraries, environment variables, and configuration files.
- **Namespaced Process**: containers run as namespaced processes; namespaces provide isolated views of system resources (such as process IDs, network access, user IDs, and file systems) for each container, ensuring they do not interfere with each other or with the host system.


### Key Features of Docker
- **Portability**: docker containers can run on any system that supports Docker, regardless of the operating system - ensuring consistency across development, testing, and production environments.
- **Version Control**: containers are lightweight because they share the host system's kernel, making them more resource-efficient than traditional virtual machines.
- **Isolation**: each container runs in isolation from others, preventing conflicts between others container or the host system (but also prevents conflict with applications and their dependencies).
- **Continuous Integration and Continuous Deployment (CI/CD)**: docker integrates well with CI/CD tools, allowing for streamlined development pipelines (automated testing, building and deployment processes).
- **Efficient Resource Utilization**: docker images can be versioned and tagged, allowing for easy rollbacks and updates.
- **Scalability**: docker makes it easy to scale applications by starting new containers as needed.
- **Security**: docker containers are isolated from each other and the host system, providing an additional layer of security.

____
## Basic Information
- **dockerfile**\
A Dockerfile is a script that contains a series of commands to build a Docker image.\
It specifies the base image, commands to install dependencies, copy files, set environment variables, and specify the command to run the application. _We analyze it specifically in the next section._


- **docker-compose.yml**\
Docker Compose is a tool that simplifies the management of multi-container Docker applications through a YAML configuration file.\
This file defines the services (containers) that make up your application, including their configurations such as images to use, ports to expose, network settings, and volume mounts.\
You can start all services with a single terminal command, making it easier to replicate and manage complex applications across different environments.


- **application.yml**\
It is a YAML-based configuration file used in Spring Boot to externalize application settings - hierarchically properties, for a more readable configuration.\
Spring Boot automatically loads these properties at runtime - flexible and centralized management. 
_It is usually located in the resources package._


- _**terminal commands** (bash)_\
  We can distinguish in two main categories: ```docker-compose``` and ```docker``` commands.\
  _(Some of the most important and used commands are reported here; many of these are very similar to commands that are used in a normal linux shell, or still very intuitive.)_\
  For ```docker-compose```, we can have:
  - ```build``` to **build** or rebuild services;
  - ```up``` to **start** the service (defined in the yaml file);
  - ```down``` to **stop** and remove the service.
  
  In the simple ```docker``` world we can manage:
  - the **volumes**: with ```volume create``` we can create a volume, and with ```volume rm``` you can remove it.
  - the **network**: as the same as volumes, we can create a new network with ```network create``` and remove it with ```network rm```
  - the **images**: ```build -t <image-name>``` to create a new image, with ```pull <image-name>``` instead we can download an image from a Docker registry, such as the [Docker Hub]( https://hub.docker.com/ ) (e.g. ```docker pull ubuntu:20.04```).
  - the **container itself**:
    - ```run <image-name>``` run a container from an image, obviously.
    - ```ps``` shows the running containers (with ```-a``` parameter we can see even those who are not running);
    - ```start <container-id>``` or ```stop <container-id>``` commands run or stop a container, ```rm <container-id>``` instead remove the container; rm
    - ```docker exec``` runs a command in a running container (e.g. ```docker exec -it <container-id> bash``` open an interactive -bash- shell).

____
#### Differences between Docker and Docker Desktop
**Docker** is an open-source platform used for developing, shipping, and running applications inside containers.\
**Docker desktop** instead is an all-in-one easy-to-install application designed to simplify the use of Docker (whose has also a Graphical User Interface). It is a commercial application (some features may require a payment).
____
