package com.javabegin.tasklist.backendspringboot.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Setter
@Table(name = "priority", schema = "public", catalog = "taskList")
@Schema(description = "Priority")
public class PriorityEntity {
    @Schema(description = "Identifier of priority")
    private Integer id;
    @Schema(description = "Name of priority", example = "High")
    private String title;
    @Schema(description = "Color of priority", example = "#ffffff")
    private String color;
    @Schema(description = "Identifier of the user to whom the priority belongs")
    private Integer userId;

    public PriorityEntity(String title, String color, Integer userId) {
        this.title = title;
        this.color = color;
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 45)
    public String getTitle() {
        return title;
    }

    @Basic
    @Column(name = "color", length = 45)
    public String getColor() {
        return color;
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
        PriorityEntity that = (PriorityEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
