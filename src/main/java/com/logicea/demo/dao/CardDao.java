package com.logicea.demo.dao;

import com.logicea.demo.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardDao extends JpaRepository<Card, Long> , JpaSpecificationExecutor<Card> {


    Optional<Card> findCardById(Long id);

    @Query("SELECT c FROM Card c JOIN FETCH c.user WHERE c.user.id = :userId")
    List<Card> findCardByUserId(@Param("userId") Long userId);




}
