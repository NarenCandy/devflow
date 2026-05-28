package com.devflow.devflow.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devflow.devflow.dto.ProjectDTO;
import com.devflow.devflow.model.Issue;
import com.devflow.devflow.model.Project;
import com.devflow.devflow.model.User;
import com.devflow.devflow.repository.ProjectRepository;
import com.devflow.devflow.repository.UserRepository;


@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

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
    : List.of(),
    p.getCreatedBy() != null ? p.getCreatedBy().getName() : null
        );
    }
    @Override 
    public ProjectDTO saveProject(Project project, String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found with email: " + username));
        project.setCreatedBy(user);
        Project saved = projectRepository.save(project);
        return new ProjectDTO(
    saved.getId(),
    saved.getProject_name(),
    saved.getDescription(),
    saved.getStatus(),
    saved.getPriority().name(),
    saved.getIssue() != null ? saved.getIssue().stream().map(Issue::getId).toList() : List.of(),
    saved.getCreatedBy() != null ? saved.getCreatedBy().getName() : null
);
    }

    @Override
    public ProjectDTO updateProject(Project project, Long id) {
        Project existingProject = projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        if(Objects.nonNull(project.getProject_name())) {
            existingProject.setProject_name(project.getProject_name());
        }
        if(Objects.nonNull(project.getDescription())) {
            existingProject.setDescription(project.getDescription());
        }
        if(Objects.nonNull(project.getStatus())) {
            existingProject.setStatus(project.getStatus());
        }
        if(Objects.nonNull(project.getPriority())) {
            existingProject.setPriority(project.getPriority());
        }
        Project updated = projectRepository.save(existingProject);
        return new ProjectDTO(
            updated.getId(),
            updated.getProject_name(),
            updated.getDescription(),
            updated.getStatus(),
            updated.getPriority().name(),
            updated.getIssue() != null 
    ? updated.getIssue().stream().map(Issue::getId).toList() 
    : List.of(),
    updated.getCreatedBy() != null ? updated.getCreatedBy().getName() : null
        );

    }

    @Override
    public void deleteProject(Long id) {
        if(!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectDTO> fetchAllProjectsbyUser(String username) {
        
        return projectRepository.findByCreatedByEmail(username).stream().map(p -> new ProjectDTO(
            p.getId(),
            p.getProject_name(),
            p.getDescription(),
            p.getStatus(),
            p.getPriority().name(),
            p.getIssue() != null
    ? p.getIssue().stream().map(Issue::getId).toList()
    : List.of(),
    p.getCreatedBy() != null ? p.getCreatedBy().getName() : null
        )).toList();
    }

}
