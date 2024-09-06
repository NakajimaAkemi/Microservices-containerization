
## 2.1 Simple Web Application with a Database

We'll build a simple web application using **Nginx** as the frontend and **MySQL** as the backend database. The application will use volumes to persist data, custom networks for communication, and secrets to securely manage credentials.

#### Step 1: Create `docker-compose.yml`

```yaml
version: '3'
services:
  web:
    image: nginx:latest
    ports:
      - "80:80"
    volumes:
      - ./web:/usr/share/nginx/html
    networks:
      - frontend
    depends_on:
      - db

  db:
    image: mysql:latest
    environment:
      MYSQL_ROOT_PASSWORD_FILE: /run/secrets/db_root_password
      MYSQL_DATABASE: webapp
      MYSQL_USER: user
      MYSQL_PASSWORD_FILE: /run/secrets/db_user_password
    volumes:
      - db-data:/var/lib/mysql
    secrets:
      - db_root_password
      - db_user_password
    networks:
      - backend

volumes:
  db-data:

networks:
  frontend:
  backend:

secrets:
  db_root_password:
    file: ./secrets/db_root_password.txt
  db_user_password:
    file: ./secrets/db_user_password.txt
```

#### Step 2: Create Necessary Files and Directories

1. Create the `secrets` folder:
    - Create two files in the `secrets` folder:
        - `db_root_password.txt`: contains the root password.
        - `db_user_password.txt`: contains the user password.

2. Create the `web` folder for static files:
    - Add an `index.html` file in the `web` folder to test the Nginx setup.

#### Step 3: Start the Application

Run the following command to launch the services:

```bash
docker-compose up
```

Visit `http://localhost` to see the Nginx homepage, and MySQL will be running in the background, storing data in a volume.

---

### 2.2 Scalable Python Flask Application with Replicas

In this example, we will create a **Flask** web application and scale it with replicas using Docker Compose.

#### Step 1: Create Flask Application

Create a directory named `flask-app` and add a simple Flask application in `app.py`:

```python
# flask-app/app.py
from flask import Flask
app = Flask(__name__)

@app.route('/')
def hello():
    return "Hello, World!"

if __name__ == "__main__":
    app.run(host='0.0.0.0')
```

#### Step 2: Create `Dockerfile`

```Dockerfile
# flask-app/Dockerfile
FROM python:3.8-slim
WORKDIR /app
COPY . /app
RUN pip install Flask
CMD ["python", "app.py"]
```

#### Step 3: Create `docker-compose.yml`

```yaml
version: '3'
services:
  web:
    build: ./flask-app
    ports:
      - "5000:5000"
    deploy:
      replicas: 3
    networks:
      - webnet

networks:
  webnet:
```

#### Step 4: Build and Run the Application

Build and run the application with:

```bash
docker-compose up --build
```

This configuration will run three replicas of the Flask application. Visit `http://localhost:5000` to see the application in action.

---

### 2.3 Docker Compose with a Multi-Service Microservice Architecture

For a more advanced example, let's build a microservice architecture using multiple services, including **Redis** for caching, **PostgreSQL** for data storage, and a **Node.js** app as the API layer.

#### Step 1: Create `docker-compose.yml`

```yaml
version: '3'
services:
  api:
    image: node:14
    volumes:
      - ./api:/usr/src/app
    working_dir: /usr/src/app
    command: npm start
    ports:
      - "3000:3000"
    depends_on:
      - redis
      - db
    networks:
      - backend
      - frontend

  redis:
    image: redis:alpine
    networks:
      - backend

  db:
    image: postgres:latest
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD_FILE: /run/secrets/db_password
      POSTGRES_DB: mydb
    volumes:
      - db-data:/var/lib/postgresql/data
    secrets:
      - db_password
    networks:
      - backend

volumes:
  db-data:

networks:
  backend:
  frontend:

secrets:
  db_password:
    file: ./secrets/db_password.txt
```

#### Step 2: Start the Application

```bash
docker-compose up --build
```

This configuration allows the Node.js app to interact with Redis and PostgreSQL using two isolated networks, and passwords are securely managed using secrets.

---




# Esempio Pratico di Creazione di un Ecosistema di Microservizi con Docker Compose

In questa seconda parte, vedremo come applicare i principi di Docker Compose alla creazione di un ecosistema di microservizi. L'esempio utilizzerà Python per implementare i microservizi e includerà servizi di supporto come Redis e MySQL.

