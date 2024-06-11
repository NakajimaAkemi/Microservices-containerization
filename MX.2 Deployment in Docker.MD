# Docker Overview
Docker is an open-source platform that enables developers to automate the deployment, scaling, and management of applications within lightweight, portable containers.\
This ensures that the application will run on any others machine regardless of any customized settings that machine might have that could differ from the machine used for writing and testing the code.


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


