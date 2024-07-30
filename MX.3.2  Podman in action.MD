# Overview
In this section we will do advanced applications of Podman, such as Pods networking, Rootless containers and so on.



## Rootless containers and user name-space isolation
Here we will show how to setup the rootless containers, but first of all we assume that we already have a non-root user, in case named brad and able to use podman. 


In order to use networking other than the host networking, Podman uses the slirp4netns package, it provides user-mode networking for unprivileged network namespaces and must be installed on the machine in order for Podman to run in a rootless environment.

> In rootless Podman, you cannot create full, separate networking for containers because
rootless processes are not allowed to create network devices and modify the firewall rules.
Rootless Podman uses slirp4netns to configure the host network and simulate a VPN for the container. 

> Note that the kernel still will not allow a non-privileged process to bind to ports less than 1024. 

First of we install the `slirp4netns` package
```shell
  apt-get install slirp4netns
```
After that we increase the number of user namespaces in the kernel.
```shell
 sudo sysctl user.max_user_namespaces=28633
 echo "user.max_user_namespaces=28633" | sudo tee -a /etc/sysctl.conf
 sudo sysctl -p
```

Most current Linux distributions include a version of shadow-utils that uses the /etc/subuid and /etc/subgid files to determine what UIDs and GIDs are available for a user in a user namespace, on my system I have allocated 65536 UIDs/GIDs starting at 165536 for the user “brad”.


```
cat /etc/subuid
bilel:100000:65536
brad:165536:65536
jon:231072:65536
```
```
cat /etc/subgid
bilel:100000:65536
brad:165536:65536
jon:231072:65536
```
The useradd program automatically allocates 65536 UIDs for each user added to the system. If you have existing users on a system, you would need to allocate the UIDs yourself manually. The format of these files is `username:STARTUID:TOTALUIDS`.
The manual allocation goes like this:
```
 echo "brad:165536:65536" | sudo tee -a /etc/subuid
 echo "brad:165536:65536" | sudo tee -a /etc/subgid
```
> Root privileges are required to add or update entries within these files.

> If you set multiple rootless container users, use unique ranges for each user

> It is recommended to use 65536 UIDs and GIDs for maximum compatibility with existing container images, but the number can be reduced


We can check the our rootless configuration is set up properly by running the `podmman unshare` command inside the modified user namespace, the output shows the mapping of UIDs between host and container namespace:
```
podman unshare cat /proc/self/uid_map
         0       1001          1
         1     165536      65536
```

After our configuration we can run our image with the user brad by exposing port 8080.
```
podman run -d -p 8080:8080 your-image
podman ps
```
Output:
```
CONTAINER ID  IMAGE                     COMMAND               CREATED        STATUS            PORTS                   NAMES
f3b9c6d5eabc  localhost/hello-world-image:latest  python3 server.py  5 minutes ago  Up 5 minutes ago  0.0.0.0:8080->8080/tcp  nostalgic_morse
```
We check with the command below that the process of our container is owned by brad, basically we are able to run our application container with root-less privileges.
```
ps aux | grep nostalgic_morse
brad       12662  0.0  0.0   4024  2048 pts/0    S+   09:55   0:00 grep --color=auto musing_bhabba
```
The command `podman top` allows us to examine the usernames of processes running insisde a container and identify their real UID on the host.
```
podman top nostalgic_morse
USER        PID         PPID        %CPU        ELAPSED            TTY         TIME        COMMAND
brad        1           0           0.022       5h6m57.584585461s  ?           4s          python3 server.py
```


### Additional options
Even in rootless container, the root of the container has namespace capabilities, meaning that is has a power of root over the user namespace.

#### specifying the mapping.
Podman can specify the mappings defined in /etc/subuid and /etc/subgid with the –subuidname and –subgidname options. This may seem redudant and in most cases it is, but this allows to have a finer control over the mapping of IDs and avoid problems based on separation of containers. 

Let's use the mapping of brad: 
```
podman  run --subuidname=brad --subgidname=brad -d -p 8080:8080 your-image cat /proc/self/uid_map
         0     165536     65536
podman  run --uidmap=0:100000:70000 --gidmap=0:200000:70000 your-image cat /proc/self/uid_map
         0          100000      70000
```
The output indicates the uid_map table has three columns, the first one is the `initial ID for the range in the new namespace`, the second one is the `initial ID in the parent namespace`, and the third is the `size of the mapping`. Similarly for the GIDs, we can look up the gid_map file.


#### user option
The `--user` option allows us to manyally set the UID of the user inside the container, this way we can prevent it to have the root privileges inside the container (which has UID=0).
```
podman run --user 1000 -d fedora sleep 10
976d7f3f034d38657cfba60aef406e7f65eae9eef735619ca7c13f8a946a0122

The command podman top, allows us to see the UIDs of the user inside and the host user.
podman top -l user huser
USER HUSER
1000 100999
```
Without the --user flag we would have an output similar to the following:
```
podman top -l user huser
 USER HUSER
 root 3267
```

The --user option is still very necessary and adds a lot of security even when using rootless Podman, and users should still use it to be as secure as possible.

#### userns option
> *Beware!* The --userns option is incompatible with --gidmap, --uidmap, --subuidname and --subgidname.

In the `podman run` command we can specify the argument `-userns=<key>` where `<key>` can be:
 - *`""` (unset)*: we map the User UID to root user (UID=0) inside the container;
 - *`keep-id`*: we keep the host user's UID within the container, this way if it's not root even inside the container we won't have root privileges;
 - *`auto`*: the host user UID is not mapped into the container, an unique UID will be selected from the unused one automatically creating a unique user namespace;
 - *`nomap`*: the host user UID is not mapped into the container, the first UID from /etc/subuid range is mapped as the root user inside the container;

*Example:*
```
podman run --userns keep-id -d fedora sleep 100
ps -ef | grep sleep
dwalsh 198080 198069 1 10:57 ? 00:00:00 sleep 100
```
The `auto` and `nomap` options by not mapping the user's UID into the container, the files in the home's directory of the user are protected can't be accessed within the container, also it prevents to have containers that share `UIDs`.




> *Note*: Podman, in auto mode, is looking for 1024 or more UIDs that are not currently used within the user's user namespace. Because containers were already run on the system, and Podman defaults to using 65,000 UIDs, all of the UIDs are used. We need to destroy all of other containers that use the entire UID mappings.


### mounts and volumes management


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
## Communication inside the Pod

## SELinux and Apparmor

## Logging and Monitoring


## CI/CD Pipeline
