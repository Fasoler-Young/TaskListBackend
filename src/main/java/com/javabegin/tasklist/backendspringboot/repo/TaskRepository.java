package com.javabegin.tasklist.backendspringboot.repo;

import com.javabegin.tasklist.backendspringboot.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    @Query("select t from TaskEntity t where" +
            "(:title is null or :title='' or lower(t.title) like lower(concat( '%', :title, '%'))) and" +
            "(:completed is null or t.completed=:completed ) and" +
            "(:priorityId is null or t.priority.id=:priorityId) and" +
            "(:categoryId is null or t.category.id=:categoryId)")
    Page<TaskEntity> findAllByParam(@Param("title") String title,
                                    @Param("completed") Boolean completed,
                                    @Param("priorityId") Integer priorityId,
                                    @Param("categoryId") Integer categoryId,
                                    Pageable pageable
    );

    TaskEntity findByIdAndUserId(Integer id, Integer userId);

    void deleteByIdAndUserId(Integer id, Integer userId);

}
