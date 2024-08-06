## Cut content

## Basic info
 - **PID (Process Identifier)**: is a unique numerical identifier assigned by the operating system to each process running on a system.
 - **namespace**: is a feature in Linux that isolates and virtualizes system resources for a set of processes. It creates an environment where a process or a group of processes sees a distinct set of resources, such as file systems, network interfaces, process IDs, and user IDs. 

- **systemd**:**systemd** is a system and service manager for Linux operating systems. It is responsible for initializing the system (booting it up) and managing system processes and services once the system is running;


## Rootless containers and user name-space separation

Probably the most significant feature of Podman is the ability to run containers in rootless mode. In many situations you don't want to give full root access to your users, but users and developers still need to run containers and build images. Every container process is managed with the current user's permissions, reducing the necessity of higher clearance.
<p align="center">
  <img src="images/rootless.png" alt="Esempio di immagine" />
</p>
**Figure**: shows multiple users running Podman independent of each other, without any root access.

Also the support of the user namespace is an important functionality that allows us to execute containers in rootless mode, and allows us to isolate the container from the host and other processes in execution.
Basically the user name-space allows us to define ranges of `UIDs (user identifiers)` and a `GIDs (group identifiers)` mapping from the host to completely different values in the container process. It allows processes to have privileges inside of the container, but no privileges if they escape the container.


>Example: While inside the container our UID may be 0 running as root inside, outside on the host it's considered with an unprivileged UID like 100000, also any file object that isn't mapped to the user namespace will be trated as owned by nobody and the container process will not be allowed access unless the object is accessible by others. Even if the process manages to escape the container, the kernel will treat it as UID=100000. 





## Podman lab
Use case
You have been asked to deploy a container running a web based application onto a server in your 
environment. 
In addition to deploying it, you need to be able to start alternate copies and remove any 
non-running containers.


The rhel9-httpd container includes a bash shell. In this step, you will deploy the container in an interactive fashion, which will allow you to run commands within the deployed container. In the Podman tab run the following:


podman run -it rhel9-httpd /bin/bash


You should now be looking at a generic bash shell prompt:

bash-5.1#

## shows the running pods
podman ps -a

podman rm $(podman ps -a | grep Exited | cut -d" " -f1)

The output of this removal is the full CONTAINER ID which was removed from the system.

## Deploy a container image into a detached container runtime
Running an interactive container is all well and good, however, if the container is offering applications or services, you do not need to interact with it.

The rhel9-httpd container, for example, is configured with an Apache web server running within it. You will create a new instance of this container in a detached mode. Further, the command provided will configure port forwarding for the Apache web server so that connections to the host's port 8081 will redirect to the running container's port 80 (Apache service).


Observe that the STATUS is Up and there is a new section in the output PORTS that indicates the port forwarding defined for the container.
Test the webserver on the container with curl.
curl localhost:8081


## Cleanup
Unlike interactive containers, detached containers are stopped using podman stop

podman stop $(podman ps -a | grep Up | cut -d" " -f1)



## rootless 
For purely demonstration purposes, we will define a new user with no root privileges, verify and configure the user namespace.
```shell
adduser brad
su brad
sudo mkdir -p /run/user/$(id -u brad)
sudo chown -R $(id -u brad):$(id -g brad) /run/user/$(id -u brad)
su - brad
export XDG_RUNTIME_DIR=/run/user/$(id -u)
echo 'export XDG_RUNTIME_DIR=/run/user/$(id -u)' >> ~/.bashrc
source ~/.bashrc

echo "brad:100000:65536" | sudo tee -a /etc/subuid
echo "brad:100000:65536" | sudo tee -a /etc/subgid
#make sure it does not overlap
cat /etc/subuid
cat /etc/subgid

# configuration of conf file for namespace in runtime
mkdir -p ~/.config/containers
cat <<EOF > ~/.config/containers/containers.conf
[engine]
runtime = "crun"
[containers]
userns="keep-id"
EOF


#make sure it has the privileges
chown brad:brad ~/.config/containers/containers.conf

#test
podman run --rm hello-world


```
Now we will need the UID of our non-root user brad.
```
su brad
id
```
Once we have our UID we will need to setup the runtime directory for brad in order to be able for it to run Podman, in our case the UID is ```1001```.
```
sudo mkdir -p /run/user/1001
sudo chown -R 1001:1001 /run/user/1001
```
Now we switch to the user brad and verify if it can use the Podman commands.
```
su brad
podman --version
```



## Systemd
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



