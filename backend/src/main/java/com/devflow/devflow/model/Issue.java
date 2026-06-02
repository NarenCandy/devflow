package com.devflow.devflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
@Entity
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;
    private String description;
    @NotBlank(message = "Status cannot be empty")
    private String status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User postedBy;
    @OneToMany(mappedBy = "issue", cascade=CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference
    private Project project;
    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;


}
