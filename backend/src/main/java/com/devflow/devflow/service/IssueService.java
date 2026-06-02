package com.devflow.devflow.service;

import java.util.List;

import com.devflow.devflow.dto.IssueDTO;
import com.devflow.devflow.model.Issue;

public interface IssueService {
    IssueDTO saveIssue(Issue issue, Long projectId, String username);
    List<IssueDTO> fetchAllIssues();
    IssueDTO updateIssue(Issue issue, Long id);
    void deleteIssue(Long id);
    IssueDTO fetchIssueById(Long id);
    // This method is not exposed via controller, used internally for fetching Issue entity when needed
    Issue fetchIssueEntityById(Long id);
    List<IssueDTO> fetchAllIssuesByProjectId(Long projectId);
    IssueDTO updateIssueStatus(Long id, String status);
    IssueDTO assignIssue(Long issueId, Long userId);

}
