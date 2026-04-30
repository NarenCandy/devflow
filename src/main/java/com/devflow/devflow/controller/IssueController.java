package com.devflow.devflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devflow.devflow.dto.IssueDTO;
import com.devflow.devflow.model.Issue;
import com.devflow.devflow.service.IssueService;

import jakarta.validation.Valid;




@RestController

public class IssueController {
    @Autowired
    private IssueService issueService;


    @PostMapping("/project/{projectId}/issue")
    public IssueDTO saveIssue(@Valid @RequestBody Issue issue , @PathVariable Long projectId) {
        
        
        return issueService.saveIssue( issue, projectId);
    }
    

    @GetMapping("/getissue")
    public List<IssueDTO> fetchissueList() {
        return issueService.fetchAllIssues();
    }
    
}
