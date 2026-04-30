package com.devflow.devflow.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private String projectName;
    private String description;
    private String status;
    private String priority;
    private List<Long> issueIds;   // only IDs, not full Issue objects

    public ProjectDTO(Long id, String projectName, String description, String status, String priority, List<Long> issueIds) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.issueIds = issueIds;
    }

    // getters
}
