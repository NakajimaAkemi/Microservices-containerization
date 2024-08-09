# Docker Image Creation Guide

This guide explains how to create Docker images for a simple Python application, a Java application, and a basic image that includes the `ls` command.

## Prerequisites

- Docker installed on your machine.
- Basic understanding of Docker.

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
