package com.javabegin.tasklist.backendspringboot.repo;

import com.javabegin.tasklist.backendspringboot.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    @Query("SELECT c FROM CategoryEntity c where" +
            "(:title is null or :title='' or lower(c.title) like concat('%', :title, '%'))" +
            "order by c.title asc")
    List<CategoryEntity> findByTitle(@Param("title") String title);

    List<CategoryEntity> findAllByOrderById();
}
