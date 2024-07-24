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






