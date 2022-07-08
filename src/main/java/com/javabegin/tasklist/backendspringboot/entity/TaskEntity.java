package com.javabegin.tasklist.backendspringboot.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Setter
@Table(name = "task", schema = "public", catalog = "taskList")
@Schema(description = "Task")
public class TaskEntity {
    @Schema(description = "Identify of task", example = "1")
    private Integer id;
    @Schema(description = "Summary of task", example = "Go for a walk")
    private String title;
    @Schema(description = "Task id done?", example = "false")
    private Boolean completed;
    @Schema(description = "Priority of task")
    private PriorityEntity priority;
    @Schema(description = "Category of task")
    private CategoryEntity category;
    @Schema(description = "Identifier of the user to whom the task belongs", example = "1")
    private Integer userId;

    public TaskEntity(String title, Boolean completed, CategoryEntity category, PriorityEntity priority, Integer userId) {
        this.title = title;
        this.completed = completed;
        this.category = category;
        this.priority = priority;
        this.userId =userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return title;
    }

    @Basic
    @Column(name = "completed", nullable = false)
    public Boolean getCompleted() {
        return completed;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public Integer getUserId(){
        return userId;
    }

    @ManyToOne
    @JoinColumn(name = "priority_id", referencedColumnName = "id")
    public PriorityEntity getPriority(){ return priority; }

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    public CategoryEntity getCategory(){ return category; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TaskEntity that = (TaskEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
