# Overview
In this section we will do advanced applications of Podman, such as Pods networking, Podman kube commands and more.



## Pods

Podman introduces the concept of pods, which are groups of one or more containers that share the same network namespace and storage resources simulating a single host. This concept is borrowed from Kubernetes and allows for easier management of related containers. Pods allow you to group multiple services
together to form a larger service managed as a single entity.

<p align="center">
  <img src="images/pods.JPG" alt="Esempio di immagine" />
</p>

Every Podman pod includes an `infra container`. This container holds the namespaces associated with the pod and allows Podman to connect other containers to the pod. It allows you to start and stop containers within the pod and the pod will stay running.

*Why should pods be considered?*
 - **Further Backend isolation**: Expose your frontend application to the public network and protect your database container in a private network, they will communicate over localhost while remaining isolated from other containers.
 - **Network sharing**: Containers in a Podman pod share the same network namespace, which means they can interact with each other without needing special network configurations.
 - **Convenient Management**: We can manage multiple containers as a single unit such as starting, stopping, or removing, affecting all containers within that pod.
 - **Resource Sharing**: Containers in a Podman pod can share volumes.
 - **Compatability with Kubernetes**: you can create and manage pods locally using Podman and then easily transition to running them in a Kubernetes environment;



### Podman exclusive commands
```podman pod```: Manages pods, which are groups of one or more containers sharing the same network namespace.
  For ```podman pod```, we can have:
  - ```create``` to **build** a pod;
  - ```start``` to **start** the pod;
  - ```stop``` to **stop** the pod;
  - ```rm``` to **remove** the pod;







## Podman kube
  For ```podman <subcommand> kube```, we can have:
  - ```generate``` creates a **YAML** description of a Podman container or pod to run in Kubernetes;
  - ```play``` subcommand that allows Podman to **run** pods based on a Kubernetes YAML file;
Subcommand group for handling Kubernetes YAML file operation
podman play kube: Deploys containers based on Kubernetes YAML files.


The new podman play kube feature looks for a directory with the same name as the image used in the YAML file. If that directory exists and there is a Containerfile or Dockerfile present in that directory, Podman builds the container image.




_ _ _












## Podman in action
In this paragraph we will show basic functions of Podman, here's how install Podman on your machine:

### Podman installation


### Pods setup
#### Pod creation
First of all we create our own pod with the following command and check the existense of our pod:
```
podman pod create --name mypod
podman pod ps
```
As we can see from the output, there is a container inside the pod, and that's the default infra container.

#### Running a container inside the pod
We can a container inside the pod we just created.
```
podman run -dt --name myubi --pod mypod registry.access.redhat.com/ubi8/ubi /bin/bash
```
This time if we run the `podman pod ps` command we can see that the number of pods increased to 2 containers. And if we run:
```
podman ps -a --pod
```
We can list all pods and the images associated with them.
###


It is important to notice that in the microservices folder there are 4 services. Let's analyze each Dockerfile, then we can watch the Docker Compose whose use this files.

#### Pod's informations

We can get information about the processes that are running inside the pod with 
```
podman pod top mypod
```

Or we can inspect the pod, showing information that describes the pod
```
podman pod inspect mypod
```
### Kube play




## Pods networking
Podman uses bridges networks in order to let containers to communicate with eachother, when a pod is created, Podman automatically creates automatically a bridge network.
For the communication between pods, we need to configure a network in order to allow them to communicate.

1. Network setup
```bash
podman network create mynetwork
```

2. Pod creation and linkage to our network
```bash
podman pod create --name pod1 --network mynetwork
podman pod create --name pod2 --network mynetwork
```

3. Container execution inside a Pod
```bash
podman run -dt --pod pod1 --name app-container your-app-name
podman run -dt --pod pod2 --name db-container postgres:13
```






