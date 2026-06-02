package com.devflow.devflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devflow.devflow.model.Comment;
import com.devflow.devflow.model.Issue;
import com.devflow.devflow.repository.CommentRepository;
import com.devflow.devflow.repository.UserRepository;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueService issueService;
    @Autowired
    private UserRepository userRepository; 

    @Override
    public Comment saveComment(Long issueId, Comment comment) {
        Issue issue = issueService.fetchIssueEntityById(issueId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        comment.setIssue(issue);
        comment.setPostedBy(currentUser);
    
        return commentRepository.save(comment);
    }
    @Override
    public List<Comment> getComments(Long issueId) {
        Issue issue = issueService.fetchIssueEntityById(issueId);
        return commentRepository.findByIssue(issue);
}

}
