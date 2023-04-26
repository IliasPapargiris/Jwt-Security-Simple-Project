package com.logicea.demo.service;

import com.logicea.demo.models.Card;
import com.logicea.demo.models.User;
import com.logicea.demo.util.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CardService {

    Page<Card> findCardsWithFilters(Long userId, String colour, String name, CardStatus cardStatus, Pageable pageable);

    List<Card> findCardsForUser(Long userId);

    List<Card> findAllCards();

    public List<Card> findCardsWithFilters(Long userId, String colour, String name, CardStatus cardStatus);

    Card createCard(String name, Optional<String> description, Optional<String> colour, User user);

    Boolean cardExists(Long cardId);
}
