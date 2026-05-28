package com.devflow.devflow.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devflow.devflow.dto.IssueDTO;
import com.devflow.devflow.dto.StatusUpdateRequestDTO;
import com.devflow.devflow.model.Issue;
import com.devflow.devflow.service.IssueService;

import jakarta.validation.Valid;






@RestController

public class IssueController {
    @Autowired
    private IssueService issueService;


    @PostMapping("/project/{projectId}/issue")
    public IssueDTO saveIssue(@Valid @RequestBody Issue issue , @PathVariable Long projectId, Principal principal) {
        
        
        return issueService.saveIssue( issue, projectId, principal.getName());
    }
    

    @GetMapping("/getissue")
    public List<IssueDTO> fetchissueList() {
        return issueService.fetchAllIssues();
    }

    @GetMapping("/project/{projectid}/issues")
    public List<IssueDTO> fetchAllIssues(@PathVariable Long projectid) {
        return issueService.fetchAllIssuesByProjectId(projectid);
    }
    @PatchMapping("/issue/{id}/status")
    public IssueDTO updateIssueStatus(@PathVariable Long id, @RequestBody StatusUpdateRequestDTO statusUpdateRequest) {
        return issueService.updateIssueStatus(id, statusUpdateRequest.getStatus());
    }

    @DeleteMapping("/issue/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        issueService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/issue/{id}")
    public IssueDTO updateIssue(@PathVariable Long id, @RequestBody @Valid Issue issue) {
       
        
        return issueService.updateIssue(issue, id);
    }

    @PatchMapping("/issue/{id}/assign/{userId}")
    public IssueDTO assignIssue(@PathVariable Long id, @PathVariable Long userId) {
        return issueService.assignIssue(id, userId);
    }

}
