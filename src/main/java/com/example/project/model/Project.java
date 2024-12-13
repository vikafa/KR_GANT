package com.example.project.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String task;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "executor_id", nullable = false)

    private Executor executor;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public int getProjectWidth() {
        long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate);
        return (int) Math.min(monthsBetween + 1, 12);
    }

    public int getProjectOffset() {
        return startDate.getMonthValue() - 1;
    }

    public int getProjectWidthPercentage() {
        return (getProjectWidth() * 100) / 12;
    }

    public int getProjectOffsetPercentage() {
        return (getProjectOffset() * 100) / 12;
    }
}


