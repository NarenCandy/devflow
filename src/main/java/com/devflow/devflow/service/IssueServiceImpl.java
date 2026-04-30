package com.devflow.devflow.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devflow.devflow.dto.IssueDTO;
import com.devflow.devflow.model.Issue;
import com.devflow.devflow.model.Project;
import com.devflow.devflow.repository.IssueRepository;
import com.devflow.devflow.repository.ProjectRepository;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public IssueDTO saveIssue(Issue issue, Long projectId) {
        Project project = projectRepository.findById(projectId).get();
        issue.setProject(project);
        Issue saved = issueRepository.save(issue);
        return new IssueDTO(
            saved.getId(),
            saved.getTitle(),
            saved.getDescription(),
            saved.getStatus(),
            saved.getPostedBy(),
            saved.getProject().getId()
        );
    }

    @Override
    public List<IssueDTO> fetchAllIssues() {
        return issueRepository.findAll()
            .stream()
            .map(i -> new IssueDTO(
                i.getId(),
                i.getTitle(),
                i.getDescription(),
                i.getStatus(),
                i.getPostedBy(),
                i.getProject().getId()
            ))
            .toList();
    }

    @Override
    public IssueDTO updateIssue(Issue issue, Long id) {
        Issue issuedb = issueRepository.findById(id).orElseThrow();
        if(Objects.nonNull(issue.getTitle()) && !"".equalsIgnoreCase(issue.getTitle())) {
            issuedb.setTitle(issue.getTitle());
        }
        Issue updated = issueRepository.save(issuedb);
        return new IssueDTO( updated.getId(),
            updated.getTitle(),
            updated.getDescription(),
            updated.getStatus(),
            updated.getPostedBy(),
            updated.getProject().getId());
       
    }

    @Override
    public void deleteIssue(Long id) {
        issueRepository.deleteById(id);
    }

    @Override
    public IssueDTO fetchIssueById(Long id) {
        Issue issue = issueRepository.findById(id).orElseThrow();
        return new IssueDTO(
            issue.getId(),
            issue.getTitle(),
            issue.getDescription(),
            issue.getStatus(),
            issue.getPostedBy(),
            issue.getProject().getId()
        );
    }

    //  This method is not exposed via controller, used internally for fetching Issue entity when needed
    @Override
    public Issue fetchIssueEntityById(Long id) {
        return issueRepository.findById(id).orElseThrow();
    }

}
