package com.devflow.devflow.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devflow.devflow.model.Issue;
import com.devflow.devflow.repository.IssueRepository;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Override
    public Issue saveIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    @Override
    public List<Issue> fetchAllIssues() {
        return (List<Issue>)issueRepository.findAll();
    }

    @Override
    public Issue updateIssue(Issue issue, Long id) {
        Issue issuedb = issueRepository.findById(id).get();
        if(Objects.nonNull(issue.getTitle()) && !"".equalsIgnoreCase(issue.getTitle())) {
            issuedb.setTitle(issue.getTitle());
        }
        return issueRepository.save(issuedb);
       
    }

    @Override
    public void deleteIssue(Long id) {
        issueRepository.deleteById(id);
    }

    @Override
    public Issue fetchIssueById(Long id) {
        return issueRepository.findById(id).get();
    }

}
