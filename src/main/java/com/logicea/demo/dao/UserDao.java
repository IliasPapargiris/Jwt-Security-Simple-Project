package com.logicea.demo.dao;

import com.logicea.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {


    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}