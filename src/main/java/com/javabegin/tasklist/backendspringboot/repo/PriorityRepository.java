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
            "(:title is null or :title='' or lower(p.title) like lower(concat('%', :title, '%') ) ) and" +
            "(p.userId=:userId)" +
            "order by p.title asc")
    List<PriorityEntity> findByTitle(@Param("title") String title, @Param("userId") Integer userId);

    List<PriorityEntity> findAllByUserId(Integer userId);

    void deleteByIdAndUserId(Integer id, Integer userId);

    PriorityEntity findByIdAndUserId(Integer id, Integer userId);

}
