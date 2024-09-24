# Overview
In this section we will show more specific applications of Podman, such as Pods and Podman kube functionalities.



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



### Pod management CLI commands
```podman pod```: Manages pods, which are groups of one or more containers sharing the same network namespace.
  For ```podman pod```, we can have:
  - ```create``` to **build** a pod;
     - ```--name <pod-name>```: specifies a name for the Pod, otherwise it's randomly generated.
     - ```--network <network-name>```: Attaches a network to the pod.
  - ```start``` to **start** the pod;
  - ```stop``` to **stop** the pod;
  - ```rm``` to **remove** the pod;
  - ```ps``` to **show** running pods;
  - ```top <pod-name>``` to **show** running containers inside the pod;
  - ```inspect <pod-name>``` to **inspect** the running pod;

#### Pods networking
Podman uses bridges networks in order to let containers to communicate with eachother, when a pod is created, Podman automatically creates automatically a bridge network.
For the communication between pods, we need to configure a network in order to allow them to communicate.

### Pods setup
#### Pod creation
First of all we create our own pod with the following command and check the existense of our pod:
```bash
podman pod create --name my-nginx-pod
podman pod ps
```
As we can see from the output, there is a container inside the pod, and that's the default infra container.

#### Running a container inside the pod
We can a container inside the pod we just created, basically it's just the basic `podman run` command with `--pod <pod-name>` option added.
```bash
podman run -d --pod my-nginx-pod --name my-nginx -p 8080:80 nginx:latest
```
Everything is setup by command line, may it be the port forwarding, volume attachments and more.

#### Example of multicontainer Pod
Now we will try to run a microservice ecoystem in [example](https://github.com/NakajimaAkemi/Microservices-containerization/tree/master/workdir/isolation-compose), but this time instead of `docker-compose` we will try to use pods. As we defined before we need two networks:
 - `front_net`: For Nginx to expose to the public.
 - `back_net`: For the Flask and PostgreSQL communication.

Defining them with the CLI commands: 
 ```bash
podman network create back_net
podman network create front_net
```












## Podman kube
Podman provides a feature where given a Pod, we can gerate and run a Kubernetes manifest YAML file trough the commmand ```podman <subcommand> kube```:
  - ```generate``` creates a **YAML** description of a Pod.
  - ```play``` subcommand that allows Podman to **run** Pods based on a Kubernetes YAML file;

Let's say that we have the Pod `my-nginx-pod` that we defined in the section before, with that we generate the YAML manifest file:

```bash
podman kube generate my-nginx-pod > my-nginx-pod.yaml
```
Now we can run the Pod trough the YAML file file the `play` command:
```bash
podman play kube my-nginx-pod.yaml
```

With this we can seamlessly transition the workload to Kubernetes.



