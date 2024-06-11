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

## Basic Information
- dockerfile
- docker-compose.yml
  - basics features (....)
- application.yml
  - (..... how it works, why it's important etc.)
- _terminal commands (bash)_
  - ...

(**todo**)


### Base Project Example - Java, Docker and Spring boot
(**todo** ... some code)


## Analyze a real project
(**todo** - here we take Chapter6 from the
[Magnus Larsson repository](https://github.com/PacktPublishing/Microservices-with-Spring-Boot-and-Spring-Cloud-Third-Edition/tree/main/Chapter06) 
and explain all the features of the project, analyzing -as we say- dockerfile, docker-compose and application.yml).


