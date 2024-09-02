

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
