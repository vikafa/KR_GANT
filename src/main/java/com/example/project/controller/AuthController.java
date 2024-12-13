package com.example.project.controller;

import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.repository.RoleRepository;
import com.example.project.service.UserService;
import com.example.project.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

/**
 * Контроллер для обработки аутентификации и регистрации пользователей.
 */
@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Отображает страницу входа.
     *
     * @return имя представления страницы входа
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Обрабатывает вход пользователя.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @param model модель для передачи данных в представление
     * @return перенаправление на главную страницу с JWT токеном
     * @throws Exception если происходит ошибка при аутентификации
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);

        model.addAttribute("jwt", jwt);
        return "redirect:/";
    }

    /**
     * Отображает страницу регистрации.
     *
     * @return имя представления страницы регистрации
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * Обрабатывает регистрацию нового пользователя.
     *
     * @param firstName имя пользователя
     * @param lastName фамилия пользователя
     * @param position должность пользователя
     * @param username имя пользователя для входа
     * @param password пароль пользователя
     * @param model модель для передачи данных в представление
     * @return имя представления страницы регистрации с сообщением об успешной регистрации или ошибке
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam String firstName, @RequestParam String lastName,
                               @RequestParam String position, @RequestParam String username,
                               @RequestParam String password, Model model) {
        if (userService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username is already taken!");
            return "register";
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPosition(position);
        user.setUsername(username);
        user.setPassword(password);

        // Добавьте роль пользователя по умолчанию
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userService.save(user);
        model.addAttribute("message", "Вы успешно зарегистрированы!");
        return "register";
    }
}
