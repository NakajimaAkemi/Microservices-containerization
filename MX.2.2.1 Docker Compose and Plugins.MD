# Docker Compose Overview
Docker Compose is a powerful tool that allows us to define and manage multi-container Docker applications. It is particularly useful when working with microservice ecosystems, as it enables the easy launch and coordination of multiple containers simultaneously. With Compose, we can configure networking, define infrastructure as code, and meet scalability requirements.


## Compose file and life cycle management
The Compose file is the heart of Docker Compose, it consists of a YAML file (`docker-compose.yml`) that allows to define an application ecosystem including services, networking, disk space and more.

### Key syntax elements of the Compose file
The `docker-compose.yaml` file follows a hierarchical structure by the use of identations.
- **version**: Specifies the Compose version to use.
- **services**: Defines the containers of the application, each service represents a container.
  - **[service-name]**: Name of the single service, the choice is at our discretion.
    - **image**: Specifies the image to use for the service.
    - **build**: Alternative to `image`, allows to build an image from a specific dockerfile.
    - **ports**: Maps the host's ports.
    - **volumes**: Allows to share data between container and host or just between containers.
    - **networks**: Defines the networks which the containers will utilize to communicate.

### Basic example of `docker-compose.yml`
Now we will ease into writing the compose file starting from a simple [example](https://github.com/NakajimaAkemi/Microservices-containerization/tree/master/workdir/simple-compose), we will try to deploy a simple Flask application mapped to the port 5000.

`Project structure`
```
simple-compose/
│
├── app/
│   ├── app.py
│   └── requirements.txt
└── docker-compose.yml
```

`docker-compose.yml`:
```yaml
version: '3'
services:
  flask:
    build: ./app
    ports:
      - "5000:5000"
```

`app/app.py`:
```python
from flask import Flask

app = Flask(__name__)

@app.route('/')
def hello():
    return "Hello from Flask in Docker!"

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)
```

`app/app.py`:
```text
flask
```

`app/Dockerfile`:
```Dockerfile
FROM python:3.9-slim
WORKDIR /app
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt
COPY . .
CMD ["python", "app.py"]
```

Now with everything set up, we will start the ecosystem with the `docker-compose` command (we will talk about it shortly) in the context of the project directory.
```bash
docker-compose up
```
To test everything we can `curl` or use a browser to `http://localhost:5000` to get the "Hello from Flask in Docker!" message. 


## Lifecycle di Docker Compose
Now that we know how to setup a simple ecosystem with compose, we will show how to really manage it with CLI commands. Docker Compose works with `docker-compose <command>` commands, allowing us to manage the ecosystem's lifecycle with the following commands:
- **`up`**: Start all services defined in the`docker-compose.yml` file.
- **`down`**: Stops and removes all containers, networks, volumes defined in the compose file.
- **`build`**: Builds the the Docker images specified in services.
- **`logs`**: Views the logs of all containers in execution.
- **`scale`**: Scales up or down the number of containers.

With this, we are able to setup and manage a simple multi-container ecosystem with the compose file and the `docker-compose` commands.

## Docker Compose Features
In this section we will introduce and explain various features of Docker compose, these features will allow us to set up and customize a multi-container ecosytem adapt to our necessities and use cases.

### Replicas
A **Replica** in Docker refers to the ability to istantiate more replicas of the same container, this allows us to scale horizontally improving the scalability, reliability and load balancing. 
We will create an [update version](https://github.com/NakajimaAkemi/Microservices-containerization/tree/master/workdir/replica-compose) of our first example by adding `deploy` field to the compose file and defining the number of replicas.

```yaml
version: '3'
services:
  flask:
    build: ./app
    ports:
      - "5000"
    deploy:
      replicas: 3
```
> [!NOTE]
> Since the service we're replicating is mapping ports, we will specify only port number and not the range, otherwise it will show an error.

As standard procedure we will start up the ecosystem and see the running containers.
```bash
docker-compose up
docker ps
```
Then we can verify by simply running a `docker ps` command and see the output (the output is totally indicative, it may vary).
```bash
CONTAINER ID   IMAGE        COMMAND         ...    PORTS
abc123         flask_app    "python app.py" ...    0.0.0.0:32768->5000/tcp
def456         flask_app    "python app.py" ...    0.0.0.0:32769->5000/tcp
ghi789         flask_app    "python app.py" ...    0.0.0.0:32770->5000/tcp
```
We can verify their reachability with a browser or the `curl` command:
 - http://localhost:32768
 - http://localhost:32769
 - http://localhost:32770

### Volumes
Docker volumes are a mechanism for persisting data generated by and used by Docker containers. There are different types of volumes: named, anonymous and bind. In this paragraph we want to analyze the characteristics of each type providing some examples.
Volumes allow us to maintain persistent data survining beyond the container lifecycle, facilitating persistence and sharing data between containers. Easy to backup and restore. Volumes are more performant in linux than bind volumes


_Is also important to say that if a service doesn't specify a volumes section, it means that the service won't have any volumes mounted. Essentially, **no data will be persisted outside the container** for that service._

#### Creare un Volume

Puoi creare un volume usando il comando:

```bash
docker volume create my_volume
```

#### Montare un Volume su un Container

Per montare un volume su un container, usa l'opzione `-v` o `--mount`:

```bash
docker run -d -v my_volume:/path/in/container my_image
```

#### Ispezionare un Volume

Per vedere i dettagli di un volume:

```bash
docker volume inspect my_volume
```

#### Rimuovere un Volume

Per rimuovere un volume inutilizzato:

```bash
docker volume rm my_volume
```



  - the **volumes**: with ```volume create``` we can create a volume, and with ```volume rm``` you can remove it.

Volumes are used to persist data generated by Docker containers. They are managed by Docker and are independent of the container's lifecycle. Volumes can be shared between containers.

**Example:**

```yaml
version: '3'
services:
  app:
    image: my-app
    volumes:
      - app-data:/var/lib/app-data
volumes:
  app-data:
```


#### Named Volumes
Named Volumes are created and managed by Docker, allowing you to persist data across container restarts and removals.
Defined in the Docker-compose file or created by using command line with: `docker volume create`.\
Named volumes are not tied to a specific container instance and can be reused! **Perfects for scenarios where data persistence is required**.
```yml
services:
  mongodb:
    image: mongo:latest
    restart: always
    ports:
      - '27017:27017'
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: secret
      MONGO_INITDB_DATABASE: projects
    volumes:
      - mongo-data:/data/db
```
As we can see in the example, the volume is set with [...] **:** [...].\
The first part is the name of the volume (in out case `mongo-data`), the second one is the path in the container.

#### Anonymous Volumes
Anonymous volumes are temporary volumes created by Docker when a container starts. They are typically used for temporary data, and they don't have a specific name, just because they are temporary.\
These volumes are ephemeral and are removed when the container is removed.
```yml
services:
  web:
    build: ./web
    volumes:
      - /app/temp
    environment:
      - FLASK_ENV=development
  [...]
```
As we can see, we don't have the name, but only the path. It means that Docker Compose will create an anonymous volume without a specific name.























### Bind Mounts
Bind mounts allow you to mount a directory or file from the host machine into the container. Bind Mounts allow you to map a host file or directory to a container file or directory. This is particularly useful during development when you want changes made on the host to be immediately reflected in the container.\
In a nutshell: _we want to use the bind mounts when we need direct access to the host’s filesystem for development, testing, or sharing configuration files._
```yml
services:
  config:
  build: config-server-end
  mem_limit: 512m
  environment:
    - SPRING_PROFILES_ACTIVE=docker,native
    [...]
  volumes:
    - ./config-repo:/config-repo
```
This means that a directory from the host machine (`./config-repo`) is mounted into the container at `/config-repo`.

Bind mounts link a directory on your local machine to a container. Unlike volumes, bind mounts directly reflect changes made on the host filesystem.

**Example:**

```yaml
version: '3'
services:
  app:
    image: my-app
    volumes:
      - ./local-directory:/app
```





___________________________________

  - the **network**: as the same as volumes, we can create a new network with ```network create``` and remove it with ```network rm```

#### 1.2.3 Networks
Networks allow containers to communicate with each other securely. Docker Compose automatically creates a default network, but you can define custom networks.

**Example:**

```yaml
version: '3'
services:
  web:
    image: nginx
    networks:
      - frontend
  db:
    image: mysql
    networks:
      - backend
networks:
  frontend:
  backend:
```















- **Secrets**: Secrets are used to store sensitive information such as passwords, OAuth tokens, and ssh keys; they are designed to be tightly controlled and secure, ensuring that secrets are not exposed outside where they are intended to be used.
- **Networks**: networking is a passage through which all the isolated container communicate - there are mainly five network drivers in Docker (such as bridge, host, none, overlay, and macvlan); in Docker, networking is indeed a crucial component that allows isolated containers to communicate with each other.





















### Networks

### Cos'è una Network?

Una **Network** Docker è un'entità virtuale che permette ai container di comunicare tra loro in un ambiente isolato. Docker crea automaticamente una network chiamata `bridge` per ogni container, ma è possibile creare e configurare network personalizzate.

### Tipi di Networks

- **Bridge**: Network standard per la comunicazione tra container su un singolo host.
- **Host**: I container condividono la network dell'host, con accesso diretto alle sue interfacce di rete.
- **Overlay**: Usata per collegare container che risiedono su diversi host Docker.
- **None**: Nessuna network è configurata; utile per container completamente isolati.

### Creazione e Gestione delle Networks

#### Creare una Network

Per creare una network:

```bash
docker network create my_network
```

#### Collegare un Container a una Network

Puoi collegare un container a una network specifica usando l'opzione `--network`:

```bash
docker run -d --network my_network my_image
```

#### Ispezionare una Network

Per vedere i dettagli di una network:

```bash
docker network inspect my_network
```

#### Rimuovere una Network

Per rimuovere una network non utilizzata:

```bash
docker network rm my_network
```






### Secrets
Secrets are used to securely pass sensitive data (e.g., API keys, passwords) to Docker containers. They are encrypted and only accessible to containers that you specify.

**Example:**

```yaml
version: '3.1'
services:
  db:
    image: mysql
    environment:
      MYSQL_ROOT_PASSWORD_FILE: /run/secrets/mysql_root_password
    secrets:
      - mysql_root_password
secrets:
  mysql_root_password:
    file: ./mysql_root_password.txt
```


---




