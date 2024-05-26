# **Introduction to Containerization**

In this section, we will introduce containerization technology, providing a basic overview of containers and their use case in microservices deployment.

## Types of Deployment
### Traditional Deployment
Initially, applications were deployed on physical servers, sharing the resources. This approach led to many problems due to conflicts with libraries, dependencies, and other performance needs. One solution was to deploy applications on separate servers, but this was costly and resulted in underutilized resources.

### Virtualized Deployment
Virtualization is a technology that allows the creation of one or more running environments simulated within a pool of shared resources. These resources are efficiently coordinated and provided as virtual resources by the hypervisor. This technology enhances resource utilization, improves scalability, and simplifies the management of IT infrastructure by allowing multiple virtual machines to run on a single physical machine.

![Virtualized Deployment](real-world-project/images/VMS.png)


## Introduction to Container technology
Containers are software units that run on the host's operating system. This approach offers the advantage of not requiring any modifications to the application to adapt it to the host's environment, making transitions between environments easy and seamless.
They are similar to virtual machines but much lighter, as they contain only the essential components needed to run an application, such as:
- runtime
- code 
- tools 
- libraries

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

#### Plugins
(Todo)











## Sources and additional readings:
- https://www.oracle.com/it/cloud/cloud-native/container-registry/what-is-docker/









