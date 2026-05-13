package com.devflow.devflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devflow.devflow.dto.ProjectDTO;
import com.devflow.devflow.model.Issue;
import com.devflow.devflow.model.Project;
import com.devflow.devflow.repository.ProjectRepository;


@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public ProjectDTO fetchProjectById(Long id) {
        Project p = projectRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        return new ProjectDTO(
            p.getId(),
            p.getProject_name(),
            p.getDescription(),
            p.getStatus(),
            p.getPriority().name(),
            p.getIssue() != null 
    ? p.getIssue().stream().map(Issue::getId).toList() 
    : List.of()
        );
    }
    @Override 
    public ProjectDTO saveProject(Project project){
        Project saved = projectRepository.save(project);
        return new ProjectDTO(
            saved.getId(),
            saved.getProject_name(),
            saved.getDescription(),
            saved.getStatus(),
            saved.getPriority().name(),
            saved.getIssue() != null 
    ? saved.getIssue().stream().map(Issue::getId).toList() 
    : List.of()
        );
    }

}
