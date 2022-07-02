package com.javabegin.tasklist.backendspringboot.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Setter
@Table(name = "category", schema = "public", catalog = "taskList")
public class CategoryEntity {
    private Integer id;
    private String title;
    private Integer completedCount;
    private Integer uncompletedCount;
    private Integer userId;

    public CategoryEntity(String title, Integer userId) {
        this.title = title;
        this.completedCount = 0;
        this.uncompletedCount = 0;
        this.userId = userId;
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
    @Column(name = "completed_count")
    public Integer getCompletedCount() {
        return completedCount;
    }

    @Basic
    @Column(name = "uncompleted_count")
    public Integer getUncompletedCount() {
        return uncompletedCount;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId(){
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CategoryEntity that = (CategoryEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
