package com.example.project.service;

import com.example.project.model.Executor;
import com.example.project.model.Project;
import com.example.project.repository.ExecutorRepository;
import com.example.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ExecutorRepository executorRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id).get();
    }

    public Project createProject(Project project) {
        Executor executor = executorRepository.findById(project.getExecutor().getId())
                .orElseThrow(() -> new RuntimeException("Executor not found"));
        project.setExecutor(executor);
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        Executor executor = executorRepository.findById(projectDetails.getExecutor().getId())
                .orElseThrow(() -> new RuntimeException("Executor not found"));
        project.setName(projectDetails.getName());
        project.setStartDate(projectDetails.getStartDate());
        project.setEndDate(projectDetails.getEndDate());
        project.setTask(projectDetails.getTask());
        project.setExecutor(executor);
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public List<Project> getSortedProjects(String sortBy) {
        return projectRepository.findAll(Sort.by(sortBy));
    }

    public List<Project> searchProjects(String query) {
        return projectRepository.findByNameContainingOrTaskContainingOrExecutorNameContaining(query, query, query);
    }
}
