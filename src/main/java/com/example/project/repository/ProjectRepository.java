package com.example.project.repository;

import com.example.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByNameContainingOrTaskContainingOrExecutorNameContaining(String name, String task, String executorName);
}
