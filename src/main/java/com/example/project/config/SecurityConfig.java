package com.example.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.project.model.User;
import com.example.project.service.UserService;

import java.util.Optional;

/**
 * Конфигурационный класс для настройки безопасности приложения.
 */
@Configuration
public class SecurityConfig {
    private final UserService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Конструктор для инициализации конфигурации безопасности.
     *
     * @param userService сервис для работы с пользователями
     * @param jwtAuthenticationFilter фильтр для аутентификации JWT
     */
    @Autowired
    public SecurityConfig(@Lazy UserService userService, @Lazy JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Создает и возвращает экземпляр PasswordEncoder для кодирования паролей.
     *
     * @return экземпляр BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Создает и возвращает сервис для загрузки информации о пользователе.
     *
     * @return экземпляр UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> user = userService.findByUsername(username);
            if (user.isPresent()) {
                return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),
                        user.get().getRoles().stream()
                                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName()))
                                .toList());
            }
            throw new UsernameNotFoundException("User not found");
        };
    }

    /**
     * Создает и возвращает AuthenticationManager для управления аутентификацией.
     *
     * @param http объект HttpSecurity для настройки безопасности
     * @return экземпляр AuthenticationManager
     * @throws Exception если происходит ошибка при создании AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    /**
     * Создает и возвращает цепочку фильтров безопасности для настройки HTTP безопасности.
     *
     * @param http объект HttpSecurity для настройки безопасности
     * @return экземпляр SecurityFilterChain
     * @throws Exception если происходит ошибка при создании цепочки фильтров
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/register", "/login").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/author/**", "/chart/**", "/executors/**", "/projects/**").hasAnyRole("USER", "ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
