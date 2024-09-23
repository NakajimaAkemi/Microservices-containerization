# Pull and Pushing Docker Containers
In this section we will show a simple guide on how to Download and execute a Container image from the Docker supported registries.

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

_ _ _



Now you have successfully downloaded and, if desired, run a container image using Docker commands.

# How to Upload a Docker Image to a Registry

This guide will walk you through the steps to build a Docker image and upload it to a Docker registry using Docker commands.

## Prerequisites
As prerequisite we need to have a *DockerHub Account* in order to pull image with Docker CLI. In the following step we will use DockerHub registry as example, but other registry services can be used such as  **AWS ECR**, **Google Container Registry** and more.

## Steps

### 1. Build Your Docker Image

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

### 2. Tag the Image for the Registry

Tag your image with the registry URL and your repository name to prepare it for uploading.

```bash
docker tag your-image-name:your-tag your-registry-username/your-repo-name:your-tag
```

**Example for Docker Hub:**

```bash
docker tag myapp:latest myusername/myapp-repo:latest
```

### 3. Log In to the Docker Registry

Authenticate with your Docker registry account.

```bash
docker login
```

- For Docker Hub, simply run `docker login` and enter your credentials.
- For other registries, you may need to specify the registry URL.

**Example:**

```bash
docker login
```

### 4. Push the Image to the Registry

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

## Summary of Commands

```bash
# Build the image
docker build -t myapp:latest .

# Tag the image
docker tag myapp:latest myusername/myapp-repo:latest

# Log in to Docker registry
docker login

# Push the image to the registry
docker push myusername/myapp-repo:latest
```

## Notes

- **Private Registries**: If you're using a private registry, include the registry URL in the tag.

  ```bash
  docker tag myapp:latest registry.example.com/myusername/myapp-repo:latest
  docker push registry.example.com/myusername/myapp-repo:latest
  ```

- **Authentication**: Make sure you have the necessary permissions to push images to the repository.
- **Security**: Never expose your Docker registry credentials in your code or repositories.

