package com.example.project.controller;

import com.example.project.model.Executor;
import com.example.project.service.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Контроллер для управления исполнителями через RESTful API.
 */
@RestController
@RequestMapping("/api/executors")
public class ExecutorController {

    @Autowired
    private ExecutorService executorService;

    /**
     * Получает список всех исполнителей.
     *
     * @return список всех исполнителей
     */
    @GetMapping
    public List<Executor> getAllExecutors() {
        return executorService.getAllExecutors();
    }

    /**
     * Получает исполнителя по его идентификатору.
     *
     * @param id идентификатор исполнителя
     * @return ResponseEntity с исполнителем, если найден, или статус 404 Not Found, если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<Executor> getExecutorById(@PathVariable Long id) {
        Optional<Executor> executor = executorService.getExecutorById(id);
        return executor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Создает нового исполнителя.
     *
     * @param executor данные нового исполнителя
     * @return ResponseEntity с созданным исполнителем
     */
    @PostMapping
    public ResponseEntity<Executor> createExecutor(@RequestBody Executor executor) {
        Executor createdExecutor = executorService.createExecutor(executor);
        return ResponseEntity.ok(createdExecutor);
    }

    /**
     * Обновляет данные исполнителя по его идентификатору.
     *
     * @param id идентификатор исполнителя
     * @param executor обновленные данные исполнителя
     * @return ResponseEntity с обновленным исполнителем
     */
    @PutMapping("/{id}")
    public ResponseEntity<Executor> updateExecutor(@PathVariable Long id, @RequestBody Executor executor) {
        Executor updatedExecutor = executorService.updateExecutor(id, executor);
        return ResponseEntity.ok(updatedExecutor);
    }

    /**
     * Удаляет исполнителя по его идентификатору.
     *
     * @param id идентификатор исполнителя
     * @return ResponseEntity с пустым телом и статусом 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExecutor(@PathVariable Long id) {
        executorService.deleteExecutor(id);
        return ResponseEntity.noContent().build();
    }
}
