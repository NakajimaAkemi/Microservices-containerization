# **Introduction to Containerization**

In this section, we will introduce containerization technology, providing a basic overview of containers and their use case in microservices deployment.

## Types of Deployment
### Traditional Deployment
Initially, applications were deployed on physical servers, sharing the resources. This approach led to many problems due to conflicts with libraries, dependencies, and other performance needs. One solution was to deploy applications on separate servers, but this was costly and resulted in underutilized resources.

### Virtualized Deployment
Virtualization is a technology that allows the creation of one or more running environments simulated within a pool of shared resources. These resources are efficiently coordinated and provided as virtual resources by the hypervisor. This technology enhances resource utilization, improves scalability, and simplifies the management of IT infrastructure by allowing multiple virtual machines to run on a single physical machine.


<p align="center">
  <img src="images/VMS.png" alt="Esempio di immagine" />
</p>



## Introduction to Container technology
Containers are software units that run on the host's operating system. This approach offers the advantage of not requiring any modifications to the application to adapt it to the host's environment, making transitions between environments easy and seamless.\
Containers provides also isolation, portability, and efficiency by packaging everything an application needs to run, without the overhead of a full OS.


<p align="center">
  <img src="images/Containers.JPG" alt="Esempio di immagine" />
</p>

Containers are similar to virtual machines but much lighter, as they contain only the essential components needed to run an application, such as:
- **runtime**: the minimal runtime environment needed to execute the application; this typically includes system libraries, shared resources, and the necessary binaries.
- **code**: containers encapsulate the application code; unlike VMs, which run an entire guest OS, containers share the host OS kernel and only package the application-specific code and dependencies.
- **tools**: any necessary tools and utilities required by the application to perform its functions (such as scripts, binaries, executables and debugging tools).
- **libraries**: the dependencies and libraries that the application relies on to run, ensuring all necessary software is included without the overhead of a full operating system.

### Examples of Container Technologies

- Docker
- Podman 
- Kurbenets

### Use cases of Containers
#### Microservices and Applications Isolation
(Todo)
#### CI/CD
(Todo)


### Container's lifecycle
#### Image
A container is created by the execution of an image, it implements an incremental file system (overlay), with layer on top of others, each command rapresents a layer and are read only. Each layer depends pn the layers underneat. When running, a container will have a writable layer on top called "container" layer.
Once used a container can be turned into an image, turning the container layer in read only an image layer.




#### Container
(Todo)

<p align="center">
  <img src="images/DockerProcess.jpg" alt="Esempio di immagine" />
</p>


#### What is Open Container Initiative (OCI)?
The OCI is a vendor-neutral initiative established by Docker, CoreOS and other industies with the objective of creating an industry standard around container technology to ensure that different tools and platforms can work together seamlessly.
The OCI specifies:
- OCI image format
- OCI runtime specification

#### Image example
(Todo)


#### Container distribution
Once an image is created, we can save the image in a public or private repository, called registry.
These registries can allow other users to pull images and use them. Be aware to use only images from trusted
authors or official distributors.

### Plugins
(Todo)
- Isolation customization
- Volumes
- Secrets
- Networks











## Sources and additional readings:
- https://www.oracle.com/it/cloud/cloud-native/container-registry/what-is-docker/
- https://www.geeksforgeeks.org/what-is-docker-image/









