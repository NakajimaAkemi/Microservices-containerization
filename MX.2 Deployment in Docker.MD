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
### Introduction to Dockerfile Syntax
As seen before a  `Dockerfile` is a simple text file that contains a series of instructions on how to build a Docker image. Each instruction in a Dockerfile adds a layer to the image, creating a lightweight and reusable environment for running applications. In this paragraph we will introduce the essential and most important commands.

#### 1. `FROM`

The `FROM` instruction specifies the base image from which your image will be built. It’s usually the first line in any Dockerfile.

```dockerfile
FROM ubuntu:20.04
```

This command sets the base image to Ubuntu 20.04.

> [!IMPORTANT]
> Choosing the base image of our container is an important step, find the one that provides what you need and obviously stick to trusted and official distibutors.
____
#### 2. `COPY`

The `COPY` instruction copies files or directories from your local machine into the Docker image.

```dockerfile
COPY . /app
```

This command copies everything from the current directory on your local machine to the `/app` directory in the Docker image.
____
#### 3. `RUN`

The `RUN` instruction executes commands inside the image during the build process, typically used for installing software or setting up your environment.

```dockerfile
RUN apt-get update && apt-get install -y python3
```

This command updates the package list and installs Python 3 inside the image.
____
#### 4. `WORKDIR`

The `WORKDIR` instruction sets the working directory inside the image. All subsequent instructions will be run from this directory.

```dockerfile
WORKDIR /app
```

This sets the working directory to `/app`.
____
#### 5. `CMD`

The `CMD` instruction specifies the command that will run when the container starts, tipically it's written following the format 
`CMD ["executable","param1","param2"]` also referenced as **exec form**.

```dockerfile
CMD ["python3", "app.py"]
```

This command runs `app.py` using Python 3 when the container starts.

> [!NOTE]
> The key difference between `CMD` and `RUN` is the moment the command is executed, the former is executed on build time and the former is execute during the container startup.

> [!IMPORTANT]
> There can only be one CMD instruction in a Dockerfile. If you list more than one `CMD`, only the last one takes effect.

____

#### 6. `EXPOSE`

The `EXPOSE` instruction tells Docker which port your application will use so that it can be accessed from outside the container.

```dockerfile
EXPOSE 8080
```

This exposes port 8080 for access.
____
#### 7. `ENV`

The `ENV` instruction sets environment variables in the container. These variables can be used by applications or scripts running inside the container.

```dockerfile
ENV ENVIRONMENT=production
```

This sets the `ENVIRONMENT` variable to `production`.

____

## Example Dockerfile

Here’s a basic example of a Dockerfile for a Python application:

```dockerfile
# Use Python 3 as the base image
FROM python:3.9

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Install any needed packages specified in requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run app.py when the container launches
CMD ["python", "app.py"]
```

### What this Dockerfile does:

1. **Sets a base image** (`FROM python:3.9`).
2. **Sets a working directory** (`WORKDIR /app`).
3. **Copies application files** into the container (`COPY . /app`).
4. **Installs dependencies** listed in `requirements.txt` (`RUN pip install`).
5. **Exposes a port** (`EXPOSE 8080`).
6. **Specifies a startup command** to run the application (`CMD ["python", "app.py"]`).

## How to download an image

## Sources and additional readings
 - https://docs.docker.com/reference/dockerfile/
