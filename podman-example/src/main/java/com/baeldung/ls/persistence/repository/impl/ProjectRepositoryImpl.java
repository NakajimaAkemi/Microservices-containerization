package com.baeldung.ls.persistence.repository.impl;

import com.baeldung.ls.persistence.model.BeanB;
import com.baeldung.ls.persistence.model.Project;
import com.baeldung.ls.persistence.repository.IProjectRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
//@Component
public class ProjectRepositoryImpl implements IProjectRepository {

    private final List<Project> projects = new ArrayList<>();
    private static Logger log = LoggerFactory.getLogger(BeanB.class);

    @Override
    public Optional<Project> findById(Long id) {
        return projects.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @Override
    public Collection<Project> findAll() {
        return projects;
    }

    @Override
    public Project save(Project project) {
        Project toSave = new Project(project);
        if (Objects.isNull(toSave.getId())) {
            toSave.setId(new Random().nextLong(1_000_000L));
        }
        Optional<Project> existingProject = findById(project.getId());
        if (existingProject.isPresent()) {
            projects.remove(existingProject);
        }
        projects.add(toSave);
        return toSave;
    }

    @PreDestroy
    public void preDestroy() {
        log.info("@PreDestroy method is called.");
    }
    @PostConstruct
    public void postConstruct() {
        log.info("@PostConstruct method is called.");
    }
}