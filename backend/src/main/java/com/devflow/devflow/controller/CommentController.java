package com.devflow.devflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devflow.devflow.model.Comment;
import com.devflow.devflow.service.CommentService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/issue")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/{issue_id}/comment")
    public Comment addComment(@PathVariable @Valid Long issue_id,
        @RequestBody Comment comment) {
      
        
        return commentService.saveComment(issue_id, comment);
    }

    @GetMapping("/{issue_id}/comments")
    public List<Comment> getComments(@PathVariable Long issue_id) {
        return commentService.getComments(issue_id);
    }
    

}
