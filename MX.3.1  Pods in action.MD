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


## Play kube
Init containers
Users of podman play kube told us they want to build images as part of the play process. Because Kubernetes does not have a similar concept, we were at first hesitant to implement the idea. The more play kube gets used, the more it gets compared to Docker compose. That was the tipping point. Our users were right.

The new podman play kube feature looks for a directory with the same name as the image used in the YAML file. If that directory exists and there is a Containerfile or Dockerfile present in that directory, Podman builds the container image.


















<yaml file-name>

### dockerfile
Thanks to the ```OCI``` format, Podman support the same syntax and can build images from the dockerfile, meaning that Podman can ```pull/push``` images to/from Docker's registries, also the switch of the workflow from Docker to Podman and viceversa easier. 


### podman-compose
Podman supports the ```docker-compose.yml``` file, by processing them into Podman CLI commands with ```podman-compose```, while Podman Compose is better integrated with Podman (as it was designed from the ground up to work with Podman) and makes better use of rootless containers and pods. However, Docker Compose is much more supported, tested and more likely to be stable, while the Podman team is more focused ```podman generate kube``` and ```podman play kube```, which allow Kubernetes (An orchestation software) YAML to be used directly with Podman similar to Compose.


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

Podman compose configuration

```yaml
version: '3.8'
services:
  app:
    image: your-app-name
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    networks:
      - mynetwork

  db:
    image: postgres:13
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: database
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - mynetwork

volumes:
  postgres_data:

networks:
  mynetwork:
    driver: bridge

```

## Podman kube
YAML file example

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: demo-app-deployment
  labels:
    app: demo-app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: demo-app
  template:
    metadata:
      labels:
        app: demo-app
    spec:
      containers:
        - name: demo-app
          image: demo-app:latest
          ports:
            - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: demo-app-service
spec:
  selector:
    app: demo-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

