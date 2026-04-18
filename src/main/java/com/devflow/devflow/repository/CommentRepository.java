package com.devflow.devflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devflow.devflow.model.Comment;

@Repository

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
