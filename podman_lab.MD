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





