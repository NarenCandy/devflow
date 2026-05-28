package com.devflow.devflow.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devflow.devflow.dto.IssueDTO;
import com.devflow.devflow.model.Issue;
import com.devflow.devflow.model.Project;
import com.devflow.devflow.model.User;
import com.devflow.devflow.repository.IssueRepository;
import com.devflow.devflow.repository.ProjectRepository;
import com.devflow.devflow.repository.UserRepository;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public IssueDTO saveIssue(Issue issue, Long projectId, String username) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        issue.setProject(project);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found with email: " + username));
        issue.setPostedBy(user);
        Issue saved = issueRepository.save(issue);
        return new IssueDTO(
            saved.getId(),
            saved.getTitle(),
            saved.getDescription(),
            saved.getStatus(),
            saved.getPostedBy() != null ? saved.getPostedBy().getName() : null,
            saved.getProject().getId(),
            saved.getAssignedTo() != null ? saved.getAssignedTo().getName() : null
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
                i.getPostedBy() != null ? i.getPostedBy().getName() : null,
                i.getProject().getId(),
                i.getAssignedTo() != null ? i.getAssignedTo().getName() : null
            ))
            .toList();
    }

    @Override
    public IssueDTO updateIssue(Issue issue, Long id) {
        Issue issuedb = issueRepository.findById(id).orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));
        if(Objects.nonNull(issue.getTitle()) && !"".equalsIgnoreCase(issue.getTitle())) {
            issuedb.setTitle(issue.getTitle());
        }
        if(Objects.nonNull(issue.getDescription()) && !"".equalsIgnoreCase(issue.getDescription())) {
            issuedb.setDescription(issue.getDescription());
        }
        if(Objects.nonNull(issue.getStatus()) && !"".equalsIgnoreCase(issue.getStatus())) {
            issuedb.setStatus(issue.getStatus());
        }
        Issue updated = issueRepository.save(issuedb);
        return new IssueDTO( updated.getId(),
            updated.getTitle(),
            updated.getDescription(),
            updated.getStatus(),
            updated.getPostedBy() != null ? updated.getPostedBy().getName() : null,
            updated.getProject().getId(),
            updated.getAssignedTo() != null ? updated.getAssignedTo().getName() : null
        );
       
    }

    @Override
    public void deleteIssue(Long id) {
        if(!issueRepository.existsById(id)) {
            throw new RuntimeException("Issue not found with id: " + id);
        }
        issueRepository.deleteById(id);
    }

    @Override
    public IssueDTO fetchIssueById(Long id) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));
        return new IssueDTO(
            issue.getId(),
            issue.getTitle(),
            issue.getDescription(),
            issue.getStatus(),
            issue.getPostedBy() != null ? issue.getPostedBy().getName() : null,
            issue.getProject().getId(),
            issue.getAssignedTo() != null ? issue.getAssignedTo().getName() : null
        );
    }

    //  This method is not exposed via controller, used internally for fetching Issue entity when needed
    @Override
    public Issue fetchIssueEntityById(Long id) {
        return issueRepository.findById(id).orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));
    }

    @Override
    public List<IssueDTO> fetchAllIssuesByProjectId(Long projectId) {
        return issueRepository.findByProjectId(projectId)
            .stream()
            .map(i -> new IssueDTO(
                i.getId(),
                i.getTitle(),
                i.getDescription(),
                i.getStatus(),
                i.getPostedBy() != null ? i.getPostedBy().getName() : null,
                i.getProject().getId(),
                i.getAssignedTo() != null ? i.getAssignedTo().getName() : null
            ))
            .toList();
}

    @Override
    public IssueDTO updateIssueStatus(Long id, String status){

        Issue issuedb =issueRepository.findById(id).orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));
        issuedb.setStatus(status);
        Issue updated = issueRepository.save(issuedb);
        return new IssueDTO( updated.getId(),
            updated.getTitle(),
            updated.getDescription(),
            updated.getStatus(),
            updated.getPostedBy() != null ? updated.getPostedBy().getName() : null,
            updated.getProject().getId(),
            updated.getAssignedTo() != null ? updated.getAssignedTo().getName() : null
        );
    }

    @Override
    public IssueDTO assignIssue(Long issueId, Long userId) {
        Issue issuedb =issueRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found with id: " + issueId));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        issuedb.setAssignedTo(user);
        Issue updated = issueRepository.save(issuedb);
        return new IssueDTO( updated.getId(),
            updated.getTitle(),
            updated.getDescription(),
            updated.getStatus(),
            updated.getPostedBy() != null ? updated.getPostedBy().getName() : null,
            updated.getProject().getId(),
            updated.getAssignedTo() != null ? updated.getAssignedTo().getName() : null
        );
        
    }
        

}
