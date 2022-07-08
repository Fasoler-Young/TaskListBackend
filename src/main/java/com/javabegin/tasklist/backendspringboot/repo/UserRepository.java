package com.javabegin.tasklist.backendspringboot.repo;

import com.javabegin.tasklist.backendspringboot.entity.UserEntity;
import org.postgresql.util.PSQLException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByLogin(String login);

    Boolean existsUserEntityByLogin(String login);

    @Transactional
    void deleteByLogin(String login);

    @Modifying
    @Transactional
    @Query("update UserEntity set password=:password where login=:login")
    void changePassword(@Param("login") String login, @Param("password") String password);

    @Modifying
    @Transactional
    @Query("update UserEntity set role=:role where login=:login")
    void changeRole(@Param("login") String login, @Param("role") String role) throws PersistenceException;


}
