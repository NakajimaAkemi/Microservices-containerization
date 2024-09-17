# Docker Introduction to Dockerfile syntax
As seen before a  `Dockerfile` is a simple text file that contains a series of instructions on how to build a Docker image. Each instruction in a Dockerfile adds a layer to the image, creating a lightweight and reusable environment for running applications. In this paragraph we will introduce the essential and most important commands.

### 1. `FROM`

The `FROM` instruction specifies the base image from which your image will be built. It’s usually the first line in any Dockerfile.

```dockerfile
FROM ubuntu:20.04
```

This command sets the base image to Ubuntu 20.04.


### 2. `COPY`

The `COPY` instruction copies files or directories from your local machine into the Docker image.

```dockerfile
COPY . /app
```

This command copies everything from the current directory on your local machine to the `/app` directory in the Docker image.

### 3. `RUN`

The `RUN` instruction executes commands inside the image during the build process, typically used for installing software or setting up your environment.

```dockerfile
RUN apt-get update && apt-get install -y python3
```

This command updates the package list and installs Python 3 inside the image.

### 4. `WORKDIR`

The `WORKDIR` instruction sets the working directory inside the image. All subsequent instructions will be run from this directory.

```dockerfile
WORKDIR /app
```

This sets the working directory to `/app`.

### 5. `CMD`

The `CMD` instruction specifies the command that will run when the container starts, tipically it's written following the format 
`CMD ["executable","param1","param2"]` also referenced as **exec form**.

```dockerfile
CMD ["python3", "app.py"]
```

This command runs `app.py` using Python 3 when the container starts.

> [!IMPORTANT]
> There can only be one CMD instruction in a Dockerfile. If you list more than one `CMD`, only the last one takes effect.



### 6. `EXPOSE`

The `EXPOSE` instruction tells Docker which port your application will use so that it can be accessed from outside the container.

```dockerfile
EXPOSE 8080
```

This exposes port 8080 for access.

### 7. `ENV`

The `ENV` instruction sets environment variables in the container. These variables can be used by applications or scripts running inside the container.

```dockerfile
ENV ENVIRONMENT=production
```

This sets the `ENVIRONMENT` variable to `production`.


> [!IMPORTANT]
> Choosing the base image of our container is an important step, find the one that provides what you need and obviously stick to trusted and official distibutors.

> [!NOTE]
> The key difference between `CMD` and `RUN` is the moment the command is executed, the former is executed on build time and the former is execute during the container startup.








# Simple examples of Docker images
In this markdown we will show various examples of the creation of simple Docker images, a simple Python application, a Java application, and a basic image that includes the `ls` command. As last example we will try to deploy a simple Flask echo server.

All examples can also be found in [here](https://github.com/NakajimaAkemi/Microservices-containerization/tree/master/workdir)


## 1. Creating a Docker Image for a Simple Python Application

### Step 1: Write the Python Application

Create a file named `app.py`:

```python
# app.py
print("Hello, Docker!")
```

### Step 2: Create the Dockerfile

Create a `Dockerfile` in the same directory:

```Dockerfile
# Use an official Python runtime as a parent image
FROM python:3.9-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Run the command to execute the Python script
CMD ["python", "app.py"]
```

### Step 3: Build the Docker Image

```bash
docker build -t python-app .
```

### Step 4: Run the Docker Container

```bash
docker run python-app
```

**Expected Output**: `Hello, Docker!`

## 2. Creating a Docker Image for a Simple Java Application

### Step 1: Write the Java Application

Create a file named `App.java`:

```java
// App.java
public class App {
    public static void main(String[] args) {
        System.out.println("Hello, Docker!");
    }
}
```

### Step 2: Create the Dockerfile

Create a `Dockerfile` in the same directory:

```Dockerfile
# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Compile the Java program
RUN javac App.java

# Run the Java program
CMD ["java", "App"]
```

### Step 3: Build the Docker Image

```bash
docker build -t java-app .
```

### Step 4: Run the Docker Container

```bash
docker run java-app
```

**Expected Output**: `Hello, Docker!`

## 3. Creating a Docker Image with the `ls` Command

### Step 1: Create the Dockerfile

Create a `Dockerfile`:

```Dockerfile
# Use an official Alpine Linux image as a parent image
FROM alpine:3.18

# Run the ls command
CMD ["ls", "-la"]
```

### Step 2: Build the Docker Image

```bash
docker build -t ls-command .
```

### Step 3: Run the Docker Container

```bash
docker run ls-command
```

**Expected Output**: A detailed list of the root directory contents.


## 4. Creating a Docker Image for a Flask Echo Server 

We will create and containerize a simple Echo server using Flask, which echoes back any data sent to it.

### 1. Set Up the Flask Echo Server

First, let's create a Flask app that acts as an Echo server.

#### Create a Project Directory

```bash
mkdir flask-echo-server
cd flask-echo-server
```

#### Create a Python File

Create a Python file named `app.py` with the following content:

```python
from flask import Flask, request, jsonify

app = Flask(__name__)

# Route to echo the received data
@app.route('/echo', methods=['POST'])
def echo():
    data = request.json
    return jsonify({"echoed_data": data})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
```

This simple Flask application defines a POST endpoint (`/echo`). It expects JSON data, and then echoes back the same data.

### 2. Create a `requirements.txt` File

In the same directory, create a `requirements.txt` file to list the dependencies for the project. For this project, we'll need Flask.

```bash
Flask
```

### 3. Create a Dockerfile

Now, we will create a `Dockerfile` to containerize the Flask app. In the same directory, create a file named `Dockerfile` with the following content:

```Dockerfile
# Use the official Python image
FROM python:3.9-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the current directory contents into the container
COPY . .

# Install the required packages
RUN pip install -r requirements.txt

# Expose port 5000 for the Flask app
EXPOSE 5000

# Define the command to run the application
CMD ["python", "app.py"]
```

This Dockerfile does the following:
1. Uses a lightweight Python image.
2. Sets the working directory.
3. Copies the current directory contents into the container.
4. Installs dependencies from `requirements.txt`.
5. Exposes port 5000 for Flask.
6. Runs the `app.py` when the container starts.

### 4. Build the Docker Image

Now that you have your Python app and Dockerfile ready, you can build the Docker image.

Run the following command in your terminal:

```bash
docker build -t flask-echo-server .
```

This will create a Docker image named `flask-echo-server` based on the `Dockerfile`.

### 5. Run the Docker Container

Once the image is built, you can run the container:

```bash
docker run -p 5000:5000 flask-echo-server
```

This command maps port 5000 on your host to port 5000 in the container, allowing you to access the Flask app.

### 6. Test the Echo Server

Now, you can test the Echo server using a tool like `curl` or Postman.

#### Using `curl`:

```bash
curl -X POST http://localhost:5000/echo -H "Content-Type: application/json" -d '{"message": "Hello, Echo Server!"}'
```

For `Windows` command prompt we need to put escape character `\` around the strings
```bash
curl -X POST http://localhost:5000/echo -H "Content-Type: application/json" -d "{\"message\": \"Hello, Echo Server!\"}"
```


You should get a response like this:

```json
{
  "echoed_data": {
    "message": "Hello, Echo Server!"
  }
}
```

## Sources
- https://medium.com/@anshita.bhasin/a-step-by-step-guide-to-create-dockerfile-9e3744d38d11
- https://docs.docker.com
