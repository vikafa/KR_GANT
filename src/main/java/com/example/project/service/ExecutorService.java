package com.example.project.service;

import com.example.project.model.Executor;
import com.example.project.repository.ExecutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExecutorService {

    @Autowired
    private ExecutorRepository executorRepository;

    public List<Executor> getAllExecutors() {
        return executorRepository.findAll();
    }

    public Optional<Executor> getExecutorById(Long id) {
        return executorRepository.findById(id);
    }

    public Executor createExecutor(Executor executor) {
        return executorRepository.save(executor);
    }

    public Executor updateExecutor(Long id, Executor executorDetails) {
        Executor executor = executorRepository.findById(id).orElseThrow(() -> new RuntimeException("Executor not found"));
        executor.setName(executorDetails.getName());
        executor.setPhone(executorDetails.getPhone());
        executor.setServiceCost(executorDetails.getServiceCost());
        executor.setServices(executorDetails.getServices());
        return executorRepository.save(executor);
    }

    public void deleteExecutor(Long id) {
        Executor executor = executorRepository.findById(id).orElseThrow(() -> new RuntimeException("Executor not found"));
        executor.getProjects().clear(); // Удаляем все связи с проектами
        executorRepository.delete(executor);
    }
}


