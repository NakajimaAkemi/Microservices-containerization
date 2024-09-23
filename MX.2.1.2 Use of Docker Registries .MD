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

### Useful commands 

This guide will walk you through the steps to build a Docker image and upload it to a Docker registry using Docker commands.


### Steps

#### 1. Build Your Docker Image

Navigate to the directory containing your `Dockerfile` and run:

```bash
docker build -t your-image-name:your-tag .
```

- Replace `your-image-name` with the desired name for your image.
- Replace `your-tag` with a tag like `latest` or `v1.0`.

**Example:**

```bash
docker build -t myapp:latest .
```

#### 2. Tag the Image for the Registry

Tag your image with the registry URL and your repository name to prepare it for uploading.

```bash
docker tag your-image-name:your-tag your-registry-username/your-repo-name:your-tag
```

**Example for Docker Hub:**

```bash
docker tag myapp:latest myusername/myapp-repo:latest
```

#### Notes

- **Private Registries**: If you're using a private registry, include the registry URL in the tag.

  ```bash
  docker tag myapp:latest registry.example.com/myusername/myapp-repo:latest
  docker push registry.example.com/myusername/myapp-repo:latest
  ```

#### 4. Push the Image to the Registry

Upload your tagged image to the Docker registry.

```bash
docker push your-registry-username/your-repo-name:your-tag
```

**Example:**

```bash
docker push myusername/myapp-repo:latest
```

### 5. Verify the Uploaded Image

Log in to your Docker registry web interface to ensure the image has been uploaded successfully.






