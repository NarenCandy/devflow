package com.devflow.devflow.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Comment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User postedBy;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonBackReference
    private Issue issue;

}