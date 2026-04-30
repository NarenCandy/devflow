package com.devflow.devflow.service;

import com.devflow.devflow.dto.ProjectDTO;
import com.devflow.devflow.model.Project;


public interface ProjectService {
    ProjectDTO saveProject(Project project);

    ProjectDTO fetchProjectById(Long id);

}
