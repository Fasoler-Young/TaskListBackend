package com.javabegin.tasklist.backendspringboot.repo;

import com.javabegin.tasklist.backendspringboot.entity.PriorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<PriorityEntity, Integer> {

    @Query("SELECT p FROM PriorityEntity p where " +
            "(:title is null or :title='' or lower(p.title) like lower(concat('%', :title, '%') ) )" +
            "order by p.title asc")
    List<PriorityEntity> findByTitle(@Param("title") String title);

    // Сортировка по названию
    List<PriorityEntity> findAllByOrderByTitleAsc();

}
