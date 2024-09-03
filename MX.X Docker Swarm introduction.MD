# Introduzione a Docker Swarm

Docker Swarm è la modalità nativa di orchestrazione dei container di Docker. Consente di creare, gestire e scalare facilmente applicazioni containerizzate su cluster di host Docker. Docker Swarm trasforma un gruppo di host Docker in un cluster altamente disponibile e gestito centralmente, chiamato "Swarm". In questo documento, esploreremo i concetti chiave, i vantaggi e le operazioni di base di Docker Swarm.

## Cos'è Docker Swarm?

Docker Swarm è una modalità che permette a Docker di agire come orchestratore, coordinando il funzionamento di container su più nodi (host Docker). In altre parole, con Docker Swarm puoi trasformare un gruppo di server in un singolo cluster su cui distribuire le tue applicazioni, gestendo il loro stato, la scalabilità e l'alta disponibilità.

## Vantaggi di Docker Swarm

- **Scalabilità**: Puoi facilmente scalare i servizi su più nodi, aumentando o diminuendo il numero di container (replicas) in esecuzione.
- **Alta Disponibilità**: Docker Swarm ridistribuisce automaticamente i container in caso di fallimento di un nodo, garantendo che i servizi rimangano attivi.
- **Gestione Centralizzata**: Tutti i nodi di un cluster Swarm sono gestiti centralmente, ma continuano a funzionare come host Docker indipendenti.
- **Sicurezza**: Swarm utilizza la crittografia TLS per proteggere la comunicazione tra i nodi e fornisce meccanismi di autenticazione per i nodi che si uniscono al cluster.
- **Bilanciamento del Carico**: Docker Swarm include un bilanciamento del carico interno che distribuisce il traffico tra i container del cluster.

## Concetti Chiave

### Nodi

- **Manager Node**: I nodi manager sono responsabili della gestione dello stato globale del cluster, incluso l'orchestrazione e il bilanciamento del carico. Gestiscono la configurazione e distribuiscono le informazioni agli altri nodi.
  
- **Worker Node**: I nodi worker eseguono le attività assegnate loro dai manager. Eseguono i container e riportano il loro stato ai nodi manager.

### Servizi e Repliche

- **Servizi**: Un servizio in Docker Swarm è un'astrazione di un'applicazione che gira su uno o più container. Puoi definire quanti container (replicas) devono eseguire il servizio.
  
- **Repliche**: Le repliche sono istanze di un servizio distribuite su uno o più nodi. Docker Swarm si assicura che il numero desiderato di repliche sia sempre in esecuzione.

### Overlay Network

Swarm utilizza reti overlay per permettere ai container in esecuzione su nodi diversi di comunicare tra loro come se fossero sulla stessa rete fisica. Le reti overlay consentono ai servizi di connettersi in modo sicuro e isolato, supportando scenari come microservizi distribuiti.

## Come Funziona Docker Swarm

### 1. Inizializzazione dello Swarm

Per iniziare, un host Docker deve essere configurato come nodo manager, inizializzando lo Swarm:

```bash
docker swarm init
```

Questo comando trasforma l'host Docker in un nodo manager e crea il cluster Swarm.

### 2. Aggiungere Nodi al Cluster

Dopo l'inizializzazione, altri nodi (host Docker) possono unirsi al cluster come worker o manager. Per ottenere il comando per aggiungere un nodo worker, puoi usare:

```bash
docker swarm join-token worker
```

I nodi worker possono quindi unirsi allo Swarm con il comando fornito.

### 3. Creare e Gestire Servizi

Una volta configurato il cluster, puoi creare un servizio con un numero specifico di repliche:

```bash
docker service create --replicas 3 --name my_service nginx
```

Docker Swarm si assicurerà che tre container Nginx siano sempre in esecuzione distribuiti tra i nodi.

### 4. Scalare un Servizio

Scalare un servizio per aumentare o diminuire il numero di repliche è semplice:

```bash
docker service scale my_service=5
```

Swarm bilancia automaticamente il carico tra le nuove repliche.

### 5. Monitorare e Gestire lo Swarm

Puoi monitorare lo stato del cluster e dei servizi con:

```bash
docker service ls
docker node ls
```

E puoi ispezionare specifici servizi o nodi per informazioni dettagliate:

```bash
docker service ps my_service
docker node inspect <node_name>
```

## Conclusione

Docker Swarm è uno strumento potente per la gestione e l'orchestrazione di container su larga scala. Fornisce una piattaforma robusta per costruire applicazioni altamente disponibili, scalabili e sicure. Con Docker Swarm, l'orchestrazione dei container diventa intuitiva, sfruttando la familiarità dell'ecosistema Docker e aggiungendo funzionalità avanzate per la gestione dei cluster.




















### Come Utilizzare le Replicas con Docker Compose

Quando usi Docker Compose in modalità Swarm, il file `docker-compose.yml` può includere la direttiva `deploy` che è specifica per le configurazioni di Swarm. All'interno della direttiva `deploy`, puoi specificare il numero di **replicas** desiderate.

### Esempio di `docker-compose.yml` con Replicas

Ecco un esempio di file `docker-compose.yml` che utilizza le **replicas** in modalità Swarm:

```yaml
version: '3.8'

services:
  web:
    image: nginx:latest
    ports:
      - "8080:80"
    deploy:
      replicas: 3
      resources:
        limits:
          cpus: "0.5"
          memory: "50M"
      restart_policy:
        condition: on-failure
    networks:
      - webnet

networks:
  webnet:
```

### Passi per Utilizzare le Replicas con Docker Compose

1. **Inizializza Docker Swarm (se non è già attivo):**

   Prima di poter utilizzare le **replicas**, devi attivare Docker Swarm:

   ```bash
   docker swarm init
   ```

2. **Esegui Docker Compose in Modalità Swarm:**

   Per utilizzare il file `docker-compose.yml` con Docker Swarm, usa il comando:

   ```bash
   docker stack deploy -c docker-compose.yml my_stack
   ```

   Qui `my_stack` è il nome dello stack, e Docker Swarm creerà i servizi specificati con il numero di **replicas** definito.

3. **Verifica le Repliche:**

   Puoi controllare lo stato delle repliche con:

   ```bash
   docker service ls
   ```

   Per vedere i dettagli di un singolo servizio:

   ```bash
   docker service ps my_stack_web
   ```

### Considerazioni

- **La direttiva `deploy` è specifica per Docker Swarm** e non funziona con il normale comando `docker-compose up` in modalità non-Swarm. Se provi a usare `deploy` con `docker-compose up`, verrà ignorato.
- **Swarm Mode è necessario** per utilizzare funzionalità avanzate come le replicas, l'auto-rilevamento dei servizi, il bilanciamento del carico interno e la tolleranza ai guasti.
- **`docker stack deploy`** è il comando da utilizzare in ambienti di produzione quando si desidera sfruttare appieno Docker Swarm e le funzionalità di orchestrazione come le replicas.

### Conclusione

Sì, le **replicas** possono essere usate con Docker Compose, ma richiedono l'uso di Docker Swarm. In modalità Swarm, Docker Compose diventa uno strumento potente per la gestione di applicazioni distribuite e scalabili, permettendo di orchestrare più istanze di un servizio facilmente.
