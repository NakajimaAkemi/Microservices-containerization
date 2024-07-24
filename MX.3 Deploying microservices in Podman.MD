# Podman Overview
Podman is open-source container management tool developed by Red Hat, designed to deploy and manage containerized applications. Podman can be obtained by installing Podman Desktop.

## Docker compatability
Thanks to the OCI format, Podman is compatible with Docker, meaning that Podman can pull/push images to/from Docker's registries, also the switch of the workflow from Docker to Podman and viceversa easier.


## Podman's architecture
Unlike Docker, Podman does not need a daemon running in the background, every command in Podman is executed as a process separated from the user. Every container process is managed with the current user's permissions, reducing the necessity of higher clearance, also a daemon may result in a single point of failure.


--Differences between Podman and Docker--





--Advantages and used cases--


--Podman Desktop--

--Deployment in Podman--


-- daemonless Architecture --


-- Rootless containers -- 

-- CI/CD integration --

-- logging and monitoring -- 


-- secrets management --


-- buildah and skopeo -- 


<p align="center">
  <img src="images/dockerAndPodman.png" alt="Esempio di immagine" />
</p>

_(while searching for images for docker I found this - in case it can be useful)_





## Sources
- https://www.redhat.com/en/topics/containers/what-is-podman
