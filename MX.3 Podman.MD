# Podman Overview
Podman (short for pod manages) is an open-source container management tool developed by Red Hat, designed to deploy and manage containerized applications. Podman in many cases works just like Docker, but it offers enhanced security and the ability to run commands with non-root privileges.

## Basic informations
 - **fork/exec**: is a two-step process used in Unix-like operating systems for creating and running new processes. 
 - **PID (Process Identifier)**: is a unique numerical identifier assigned by the operating system to each process running on a system.
 - **namespace**: is a feature in Linux that isolates and virtualizes system resources for a set of processes. It creates an environment where a process or a group of processes sees a distinct set of resources, such as file systems, network interfaces, process IDs, and user IDs. 

- **systemd**:**systemd** is a system and service manager for Linux operating systems. It is responsible for initializing the system (booting it up) and managing system processes and services once the system is running;

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


## Rootless containers and user name-space separation

Probably the most significant feature of Podman is the ability to run containers in rootless mode. In many situations you don't want to give full root access to your users, but users and developers still need to run containers and build images. Every container process is managed with the current user's permissions, reducing the necessity of higher clearance.
<p align="center">
  <img src="images/rootless.png" alt="Esempio di immagine" />
</p>

Also the support of the user namespace is an important functionality that allows us to execute containers in rootless mode, and allows us to isolate the container from the host and other processes in execution.
Basically the user name-space allows us to define ranges of `UIDs (user identifiers)` and a `GIDs (group identifiers)` mapping from the host to completely different values in the container process. It allows processes to have privileges inside of the container, but no privileges if they escape the container.


>Example: While inside the container our UID may be 0 running as root inside, outside on the host it's considered with an unprivileged UID like 100000, also any file object that isn't mapped to the user namespace will be trated as owned by nobody and the container process will not be allowed access unless the object is accessible by others. Even if the process manages to escape the container, the kernel will treat it as UID=100000. 


## Concept of Pods

Podman introduces the concept of “pods,” which are groups of one or more containers that share the same network namespace and storage resources simulating a single host. This concept is borrowed from Kubernetes and allows for easier management of related containers.

<p align="center">
  <img src="images/pods.JPG" alt="Esempio di immagine" />
</p>

One of the design goals of containers is to separate services into single containers: microservices. Then you combine containers together to build larger services. Pods allow you to group multiple services
together to form a larger service managed as a single entity. One of the goals of Podman is allowing you to experiment with pods

## Systemd Integration

Podman supports systemd integration, which is tipically used to manage local services such as web servers, container engines, network daemons etc..
There are two common use cases for combining systemd and containers:
 - Running systemd inside a container
 - Using systemd to run containerized applications


With systemd unit files, you can: 
 - set uo a container or pod to start as systemd service
 - define the order in which containerized service runs and check for dependencies (another service is running, a file is available or resource is mounted)
 - control the state of the systemd system using systemctl command

Podman automatically sets up several mounts in the container, and systemd is good to go. While it's a comparatively small Podman feature, it was a huge leap for running containerized workloads when it was introduced.

Historically, other container tools have not supported systemd. Users faced the challenge of writing custom init scripts, which are prone to errors and a support burden for software vendors. With Podman, all these issues go away. Users can use systemd to install and run their applications in containers, just like anywhere else, and software vendors will not face the challenges of dealing with custom init scripts written by their users.

Systemd was responsible for a complete system initialization. Due to Red Hat integrating containers with systemd, you can manage OCI and Docker-formatted containers built by Podman in the same way as other services and features are managed in a Linux system. You can use the systemd initialization service to work with pods and containers.


Podman Compose executes the Podman command directly, rather than communicating with Podman's API socket. This eliminates the need to run the Podman service to provide the API, saving resources. Because it uses Podman's regular command line and fork-exec model, it is easier to trace and manage on the system. For example, Podman Compose can easily be managed by a systemd unit file.

Podman and systemd allow you to manage
the entire life cycle of the application on nodes without human intervention

### setup with Quadlets
> Podman generate systemd  is deprecated
With quadlets we define how to run a container in a format that is very similar to regualer systemd unit files. The container descriptions focus on the relevant container details and hide technical details of running containers under systemd. Create the <CTRNAME>.container unit file in one of the following directories:
 - /usr/share/containers/systemd/ or /etc/containers/systemd/ for root users
 - $HOME/.config/containers/systemd/, $XDG_CONFIG_HOME/containers/systemd/, /etc/containers/systemd/users/$(UID), or /etc/containers/systemd/users/ for rootless

We need to install the contaier-tools module
```apt-get install container-tools```

We create the unit file mysleep.container
```
cat $HOME/.config/containers/systemd/mysleep.container
[Unit]
Description=The sleep container
After=local-fs.target

[Container]
Image=registry.access.redhat.com/ubi8-minimal:latest
Exec=sleep 1000

[Install]
Start by default on boot
WantedBy=multi-user.target default.target
```
The container section we specifiy:
 - image we want to run
 - Exec the command we want to run inside the container

We create mysleep.service based on mysleep.container
systemctl --user daemon-reload

Check the status of mysleep.service
systemctl --user status mysleep.service

Verification
systemctl --user status mysleep.service


podman ps -a

Note:
 - the container has a systemd-  prefix this helps to distinguish  common containers from those running in systemd.


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
In this paragraph we will show basic functions of Podman, here's how install Podman on your machine:



### Systemd


### Pods setup
In this paragraph we analyze the Chapter 6 of the [Magnus Larsson repository](https://github.com/PacktPublishing/Microservices-with-Spring-Boot-and-Spring-Cloud-Third-Edition/tree/main/Chapter06).\

It is important to notice that in the microservices folder there are 4 services. Let's analyze each Dockerfile, then we can watch the Docker Compose whose use this files.


### Systemd 


### Kube play


## Sources
- https://www.redhat.com/en/topics/containers/what-is-podman
- https://github.com/containers/podman/blob/main/docs/tutorials/rootless_tutorial.md
