package com.logicea.demo.dao;

import com.logicea.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT DISTINCT user FROM User user " +
            "JOIN FETCH user.cards cards")
    List<User> retrieveAllUsers();
}