# **Introduction to Containerization**

In this section, we will introduce containerization technology, providing a basic overview of containers and their use case in microservices deployment.

## Traditional Deployment
Initially applications were deployed in a physical server, sharing the resources, this approach was the 
source of many problems, caused by conflicts of libraries, dependencies and other needs of performance.
One solution was to deploy applications in different servers, but it was costly and the respurces were 
underutilized.

## Virtualized Deployment

Virtualization is a technology that allows to create one or mode running enviroments simulated in a pool of
shared resources efficiently coordinated and provided in the form of virtual resources by the Hypervisor. 



## Introduction to Container technology
Containers are a software unit exexuted by the physical host's OS, it's a similar aproach to VM with the
difference that its lighter thanks to the fact that it contains only the neccessary components to allow
the application to run correctly and efficently, such as runtime, code, tools, libraries and other
kinds of dependencies. The advantage of this approach is the fact that there is no need to modify the 
application in order to adapt it to the host's enviroment, allowing and easy and seamless transition between
enviroments.

### Examples of Container Technologies


# How containers are structured#



## What is Open Container Initiative (OCI)?
The OCI is a vendor-neutral initiative established by Docker, CoreOS and other industies with the objective of creating an industry standard around container technology to ensure that different tools and platforms can work together seamlessly.
The OCI specifies:
- OCI image format
- OCI runtime specification


### From Dockerfile to Image













