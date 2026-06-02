package com.devflow.devflow.dto;

import lombok.Data;

@Data
public class IssueDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String postedBy;
    private Long projectId;   // instead of full Project object
    private String assignedTo; // instead of full User object

    public IssueDTO(Long id, String title, String description, String status, String postedBy, Long projectId, String assignedTo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.postedBy = postedBy;
        this.projectId = projectId;
        this.assignedTo = assignedTo;
    }

    // getters (or use Lombok @Data if you prefer)
}
