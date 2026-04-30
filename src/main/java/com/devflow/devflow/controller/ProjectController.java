package com.devflow.devflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devflow.devflow.dto.ProjectDTO;
import com.devflow.devflow.model.Project;
import com.devflow.devflow.service.ProjectService;


@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/project")
        public ProjectDTO saveProject(@RequestBody Project project){
            return projectService.saveProject(project);
        }

    @GetMapping("/project/{id}")
    public ProjectDTO fetchProjectById(@PathVariable Long id) {
        return projectService.fetchProjectById(id);
    }

}








