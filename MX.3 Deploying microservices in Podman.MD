# Podman Overview
Podman (short for pod manages) is an open-source container management tool developed by Red Hat, designed to deploy and manage containerized applications. Podman in many cases works just like Docker, but it offers enhanced security and the ability to run commands with non-root privileges.

## Basic informations
 - **cgroup (control group)**: is a Linux kernel feature that allows the system to allocate, monitor, and limit resources (such as CPU, memory, and I/O) for a group of processes. It provides a way to manage and isolate the resource usage of applications and services, improving system performance and stability. Cgroups are essential for implementing containerization and resource management in Linux-based environments.
 - **fork/exec**: is a two-step process used in Unix-like operating systems for creating and running new processes. 
 - **PID (Process Identifier)**: is a unique numerical identifier assigned by the operating system to each process running on a system. It is used to manage and reference processes, enabling tasks such as process control, monitoring, and communication. PIDs help the operating system distinguish between different running processes.
 - **namespace**: is a feature in Linux that isolates and virtualizes system resources for a set of processes. It creates an environment where a process or a group of processes sees a distinct set of resources, such as file systems, network interfaces, process IDs, and user IDs. This isolation is key to containerization, allowing multiple containers to run on the same host without interfering with each other.

- **Userspace**: Userspace refers to the area of system memory where user-mode applications and processes run, as opposed to kernel space where the core of the operating system executes. It provides an isolated environment where applications operate with restricted access to hardware and system resources, ensuring system stability and security by preventing direct interaction with the kernel.

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


## Rootless containers and User-namespace support

Probably the most significant feature of Podman is the ability to run containers in rootless mode. In many situations you don't want to give full root access to your users, but users and developers still need to run containers and build images. Every container process is managed with the current user's permissions, reducing the necessity of higher clearance.
<p align="center">
  <img src="images/rootless.png" alt="Esempio di immagine" />
</p>

Also the support of the user namespace is an important functionality that allows us to execute containers in rootless mode, and allows us to isolate the container from the host and other processes in execution. 

## Concept of Pods

Podman introduces the concept of “pods,” which are groups of one or more containers that share the same network namespace and storage resources simulating a single host. This concept is borrowed from Kubernetes and allows for easier management of related containers.

<p align="center">
  <img src="images/pods.JPG" alt="Esempio di immagine" />
</p>

One of the design goals of containers is to separate services into single containers: microservices. Then you combine containers together to build larger services. Pods allow you to group multiple services
together to form a larger service managed as a single entity. One of the goals of Podman is allowing you to experiment with pods

## Systemd Integration
Podman Compose executes the Podman command directly, rather than communicating with Podman's API socket. This eliminates the need to run the Podman service to provide the API, saving resources. Because it uses Podman's regular command line and fork-exec model, it is easier to trace and manage on the system. For example, Podman Compose can easily be managed by a systemd unit file.

Podman and systemd allow you to manage
the entire life cycle of the application on nodes without human intervention


## Play kube
Init containers
Users of podman play kube told us they want to build images as part of the play process. Because Kubernetes does not have a similar concept, we were at first hesitant to implement the idea. The more play kube gets used, the more it gets compared to Docker compose. That was the tipping point. Our users were right.

The new podman play kube feature looks for a directory with the same name as the image used in the YAML file. If that directory exists and there is a Containerfile or Dockerfile present in that directory, Podman builds the container image.


## Why should we care about Podman?  

Podman is fundamentally design with security in mind, paving the way for running containers in high-security enviroments. A daemon running as root generally does not meet these requirements and has prevented lots of security-conscious companies from widespread adoption of Docker. Podman containers have always been rootless, while Docker is an all-in-1 tool for container management and creation, and including the fact that the architecture is daemon-less, the surface of attack is highly reduced.

## Podman and Docker compatability 
Podman is a powerful alternative to Docker, but the 2 can also work together,supporting most Docker commands and Dockerfile syntax. This compatibility makes transitioning from Docker to Podman smoother for developers. Some developers combine Podman and Docker, using Docker during the development stage and transferring their program to Podman in runtime environments. 


### Podman commands (bash)
One of Podman's greatest advantages is its CLI compatibility with Docker, because Podman does almost everything that Docker can do with the same command line as Docker. In fact, when building Podman, Docker users can adapt without any significant changes. For example, you can use the ```alias``` command to create a docker alias for Podman:
```
$ alias docker=podman
```
You can run other commands, such as ```pull```, ```push```, ```build```, ```commit```, ```tag```, and more with Podman. Podman’s CLI is similar to Docker’s, so users who are familiar with one are likely to have success with the other.

