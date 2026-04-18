package com.devflow.devflow.service;

import java.util.List;

import com.devflow.devflow.model.Issue;

public interface IssueService {
    Issue saveIssue(Issue issue);
    List<Issue> fetchAllIssues();
    Issue updateIssue(Issue issue, Long id);
    void deleteIssue(Long id);
    Issue fetchIssueById(Long id);

}
