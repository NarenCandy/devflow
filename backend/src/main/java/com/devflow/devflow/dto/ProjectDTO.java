package com.devflow.devflow.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String projectName;
    private String description;
    private String status;
    private String priority;
    private String createdBy;
    private List<Long> issueIds;   // only IDs, not full Issue objects
    private List<MemberDTO> members;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDTO {
        private Long id;
        private String name;
        private String email;

        
    }
    public ProjectDTO(Long id, String projectName, String description, String status, String priority, List<Long> issueIds, String createdBy) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.issueIds = issueIds;
        this.createdBy = createdBy;
    }

    // getters
}
