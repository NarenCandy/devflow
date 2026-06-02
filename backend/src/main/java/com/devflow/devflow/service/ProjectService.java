package com.devflow.devflow.service;

import java.util.List;

import com.devflow.devflow.dto.ProjectDTO;
import com.devflow.devflow.model.Project;


public interface ProjectService {
    ProjectDTO saveProject(Project project, String username);

    ProjectDTO fetchProjectById(Long id);

    ProjectDTO updateProject(Project project, Long id);
    void  deleteProject(Long id);
    List<ProjectDTO> fetchAllProjectsbyUser(String username);
    ProjectDTO addMemberToProject(Long projectId, String email);
    ProjectDTO removeMember(Long projectId, Long userId);


}
