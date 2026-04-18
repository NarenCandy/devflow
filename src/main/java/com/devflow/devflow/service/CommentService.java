package com.devflow.devflow.service;

import com.devflow.devflow.model.Comment;

public interface CommentService {
    Comment saveComment(Long issueId, Comment comment);

}
