package com.javabegin.tasklist.backendspringboot.repo;

import com.javabegin.tasklist.backendspringboot.entity.StatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface StatRepository extends CrudRepository<StatEntity, Integer> {


}
