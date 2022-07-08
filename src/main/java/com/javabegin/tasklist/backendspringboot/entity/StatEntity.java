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
@Table(name = "stat", schema = "public", catalog = "taskList")
@Schema(description = "Stat")
public class StatEntity {
    @Schema(description = "Identify of stat", example = "1")
    private Integer id;
    @Schema(description = "Count of completed tasks", example = "1")
    private Integer completedTotal;
    @Schema(description = "Count of uncompleted tasks", example = "1")
    private Integer uncompletedTotal;
    @Schema(description = "Identifier of the user to whom the statistic belongs", example = "1")
    private Integer userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    @Basic
    @Column(name = "completed_total")
    public Integer getCompletedTotal() {
        return completedTotal;
    }

    @Basic
    @Column(name = "uncompleted_total")
    public Integer getUncompletedTotal() {
        return uncompletedTotal;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId(){
        return  userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StatEntity that = (StatEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
