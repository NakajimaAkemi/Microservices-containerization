package com.baeldung.ls.controller.impl;

import com.baeldung.ls.controller.IProjectController;
import com.baeldung.ls.persistence.model.BeanB;
import com.baeldung.ls.persistence.model.Project;
import com.baeldung.ls.service.IProjectService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
public class ProjectControllerImpl implements IProjectController {
    IProjectService projectService;
    private static Logger log = LoggerFactory.getLogger(BeanB.class);


    //@Autowired
    public ProjectControllerImpl(IProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public Project findOne(Long id) {
        return projectService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @Override
    public Collection<Project> findAll() {
        return projectService.findAll();
    }

    @Override
    public Project create(Project project) {
        return projectService.save(project);
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