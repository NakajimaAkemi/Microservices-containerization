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
    - **depends_on: `<service>`**: Define a dependency between services, the specified service will start up first.
    - **enviroment**: Are used to pass enviroment variables in order to configure containers and applications within them.

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
We will create an [updated version](https://github.com/NakajimaAkemi/Microservices-containerization/tree/master/workdir/replica-compose) of our first example by adding `deploy` field to the compose file and defining the number of replicas.

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
Docker volumes are a mechanism for Docker containers that allow us to mantain to maintain persistent data survining beyond the container lifecycle, facilitating sharing data between containers, backup and restore. There are different types of volumes: named, anonymous and bind. In this paragraph we want to analyze the characteristics of each type providing some examples.

_Is also important to say that if a service doesn't specify a volumes section, it means that the service won't have any volumes mounted. Essentially, **no data will be persisted outside the container** for that service._

#### Volume creation
First things first, we need to create a Volume trough our docker CLI :
```bash
docker volume create my_volume
```
#### Volume removal
```bash
docker volume rm my_volume
```
#### Inspecting a Volume
We can inspect a volume if we want to
```bash
docker volume inspect my_volume
```
#### Including the volume in the compose file
As we created our volume we can mount it to our container image.
```yml
version: '3'
services:
  flask:
    build: ./app
    ports:
      - "5000:5000"
    volumes:
      - flask_data:/app/data

volumes:
  flask_data:
```
As we can see in the example, the volume is set with [...] **:** [...].\
The first part is the name of the volume (in out case `flask_data`), the second one is the path in the container.

#### Anonymous Volumes
Anonymous volumes are temporary volumes created by Docker when a container starts. They are typically used for temporary data, and they don't have a specific name, just because they are temporary.\
These volumes are ephemeral and are removed when the container is removed.
```yml
services:
  web:
    build: ./app
    volumes:
      - /app/temp
    environment:
```
As we can see, we don't have the name, but only the path. It means that Docker Compose will create an anonymous volume without a specific name.




#### Bind Mounts

 Easy to backup and restore. Volumes are more performant in linux than bind volumes


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

### Secrets
Secrets are any piece of sensitive data information(e.g., API keys, passwords), that shouldn't be transmitted over the network or stored unencrypted. It is an alternative to enviroment variables as they are open to the risk of exposure due to being accessible to all processes or even printed in log files (a simple `docker inspect` can expose the sensitive data). 

They are designed to be tightly controlled, secure and only accessible (read-only) to services only when expicitly granted access within the `secrets` field. 

This time we will set up a simple ecosystem of Flask server backed by a PostgreSQL, starting with the setup of the secret for the database password.
```bash
echo "mypostgrespassword" | docker secret create db_password -
```
Then add the secret to the compose file.
As and example we will 
```yaml
version: '3.9'
services:
  db:
    image: postgres:13
    environment:
      POSTGRES_USER: postgres
      POSTGRES_DB: mydatabase
    secrets:
      - db_password  # Il database userà il secret per la password
    environment:
      POSTGRES_PASSWORD_FILE: /run/secrets/db_password  # Usa il secret montato come file per la password

  flask:
    build: ./app
    ports:
      - "5000:5000"
    depends_on:
      - db
    secrets:
      - db_password
    environment:
      - DATABASE_URL=postgresql://postgres@db/mydatabase
      - DB_PASSWORD_FILE=/run/secrets/db_password  # Flask leggerà la password da questo file

secrets:
  db_password:
    external: true  # Secret creato precedentemente

```

This one is a really stupid example:
```Python
from flask import Flask
import psycopg2
import os

app = Flask(__name__)

def get_db_password():
    # Legge la password del DB dal file secret
    secret_path = '/run/secrets/db_password'
    if os.path.exists(secret_path):
        with open(secret_path, 'r') as secret_file:
            return secret_file.read().strip()
    return None

@app.route('/')
def index():
    db_password = get_db_password()
    if db_password:
        try:
            # Connessione a PostgreSQL usando la password dal secret
            conn = psycopg2.connect(
                dbname="mydatabase",
                user="postgres",
                password=db_password,
                host="db"
            )
            return "Connected to the database!"
        except Exception as e:
            return f"Error connecting to the database: {str(e)}"
    else:
        return "No database password found!"

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)

```


### Networks

___________________________________

  - the **network**: as the same as volumes, we can create a new network with ```network create``` and remove it with ```network rm```

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