#### Podman exclusive commands
Podman other than having the same commands as Docker, it has also exclusive commands, which are ```kube```,
```systemd``` and ```pod```.

```podman pod```: Manages pods, which are groups of one or more containers sharing the same network namespace.
  For ```podman pod```, we can have:
  - ```create``` to **build** a pod;
  - ```start``` to **start** the pod;
  - ```stop``` to **stop** the pod;
  - ```rm``` to **remove** the pod;

  For ```podman generate systemd```, we can have:
  - ```create``` to **build** or rebuild services;
  - ```start``` to **start** the service (defined in the yaml file);
  - ```stop``` to **stop** and remove the service.
  - ```rm``` to **stop** and remove the service.
 Generates systemd unit files to manage containers or pods with systemd

  For ```podman <subcommand> kube```, we can have:
  - ```generate``` creates a **YAML** description of a Podman container or pod to run in Kubernetes;
  - ```play``` subcommand that allows Podman to **run** pods based on a Kubernetes YAML file;
Subcommand group for handling Kubernetes YAML file operation
podman play kube: Deploys containers based on Kubernetes YAML files.

<yaml file-name>

### dockerfile
Thanks to the ```OCI``` format, Podman support the same syntax and can build images from the dockerfile, meaning that Podman can ```pull/push``` images to/from Docker's registries, also the switch of the workflow from Docker to Podman and viceversa easier. 


### podman-compose
Podman supports the ```docker-compose.yml``` file, by processing them into Podman CLI commands with ```podman-compose```, while Podman Compose is better integrated with Podman (as it was designed from the ground up to work with Podman) and makes better use of rootless containers and pods. However, Docker Compose is much more supported, tested and more likely to be stable, while the Podman team is more focused ```podman generate kube``` and ```podman play kube```, which allow Kubernetes (An orchestation software) YAML to be used directly with Podman similar to Compose.


## Podman in depth
In this paragraph we will show basic functions of Podman.

### Rootless containers
Here we will show how to setup the rootless containers, but first of all we assume that we already have a non-root user, in case named brad and able to use podman. 

First of we install the `slirp4netns` package, we increase the number of user namespaces in the kernel.
```shell
 apt-get install slirp4netns
 sudo sysctl user.max_user_namespaces=28633
 echo "user.max_user_namespaces=28633" | sudo tee -a /etc/sysctl.conf
 sudo sysctl -p
```
We add the configurations for UID and GID that will be mapped in the container in the files `/etc/subuid` e `/etc/subgid`. But first we take a look on the files and make sure that the ranges are not overlapping.
```
 cat /etc/subuid
 cat /etc/subgid
```
After our fist verification, we add the configurations for our user brad, keep note that the numbers may vary and the most important thing is to have a sufficient interval for our container.
```
 echo "brad:165536:65536" | sudo tee -a /etc/subuid
 echo "brad:165536:65536" | sudo tee -a /etc/subgid
 cat /etc/subuid
 cat /etc/subgid
```
After our configuration we can run our image with the user brad by exposing port 8080.
```
podman run -d -p 8080:8080 your-image
podman ps
```
Output:
```
CONTAINER ID  IMAGE                     COMMAND               CREATED        STATUS            PORTS                   NAMES
f3b9c6d5eabc  localhost/hello-world-image:latest  python3 server.py  5 minutes ago  Up 5 minutes ago  0.0.0.0:8080->8080/tcp  nostalgic_morse
```

We can check the our rootless configuration is set up properly by running tge `podmman unshare` command inside the modified user namespace:
```
podman unshare cat /proc/self/uid_map
         0       1001     1
         1       100000   65536
     65537       165536   65536
```

### Systemd


### Pods setup
In this paragraph we analyze the Chapter 6 of the [Magnus Larsson repository](https://github.com/PacktPublishing/Microservices-with-Spring-Boot-and-Spring-Cloud-Third-Edition/tree/main/Chapter06).\

It is important to notice that in the microservices folder there are 4 services. Let's analyze each Dockerfile, then we can watch the Docker Compose whose use this files.


### Systemd 


### Kube play


## Sources
- https://www.redhat.com/en/topics/containers/what-is-podman
- https://github.com/containers/podman/blob/main/docs/tutorials/rootless_tutorial.md
