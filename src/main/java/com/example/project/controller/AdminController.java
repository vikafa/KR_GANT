package com.example.project.controller;

import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Контроллер для управления административными функциями.
 */
@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * Отображает административную панель с пользователями и ролями.
     *
     * @param model модель для передачи данных в представление
     * @return имя представления административной панели
     */
    @GetMapping("/admin")
    public String viewAdminPanel(Model model) {
        List<User> users = userService.getAllUsers();
        List<Role> roles = userService.getAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "admin";
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return объект пользователя или null, если пользователь не найден
     */
    @GetMapping("/api/users/{id}")
    @ResponseBody
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id).orElse(null);
    }

    /**
     * Обновляет роли пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @param roles новые роли пользователя
     * @return обновленный объект пользователя
     */
    @PutMapping("/api/users/{id}/roles")
    @ResponseBody
    public User updateUserRoles(@PathVariable Long id, @RequestBody Set<Role> roles) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRoles(roles);
        userService.save(user);
        return user;
    }

    /**
     * Обновляет роль пользователя на "ROLE_ADMIN".
     *
     * @param id идентификатор пользователя
     * @return перенаправление на административную панель
     */
    @PostMapping("/admin/update_user/{id}")
    public String updateUserToAdmin(@PathVariable("id") Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userService.updateUserRole(user.getUsername(), "ROLE_ADMIN");
        } else {
            throw new RuntimeException("User not found");
        }
        return "redirect:/admin";
    }

    /**
     * Обновляет роль администратора на "ROLE_USER".
     *
     * @param id идентификатор администратора
     * @return перенаправление на административную панель
     */
    @PostMapping("/admin/update_admin/{id}")
    public String updateAdminToUser(@PathVariable("id") Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userService.updateUserRole(user.getUsername(), "ROLE_USER");
        } else {
            throw new RuntimeException("User not found");
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete_user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
