# Podman Overview
Podman (short for pod manages) is an open-source container management tool developed by Red Hat, designed to deploy and manage containerized applications. 


## Podman's architecture
--Differences between Podman and Docker--

Unlike Docker, Podman does not need a daemon running in the background, every command in Podman is executed as a process separated from the user. Every container process is managed with the current user's permissions, reducing the necessity of higher clearance, also a daemon may result in a single point of failure.

Podman containers have always been rootless, while Docker is an all-in-1 tool for container management and creation, Podman is far more specific for its applications on the aspects of containerization.

## Why Podman?
Podman changed the container landscape by offering the same high-performance capabilities as leading container engines, but with the flexibility, accessibility, and security features that many development teams are seeking. Podman can help you:

Manage container images and the full container lifecycle, including running, networking, checkpointing, and removing containers.
Run and isolate resources for rootless containers and pods.
Support OCI and Docker images as well as a Docker-compatible CLI.
Create a daemonless environment to improve security and reduce idle resource consumption. 
Deploy a REST API to support Podman’s advanced functionality.
Implement checkpoint/restore functionality for Linux containers with Checkpoint/Restore in Userspace (CRIU). CRIU can freeze a running container and save its memory contents and state to disk so that containerized workloads can be restarted faster.
Automatically update containers. Podman detects if an updated container fails to start and automatically rolls back to the last working version. This provides new levels of reliability for applications. 

Podman vs. Docker
Docker is a containerization technology that allows the creation and use of Linux containers. The main difference between Podman and Docker is Podman’s daemonless architecture. Podman containers have always been rootless, while Docker only recently added a rootless mode to its daemon configuration. Docker is an all-in-1 tool for container creation and management, whereas Podman and its associated tools like Buildah and Skopeo are more specialized for specific aspects of containerization. This makes it possible to customize your environments with only the tools you need.

Podman is a powerful alternative to Docker, but the 2 can also work together. Users can easily switch between them by aliasing Docker to Podman (alias docker=podman) and vice versa. Additionally, an rpm called podman-docker can drop a “docker” into the system application path, which calls Podman for those environments where the “docker” command is needed, easing the transition from Docker. Podman’s CLI is similar to Docker’s, so users who are familiar with one are likely to have success with the other.

Some developers combine Podman and Docker, using Docker during the development stage and transferring their program to Podman in runtime environments.

Podman is best suited for developers running containers without Kubernetes or Red Hat OpenShift Container Platform. For Kubernetes container orchestration, developers can use the open source, community-driven container engine CRI-O.


## Docker compatability
Thanks to the OCI format, Podman is compatible with Docker, meaning that Podman can pull/push images to/from Docker's registries, also the switch of the workflow from Docker to Podman and viceversa easier.

Podman is a powerful alternative to Docker, but the 2 can also work together. Users can easily switch between them by aliasing Docker to Podman (alias docker=podman) and vice versa. Additionally, an rpm called podman-docker can drop a “docker” into the system application path, which calls Podman for those environments where the “docker” command is needed, easing the transition from Docker. Podman’s CLI is similar to Docker’s, so users who are familiar with one are likely to have success with the other.


Some developers combine Podman and Docker, using Docker during the development stage and transferring their program to Podman in runtime environments.



## Podman, Buildah, and Skopeo
Podman is a modular container engine, so it must work alongside tools like Buildah and Skopeo to build and move its containers. With Buildah, you can build containers either from scratch or by using an image as a starting point. Skopeo moves container images between different types of storage systems, allowing you to copy images between registries like docker.io, quay.io, and your internal registry or between different types of storage on your local system. This modular approach to containerization results in a flexible, lightweight environment by reducing overhead and isolating the features you need. Working with containers makes it possible to use smaller, more modular tools that can focus on a single purpose and be updated as often as needed.

Think of Podman, Buildah, and Skopeo as a set of specialized Swiss Army knives. Combined, they can satisfy almost all container use cases. Podman is the biggest of these knives.

Podman and Buildah use runC―the OCI runtime―by default to launch containers. You can use runC to build and run an image, or you can use it to run Docker-formatted images. This Go language-based tool reads a runtime specification, configures the Linux kernel, and eventually creates and starts container processes. With some configuration changes, you can also use Podman with other footprints like crun.



--Advantages and used cases--


--Podman Desktop--

--Deployment in Podman--


-- daemonless Architecture --


-- Rootless containers -- 

-- CI/CD integration --

-- logging and monitoring -- 


-- secrets management --


-- buildah and skopeo -- 


<p align="center">
  <img src="images/dockerAndPodman.png" alt="Esempio di immagine" />
</p>

_(while searching for images for docker I found this - in case it can be useful)_





## Sources
- https://www.redhat.com/en/topics/containers/what-is-podman
