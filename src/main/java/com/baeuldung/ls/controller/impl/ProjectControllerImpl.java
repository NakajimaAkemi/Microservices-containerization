package com.baeuldung.ls.controller.impl;
import com.baeuldung.ls.controller.IProjectController;
import com.baeuldung.ls.persistence.model.Project;
import com.baeuldung.ls.service.IProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
public class ProjectControllerImpl implements IProjectController {
    IProjectService projectService;

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
}