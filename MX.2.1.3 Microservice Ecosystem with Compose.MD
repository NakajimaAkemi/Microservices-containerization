# Creare un Ecosistema di Microservizi con Docker Compose e Python

Questo tutorial ti mostrerà come creare e gestire un ecosistema di microservizi utilizzando Docker Compose con microservizi implementati in Python. Presuppone che tu abbia già familiarità con i concetti di base di Docker, Docker Compose e Python.



## Struttura del Progetto

La struttura del progetto sarà organizzata in questo modo:

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
├── docker-compose.yml
└── .env
```

## Passo 1: Creare i Microservizi

### Microservizio 1: Flask (Service1)

Il primo microservizio sarà un semplice server HTTP che risponde con "Hello from Service 1".

#### File `service1/app.py`

```python
from flask import Flask

app = Flask(__name__)

@app.route('/')
def hello():
    return 'Hello from Service 1'

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8080)
```

#### File `service1/requirements.txt`

```plaintext
flask
```

#### File `service1/Dockerfile`

```dockerfile
# Usa un'immagine di base di Python
FROM python:3.9

# Imposta la directory di lavoro nel container
WORKDIR /usr/src/app

# Copia il file dei requisiti
COPY requirements.txt ./

# Installa le dipendenze del progetto
RUN pip install --no-cache-dir -r requirements.txt

# Copia il resto del codice sorgente
COPY . .

# Esponi la porta su cui il servizio gira
EXPOSE 8080

# Comando per avviare il servizio
CMD ["python", "app.py"]
```

### Microservizio 2: Flask (Service2)

Il secondo microservizio sarà simile al primo ma risponderà con "Hello from Service 2".

#### File `service2/app.py`

```python
from flask import Flask

app = Flask(__name__)

@app.route('/')
def hello():
    return 'Hello from Service 2'

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8080)
```

#### File `service2/requirements.txt`

```plaintext
flask
```

#### File `service2/Dockerfile`

```dockerfile
# Usa un'immagine di base di Python
FROM python:3.9

# Imposta la directory di lavoro nel container
WORKDIR /usr/src/app

# Copia il file dei requisiti
COPY requirements.txt ./

# Installa le dipendenze del progetto
RUN pip install --no-cache-dir -r requirements.txt

# Copia il resto del codice sorgente
COPY . .

# Esponi la porta su cui il servizio gira
EXPOSE 8080

# Comando per avviare il servizio
CMD ["python", "app.py"]
```

### Microservizio 3: Flask (Service3)

Il terzo microservizio sarà simile ai primi due ma risponderà con "Hello from Service 3".

#### File `service3/app.py`

```python
from flask import Flask

app = Flask(__name__)

@app.route('/')
def hello():
    return 'Hello from Service 3'

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8080)
```

#### File `service3/requirements.txt`

```plaintext
flask
```

#### File `service3/Dockerfile`

```dockerfile
# Usa un'immagine di base di Python
FROM python:3.9

# Imposta la directory di lavoro nel container
WORKDIR /usr/src/app

# Copia il file dei requisiti
COPY requirements.txt ./

# Installa le dipendenze del progetto
RUN pip install --no-cache-dir -r requirements.txt

# Copia il resto del codice sorgente
COPY . .

# Esponi la porta su cui il servizio gira
EXPOSE 8080

# Comando per avviare il servizio
CMD ["python", "app.py"]
```

## Passo 2: Creare il File `docker-compose.yml`

Il file `docker-compose.yml` definirà come i vari container interagiranno tra loro.

```yaml
version: '3.8'

services:
  service1:
    build: ./service1
    ports:
      - "8081:8080"
    environment:
      - FLASK_ENV=production

  service2:
    build: ./service2
    ports:
      - "8082:8080"
    environment:
      - FLASK_ENV=production
    depends_on:
      - service1

  service3:
    build: ./service3
    ports:
      - "8083:8080"
    environment:
      - FLASK_ENV=production
    depends_on:
      - service1
      - service2
```

### Spiegazione dei Parametri

- **`build`**: Specifica la directory in cui si trova il `Dockerfile` per ogni servizio.
- **`ports`**: Mappa la porta del container a una porta del tuo host.
- **`environment`**: Definisce le variabili d'ambiente.
- **`depends_on`**: Definisce le dipendenze tra i servizi, assicurando che i servizi necessari siano avviati prima di altri.

## Passo 3: Creare il File `.env` (Opzionale)

Il file `.env` può essere usato per definire variabili d'ambiente che verranno caricate automaticamente da Docker Compose. Esempio:

```env
FLASK_ENV=production
```

Queste variabili possono essere utilizzate all'interno del file `docker-compose.yml` come segue:

```yaml
environment:
  - FLASK_ENV=${FLASK_ENV}
```

## Passo 4: Costruire e Avviare i Servizi

Con il file `docker-compose.yml` pronto, puoi costruire e avviare tutti i servizi con un singolo comando:

```bash
docker-compose up --build
```

- **`--build`**: Ricostruisce le immagini del Dockerfile prima di avviare i container.

### Verifica dei Servizi

Puoi verificare che i servizi siano attivi accedendo alle seguenti URL nel tuo browser:

- **Service 1**: [http://localhost:8081](http://localhost:8081)
- **Service 2**: [http://localhost:8082](http://localhost:8082)
- **Service 3**: [http://localhost:8083](http://localhost:8083)

Ogni servizio dovrebbe rispondere con il proprio messaggio distintivo.

## Passo 5: Gestire i Servizi

### Fermare i Servizi

Per fermare tutti i servizi, esegui:

```bash
docker-compose down
```

### Visualizzare i Log

Puoi visualizzare i log di tutti i container con:

```bash
docker-compose logs -f
```

### Scalare i Servizi

Per scalare un servizio specifico (ad esempio, avere 3 istanze di `service1`):

```bash
docker-compose up --scale service1=3 -d
```

- **`-d`**: Esegue i container in background.

## Passo 6: Versionamento con Git

Per versionare il tuo ecosistema di microservizi, puoi creare un repository Git:

```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin <your-repository-url>
git push -u origin main
```

## Conclusione

Ora hai un ecosistema di microservizi completamente funzionante, tutto gestito con Docker Compose e sviluppato interamente in Python. Ogni servizio è isolato e indipendente, permettendoti di gestire facilmente il tuo ambiente di sviluppo e produzione.

