package com.devflow.devflow.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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

    
    // ✅ single toDTO method — used everywhere
    private ProjectDTO toDTO(Project p) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(p.getId());
        dto.setProjectName(p.getProject_name());
        dto.setDescription(p.getDescription());
        dto.setStatus(p.getStatus());
        dto.setPriority(p.getPriority().name());
        dto.setCreatedBy(p.getCreatedBy() != null ? p.getCreatedBy().getName() : null);
        dto.setIssueIds(p.getIssue() != null
                ? p.getIssue().stream().map(Issue::getId).toList()
                : List.of());
        dto.setMembers(p.getMembers() != null
                ? p.getMembers().stream().map(m -> {
                    ProjectDTO.MemberDTO md = new ProjectDTO.MemberDTO();
                    md.setId(m.getId());
                    md.setName(m.getName());
                    md.setEmail(m.getEmail());
                    return md;
                }).toList()
                : List.of());
        return dto;
    } 

    @Override
    public ProjectDTO fetchProjectById(Long id) {
        Project p = projectRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        return toDTO(p);
    }
    @Override 
    public ProjectDTO saveProject(Project project, String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found with email: " + username));
        project.setCreatedBy(user);
        return toDTO(projectRepository.save(project));
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
        return toDTO(projectRepository.save(existingProject));

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
        
        List<Project> owned = projectRepository.findByCreatedByEmail(username);
        List<Project> memberOf = projectRepository.findByMembersEmail(username);

        return Stream.concat(owned.stream(), memberOf.stream())
                .distinct() // avoid duplicates if owner is also in members list
                .map(this::toDTO)
                .toList();
    }


    @Override
    public ProjectDTO addMemberToProject(Long projectId, String email){
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " +email));
        boolean alreadyMember = project.getMembers().stream().anyMatch(m->m.getId().equals(user.getId()));
        if(!alreadyMember){
            project.getMembers().add(user);
            projectRepository.save(project);
        }
        return toDTO(project);
    }

    @Override
    public ProjectDTO removeMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.getMembers().removeIf(m -> m.getId().equals(userId));
        return toDTO(projectRepository.save(project));
    }




}
