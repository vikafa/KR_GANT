package com.example.project.controller;

import com.example.project.model.Project;
import com.example.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления проектами через RESTful API.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * Получает список всех проектов.
     *
     * @return список всех проектов
     */
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    /**
     * Получает проект по его идентификатору.
     *
     * @param id идентификатор проекта
     * @return ResponseEntity с проектом, если найден, или статус 404 Not Found, если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProject(id);
        return project != null
                ? ResponseEntity.ok(project)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Создает новый проект.
     *
     * @param project данные нового проекта
     * @return ResponseEntity с созданным проектом
     */
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return ResponseEntity.ok(createdProject);
    }

    /**
     * Обновляет данные проекта по его идентификатору.
     *
     * @param id идентификатор проекта
     * @param projectDetails обновленные данные проекта
     * @return ResponseEntity с обновленным проектом
     */
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        Project updatedProject = projectService.updateProject(id, projectDetails);
        return ResponseEntity.ok(updatedProject);
    }

    /**
     * Удаляет проект по его идентификатору.
     *
     * @param id идентификатор проекта
     * @return ResponseEntity с пустым телом и статусом 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получает отсортированный список проектов по заданному критерию.
     *
     * @param sortBy критерий сортировки
     * @return отсортированный список проектов
     */
    @GetMapping("/sort")
    public List<Project> getSortedProjects(@RequestParam String sortBy) {
        return projectService.getSortedProjects(sortBy);
    }

    /**
     * Ищет проекты по заданному запросу.
     *
     * @param query строка запроса для поиска
     * @return список найденных проектов
     */
    @GetMapping("/search")
    public List<Project> searchProjects(@RequestParam String query) {
        return projectService.searchProjects(query);
    }
}
