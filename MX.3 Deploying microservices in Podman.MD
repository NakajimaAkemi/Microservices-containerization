# Podman Overview
Podman (short for pod manages) is an open-source container management tool developed by Red Hat, designed to deploy and manage containerized applications. Podman in many cases works just like Docker, but it offers enhanced security and the ability to run commands with non-root privileges.


## Podman's architecture
<p align="center">
  <img src="images/dockerAndPodman.png" alt="Esempio di immagine" />
</p>
Podman is fundamentally different from Docker by being daemonless, Podman uses a more traditional fork-exec model to run containers, every command in Podman is executed as a process separated from the user. 
Podman's architecture is more robust and reliable than Docker's because it erases a single point of failure that is the daemon, if a daemon goes down, no container is reachable anymore.

<p align="center">
  <img src="images/daemonless.JPG" alt="Esempio di immagine" />
</p>

One benefit of the fork-exec model, it integrates well into the Linux kernel's audit system. This way, the system logs exactly which user executed which containers. 


## Rootless containers

Probably the most significant feature of Podman is the ability to run containers in rootless mode. In many situations you don't want to give full root access to your users, but users and developers still need to run containers and build images. 
Every container process is managed with the current user's permissions, reducing the necessity of higher clearance.


## Concept of Pods

Podman introduces the concept of “pods,” which are groups of one or more containers that share the same network namespace and storage resources simulating a single host. This concept is borrowed from Kubernetes and allows for easier management of related containers.

One of the design goals of containers is to separate services into single containers: microservices. Then you combine containers together to build larger services. Pods allow you to group multiple services
together to form a larger service managed as a single entity. One of the goals of Podman is allowing you to experiment with pods

<p align="center">
  <img src="images/pods.JPG" alt="Esempio di immagine" />
</p>


## Why should we care about Podman?  

Podman is fundamentally design with security in mind, paving the way for running containers in high-security enviroments. A daemon running as root generally does not meet these requirements and has prevented lots of security-conscious companies from widespread adoption of Docker. Podman containers have always been rootless, while Docker is an all-in-1 tool for container management and creation, and including the fact that the architecture is daemon-less, the surface of attack is highly reduced.



## Systemd Integration



## Podman commands
A container image consists of three components:
 A directory tree containing all the software required to run your application
 A JSON file that describes the contents of the rootfs
 Another JSON file called a manifest list that links multiple images together to
support different architectures
The directory tree is called a rootfs (root filesystem). The software is laid out like it was
the root (/) of a Linux system.
 The executable to be run within the rootfs, the working directory, the environment variables to be used, the maintainer of the executable, and other labels to help
identify the content of the image are defined in the first JSON file. You can see this
JSON file using the podman inspect command:
$ podman inspect docker:/ /registry.access.redhat.com/ubi8


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


Provides a familiar command line experience compatible with the docker cli, grat for running/building/sharing containers outside of OpenShift, Can be Integrated in infrasturucture where docker daemon/cli are used today.
Podman's commands are run from the  command-line interface, which are similar to Docker


## Docker compatability

Thanks to the OCI format, Podman is compatible with Docker, meaning that Podman can pull/push images to/from Docker's registries, also the switch of the workflow from Docker to Podman and viceversa easier. Podman is a powerful alternative to Docker, but the 2 can also work together,supporting most Docker commands and Dockerfile syntax. This compatibility makes transitioning from Docker to Podman smoother for developers.

Because Podman does almost everything that Docker can do with the same command line as Docker, users can easily switch between them by aliasing Docker to Podman (alias docker=podman) and vice versa. Additionally, an rpm called podman-docker can drop a “docker” into the system application path, which calls Podman for those environments where the “docker” command is needed, easing the transition from Docker. Podman’s CLI is similar to Docker’s, so users who are familiar with one are likely to have success with the other.

Some developers combine Podman and Docker, using Docker during the development stage and transferring their program to Podman in runtime environments.

## _
leveraing SELinux.
Integrated with systemd
Default approcah on Fedora and RHEL.

Not everybody has the luxury to build all thier own containers from scratch, because it would be an added cost stemming from the maintenance needs, such as patching a security issue.
Base OS also?
Another issue it other tools that run along side the container technology.

--Advantages and used cases--


--Podman Desktop--

--Deployment in Podman--



## Sources
- https://www.redhat.com/en/topics/containers/what-is-podman
