# Podman Overview
Podman (short for pod manages) is an open-source container management tool developed by Red Hat, designed to deploy and manage containerized applications. Podman in many cases works just like Docker, but it offers enhanced security and the ability to run commands with non-root privileges.

## Basic terms informations 
 - **fork/exec**: is a two-step process used in Unix-like operating systems for creating and running new processes. 

- **Orchestration**: Container orchestration is the automated management of containerized applications, handling deployment, scaling, networking, and availability across a cluster of machines. Tools like ```Kubernetes``` and ```Docker Swarm``` facilitate this process.

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







## Podman and Docker compatability 
Podman is a powerful alternative to Docker, but the 2 can also work together,supporting most Docker commands and Dockerfile syntax. This compatibility makes transitioning from Docker to Podman smoother for developers. Some developers combine Podman and Docker, using Docker during the development stage and transferring their program to Podman in runtime environments. 

### Dockerfile
Thanks to the ```OCI``` format, Podman support the same syntax and can build images from the dockerfile, meaning that Podman can ```pull/push``` images to/from Docker's registries, also the switch of the workflow from Docker to Podman and viceversa easier. 

### Podman commands (bash)
One of Podman's greatest advantages is its CLI compatibility with Docker, because Podman does almost everything that Docker can do with the same command line as Docker. In fact, when building Podman, Docker users can adapt without any significant changes. For example, you can use the ```alias``` command to create a docker alias for Podman:
```
$ alias docker=podman
```
You can run other commands, such as ```pull```, ```push```, ```build```, ```commit```, ```tag```, and more with Podman. Podman’s CLI is similar to Docker’s, so users who are familiar with one are likely to have success with the other. There are exclusive podman commands, but are for more specific use cases.




## Why should we care about Podman?  
Podman is fundamentally design with security in mind, paving the way for running containers in high-security enviroments. 

#### Reason 1: Less surface of attack
A daemon running as root generally does not meet the security requirements and has prevented lots of security-conscious companies from widespread adoption of Docker. 

Podman containers have always been **rootless**, while Docker is an all-in-1 tool for container management and creation, and including the fact that the architecture is daemon-less, due to the simplified architecture the surface of attack is highly reduced.

#### Reason 2: Systemd integration 
Podman can generate systemd service files, allowing developers/users to manage the containers as sytemd services in Linux-based systems.

#### Reason 3: Pod concept
Podman supports the concept of Pods, containers running together in the same network namespace, providing simpler management. 

#### Reason 4: Seamless Kubernetes integration
Podman can generate Kubernetes YAML files from existing pod and containers, making the integration to the orchestration tool seamless.

## Sources
- https://www.redhat.com/en/topics/containers/what-is-podman
- https://github.com/containers/podman/blob/main/docs/tutorials/rootless_tutorial.md
