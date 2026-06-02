package com.devflow.devflow.service;

import java.util.List;

import com.devflow.devflow.model.Comment;

public interface CommentService {
    Comment saveComment(Long issueId, Comment comment);
    List<Comment> getComments(Long issueId);

}
