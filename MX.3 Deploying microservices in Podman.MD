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

Podman containers have always been rootless, while Docker only recently added a rootless mode to its daemon configuration.

This is probably the most significant feature of Podman, the ability to run containers in rootless mode.
In many situations you don't want to give full root acess to your users, but users and developers still need to run containers and build images. Requiring root access prevents lots if security-conscious companies from widespread adoption of Docker.
Docker has a client-server logic mediated by a daemon; the latter does not need the mediator

Every container process is managed with the current user's permissions, reducing the necessity of higher clearance, 

Podman containers have always been rootless, while Docker is an all-in-1 tool for container management and creation, Podman is far more specific for its applications on the aspects of containerization.

## Concept of Pods

## Why should we care about Podman?  
Podman is fundamentally design with security in mind, leveraing SELinux.
It has a smaller attack surface being just a running engine.
Rootless support built in
Integrated with systemd
Default approcah on Fedora and RHEL.

Not everybody has the luxury to build all thier own containers from scratch, because it would be an added cost stemming from the maintenance needs, such as patching a security issue.
Base OS also?
Another issue it other tools that run along side the container technology.

Podman changed the container landscape by offering the same high-performance capabilities as leading container engines, but with the flexibility, accessibility, and security features that many development teams are seeking. Podman can help you:

 - Manage container images and the full container lifecycle, including running, networking, checkpointing, and removing containers.
 - Run and isolate resources for rootless containers and pods.
 - Support OCI and Docker images as well as a Docker-compatible CLI.
 - Create a daemonless environment to improve security and reduce idle resource consumption. 
 - Deploy a REST API to support Podman’s advanced functionality.
 - Implement checkpoint/restore functionality for Linux containers with Checkpoint/Restore in Userspace (CRIU). CRIU can freeze a running container and save its memory contents and state to disk so that containerized workloads can be restarted faster.
 - Automatically update containers. Podman detects if an updated container fails to start and automatically rolls back to the last working version. This provides new levels of reliability for applications. 


Podman really paved the way for running containers in high-security environments, including research labs, banks, and insurance companies. A daemon running as root generally does not meet these requirements.




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


Provides a familiar command line experience compatible with the docker cli, grat for running/building/sharing containers outside of OpenShift, Can be Integrated in infrasturucture where docker daemon/cli are used today.
Podman's commands are run from the  command-line interface, which are similar to Docker


## Docker compatability
Thanks to the OCI format, Podman is compatible with Docker, meaning that Podman can pull/push images to/from Docker's registries, also the switch of the workflow from Docker to Podman and viceversa easier.

Podman is a powerful alternative to Docker, but the 2 can also work together. Users can easily switch between them by aliasing Docker to Podman (alias docker=podman) and vice versa. Additionally, an rpm called podman-docker can drop a “docker” into the system application path, which calls Podman for those environments where the “docker” command is needed, easing the transition from Docker. Podman’s CLI is similar to Docker’s, so users who are familiar with one are likely to have success with the other.
Some developers combine Podman and Docker, using Docker during the development stage and transferring their program to Podman in runtime environments.

Podman is a powerful alternative to Docker, but the 2 can also work together. Users can easily switch between them by aliasing Docker to Podman (alias docker=podman) and vice versa. Additionally, an rpm called podman-docker can drop a “docker” into the system application path, which calls Podman for those environments where the “docker” command is needed, easing the transition from Docker. Podman’s CLI is similar to Docker’s, so users who are familiar with one are likely to have success with the other.

Some developers combine Podman and Docker, using Docker during the development stage and transferring their program to Podman in runtime environments.

Podman is
often summarized with the simple line alias Docker = Podman because Podman does almost everything that Docker can do with the same command line as Docker. But as
you will learn in this book, Podman can do so much more. Understanding Docker is
not critical to understanding Podman, but it is helpful.

--Advantages and used cases--


--Podman Desktop--

--Deployment in Podman--



## Sources
- https://www.redhat.com/en/topics/containers/what-is-podman
