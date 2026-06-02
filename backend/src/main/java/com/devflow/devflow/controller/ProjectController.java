package com.devflow.devflow.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devflow.devflow.dto.ProjectDTO;
import com.devflow.devflow.model.Project;
import com.devflow.devflow.service.ProjectService;

import jakarta.validation.Valid;


@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/project")
        public ProjectDTO saveProject(@RequestBody @Valid Project project, Principal principal) {
            return projectService.saveProject(project, principal.getName());
        }
    
    @GetMapping("/projects")
    public List<ProjectDTO> fetchAllProjectsbyUser(Principal principal) {
        return projectService.fetchAllProjectsbyUser(principal.getName());
    }

    @GetMapping("/project/{id}")
    public ProjectDTO fetchProjectById(@PathVariable Long id) {
        return projectService.fetchProjectById(id);
    }

    @PutMapping("/project/{id}")
    public ProjectDTO updateProject(@RequestBody @Valid Project project, @PathVariable Long id) {
        return projectService.updateProject(project, id);
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/project/{id}/members")
    public ResponseEntity<ProjectDTO> addMember(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(projectService.addMemberToProject(id, body.get("email")));
}

    @DeleteMapping("/project/{id}/members/{userId}")
    public ResponseEntity<ProjectDTO> removeMember(
        @PathVariable Long id,
        @PathVariable Long userId) {
        return ResponseEntity.ok(projectService.removeMember(id, userId));
}

}








