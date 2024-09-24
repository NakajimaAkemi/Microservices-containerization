# Pull and Pushing Docker Containers
In this section we will show a simple guide on how to Download and execute a Container image from the Docker supported registries.

## Authenticating to a container registry
If we need to access specific registries or other registries different than DockerHub we need to execute an authentication procedure, with the command `docker login`:

  - `<registry-url>`: Not neccessary if we want to login to DockerHub

     - `<port>`: If there's a specific port to connect to.

Right after the CLI will ask your credentials and the login will complete.

> [!WARNING]
> In some cases the login is tipically executed using cloud-specific tools or commands.



## Pulling Docker images
### Useful commands 
In this section we will introduce useful commmands that can be used in order to fetch container images using the following:

- `docker search <image-name>`: this will allow us to search for the image in the DockerHub, if we're unsure which image we need.

- `docker pull <image-name>`: Command to download the image with the specified name
    - `:<tag>` : It's non mandatory option, we can put if we're searching for a specific container version. 

- `docker images`: Displays the images that are stored locally, we can use to verify if the download is successful.



#### Example of search
```bash
docker search ubuntu
```

#### Example of Docker pull with tag
```bash
docker pull ubuntu:latest
```
If you don't specify a tag, Docker will download the `latest` tag by default:

### Pulling Non-DockerHub images
After we authenticated we can download images from the registry with following command:
```bash
docker pull <registry-url>/<repository>/<image-name>:<tag>
```
#### Example
We want to pull a `my-app` image tagged `latest` residing in the repo `my-repo` from the `registry.example.com` registry.

```bash
docker pull registry.example.com/my-repo/my-app:latest
```
Now you have successfully downloaded and, if desired, run a container image using Docker commands.

## Pushing Docker images
### Prerequisites
As prerequisite we need to have a *DockerHub Account* in order to pull image with Docker CLI or other registry services such as  **AWS ECR**, **Google Container Registry** and more.


### Steps
In this procedure we alredy assume that we have a `your-image` image already built locally. Wether it's tagged or not doesn't matter since images can have more than one tag


#### 1. Tag the Image for the Registry
This an important step for the procedure, because with the tag we indicate were the images are uploaded.
There are two different cases for tagging:
  - `<username>/<repository>:<your-tag>`: DockerHub registry
  - `<registy-url>/<username>/<repository>:<your-tag>`: Private registry

_As we can see the only difference is the inclusion of the registry's URL._

> [!NOTE]
> If you ask yourself why is there an additional `<your-tag>` in the registry tag, note that it is a way to version control and it's not mandatory.


##### Examples
We are the user `myusername` and let's say i want to upload `my-app:latest` image to my DockerHub repository `myapp-repo`, first of all i need to tag the image appropriately.
```bash
docker tag myapp:latest myusername/myapp-repo:your-tag
```

As second example we tag the same image but this time we specify the registry URL `registry.example.com`
```bash
docker tag myapp:latest registry.example.com/myusername/myapp-repo:your-tag
```

#### 2. Push the Image to the Registry
After tagging the image with the correct registry URL, you can push it using the `docker push` command.

##### Examples
```bash
docker push myusername/myapp-repo:your-tag
```
or in the case of private registry

```bash
docker push registry.example.com/myusername/myapp-repo:your-tag
```

#### 3. Verify the Uploaded Image

Log in to your Docker registry web interface to ensure the image has been uploaded successfully.






