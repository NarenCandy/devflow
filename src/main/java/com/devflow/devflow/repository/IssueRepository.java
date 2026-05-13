package com.devflow.devflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devflow.devflow.model.Issue;

@Repository

public interface IssueRepository  extends JpaRepository<Issue, Long> {
    List<Issue> findByProjectId(Long projectId); 

}