## Struttura del Progetto

```plaintext
project-root/
│
├── service1/
│   ├── app.py
│   ├── requirements.txt
│   └── Dockerfile
│
├── service2/
│   ├── app.py
│   ├── requirements.txt
│   └── Dockerfile
│
├── service3/
│   ├── app.py
│   ├── requirements.txt
│   └── Dockerfile
│
├── redis/
│   └── Dockerfile
│
├── mysql/
│   └── Dockerfile
│
├── docker-compose.yml
└── .env
```

## Creazione dei Microservizi

### Microservizio 1: Flask (Service1)

Il primo microservizio sarà un semplice server Flask che restituisce un messaggio.

#### File `service1/app.py`

```python
from flask import Flask
import redis

app = Flask(__name__)
cache = redis.Redis(host='redis', port=6379)

@app.route('/')
def hello():
    visits = cache.incr('visits')
    return f'Hello from Service 1! Visits: {visits}'

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8080)
```

#### File `service1/requirements.txt`

```plaintext
flask
redis
```

#### File `service1/Dockerfile`

```dockerfile
FROM python:3.9

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8080

CMD ["python", "app.py"]
```

### Microservizio 2: Flask (Service2)

Il secondo microservizio sarà simile al primo, ma interagirà con un database MySQL.

#### File `service2/app.py`

```python
from flask import Flask, jsonify
import mysql.connector

app = Flask(__name__)

def get_db_connection():
    return mysql.connector.connect(
        host='mysql',
        user='root',
        password='password',
        database='test_db'
    )

@app.route('/')
def hello():
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute('SELECT COUNT(*) FROM test_table')
    count = cursor.fetchone()[0]
    cursor.close()
    conn.close()
    return jsonify(message='Hello from Service 2!', count=count)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8080)
```

#### File `service2/requirements.txt`

```plaintext
flask
mysql-connector-python
```

#### File `service2/Dockerfile`

```dockerfile
FROM python:3.9

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8080

CMD ["python", "app.py"]
```

### Microservizio 3: Flask (Service3)

Il terzo microservizio sarà simile ai primi due, ma avrà funzionalità aggiuntive.

#### File `service3/app.py`

```python
from flask import Flask

app = Flask(__name__)

@app.route('/')
def hello():
    return 'Hello from Service 3!'

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8080)
```

#### File `service3/requirements.txt`

```plaintext
flask
```

#### File `service3/Dockerfile`

```dockerfile
FROM python:3.9

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8080

CMD ["python", "app.py"]
```

## Aggiunta di Servizi Redis e MySQL

### Redis

Redis verrà utilizzato come servizio di caching per il microservizio 1.

#### File `redis/Dockerfile`

```dockerfile
FROM redis:alpine
```

### MySQL

MySQL verrà utilizzato come database per il microservizio 2.

#### File `mysql/Dockerfile`

```dockerfile
FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=password
ENV MYSQL_DATABASE=test_db

EXPOSE 3306
```

## Creazione del File `docker-compose.yml`

Il file `docker-compose.yml` orchestrerà i microservizi insieme a Redis e MySQL.

```yaml
version: '3.8'

services:
  service1:
    build: ./service1
    ports:
      - "8081:8080"
    environment:
      -

 FLASK_ENV=production
    depends_on:
      - redis

  service2:
    build: ./service2
    ports:
      - "8082:8080"
    environment:
      - FLASK_ENV=production
    depends_on:
      - mysql

  service3:
    build: ./service3
    ports:
      - "8083:8080"
    environment:
      - FLASK_ENV=production
    depends_on:
      - service1
      - service2

  redis:
    image: redis:alpine
    ports:
      - "6379:6379"

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: test_db
    ports:
      - "3306:3306"
```

## Avvio dell'Ecosistema

Per avviare l'intero ecosistema, esegui:

```bash
docker-compose up --build
```

### Verifica dei Servizi

Puoi verificare che i servizi siano attivi accedendo alle seguenti URL nel tuo browser:

- **Service 1**: [http://localhost:8081](http://localhost:8081)
- **Service 2**: [http://localhost:8082](http://localhost:8082)
- **Service 3**: [http://localhost:8083](http://localhost:8083)

### Gestione dei Servizi

- **Fermare i servizi**: `docker-compose down`
- **Visualizzare i log**: `docker-compose logs -f`
- **Scalare i servizi**: `docker-compose up --scale service1=3 -d`
