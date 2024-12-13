package com.example.project.model;

public class RegistrationRequest {
    private String username;
    private String password;

    // Конструктор по умолчанию
    public RegistrationRequest() {
    }

    // Конструктор с параметрами
    public RegistrationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Геттеры и сеттеры
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
