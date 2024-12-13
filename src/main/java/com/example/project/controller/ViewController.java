package com.example.project.controller;

import com.example.project.model.Executor;
import com.example.project.model.Project;
import com.example.project.service.ExecutorService;
import com.example.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Контроллер для отображения различных представлений, таких как домашняя страница, проекты, исполнители и т.д.
 */
@Controller
public class ViewController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ExecutorService executorService;

    /**
     * Отображает домашнюю страницу.
     *
     * @param model модель для передачи данных в представление
     * @return имя представления домашней страницы
     */
    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    /**
     * Отображает страницу с проектами. Поддерживает поиск и сортировку проектов.
     *
     * @param model модель для передачи данных в представление
     * @param query строка запроса для поиска проектов
     * @param sortBy критерий сортировки проектов
     * @return имя представления страницы с проектами
     */
    @GetMapping("/projects")
    public String viewProjects(Model model, @RequestParam(required = false) String query, @RequestParam(required = false) String sortBy) {
        List<Project> projects;
        if (query != null && !query.isEmpty()) {
            projects = projectService.searchProjects(query);
        } else if (sortBy != null && !sortBy.isEmpty()) {
            projects = projectService.getSortedProjects(sortBy);
        } else {
            projects = projectService.getAllProjects();
        }
        model.addAttribute("projects", projects);

        List<Executor> executors = executorService.getAllExecutors();
        model.addAttribute("executors", executors);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", authentication.getPrincipal());
        model.addAttribute("roles", authentication.getAuthorities());

        return "projects";
    }

    /**
     * Отображает страницу с исполнителями.
     *
     * @param model модель для передачи данных в представление
     * @return имя представления страницы с исполнителями
     */
    @GetMapping("/executors")
    public String viewExecutors(Model model) {
        List<Executor> executors = executorService.getAllExecutors();
        model.addAttribute("executors", executors);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", authentication.getPrincipal());
        model.addAttribute("roles", authentication.getAuthorities());
        return "executors";
    }

    /**
     * Отображает страницу с информацией об авторе.
     *
     * @return имя представления страницы с информацией об авторе
     */
    @GetMapping("/author")
    public String viewAuthor() {
        return "author";
    }

    /**
     * Отображает страницу с графиками.
     *
     * @param model модель для передачи данных в представление
     * @return имя представления страницы с графиками
     */
    @GetMapping("/chart")
    public String viewChart(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "chart";
    }
}
