package com.devflow.devflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devflow.devflow.model.Comment;
import com.devflow.devflow.model.Issue;
import com.devflow.devflow.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueService issueService;

    @Override
    public Comment saveComment(Long issueId, Comment comment) {
        Issue issue = issueService.fetchIssueById(issueId);
        comment.setIssue(issue);
    
        return commentRepository.save(comment);
    }

}
