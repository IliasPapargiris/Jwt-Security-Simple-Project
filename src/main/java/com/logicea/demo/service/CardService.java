package com.logicea.demo.service;

import com.logicea.demo.exceptions.CardDoesNotBelongToUserException;
import com.logicea.demo.exceptions.CardNotFoundException;
import com.logicea.demo.exceptions.ForbiddenException;
import com.logicea.demo.models.Card;
import com.logicea.demo.models.User;
import com.logicea.demo.util.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardService {

    Page<Card> findCardsWithFilters(Long userId, String colour, String name, CardStatus cardStatus, Pageable pageable);

    List<Card> findCardsForUser(Long userId);

    List<Card> findAllCards();

    Card createCard(Card card, User user);

    Card updateCard(Long cardId, User user,Card card) throws ForbiddenException, CardNotFoundException;

    Card findCardForUser(Long cardId, User user) throws CardDoesNotBelongToUserException, CardNotFoundException;

     void deleteCard(Long cardId, Long ownerId) throws CardNotFoundException, ForbiddenException;
}
