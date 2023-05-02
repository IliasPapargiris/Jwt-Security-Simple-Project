package com.logicea.demo.service;

import com.logicea.demo.dao.CardDao;
import com.logicea.demo.dao.CardSpecification;
import com.logicea.demo.exceptions.CardDoesNotBelongToUserException;
import com.logicea.demo.exceptions.CardNotFoundException;
import com.logicea.demo.models.Card;
import com.logicea.demo.models.User;
import com.logicea.demo.util.CardStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardDao cardDao;

    @Override
    public Page<Card> findCardsWithFilters(Long userId, String colour, String name, CardStatus cardStatus, Pageable pageable) {
        Specification<Card> spec = Specification.where(CardSpecification.hasName(name))
                .and(CardSpecification.isOwnedBy(userId))
                .and(colour == null ? null : CardSpecification.hasColour(colour))
                .and(cardStatus == null ? null : CardSpecification.hasStatus(cardStatus));

        return cardDao.findAll(spec, pageable);
    }

    @Override
    public List<Card> findCardsForUser(Long userId) {

        return cardDao.findCardByUser(userId);
    }

    public Card findCardForUser(Long cardId, User user) throws CardDoesNotBelongToUserException, CardNotFoundException {
        Optional<Card> optionalCard = cardDao.findCardById(cardId);
        validOptionalCardForUser(optionalCard, user.getId(), cardId);
        return optionalCard.get();
    }


    @Override
    public List<Card> findAllCards() {
        return cardDao.findAll();
    }


    @Override
    @Transactional
    public Card createCard(Card card, User user) {
        card.setUser(user);
        card.setStatus(CardStatus.TODO);
        return cardDao.save(card);
    }

    @Override
    @Transactional
    public Card updateCard(Long cardId, User user, Card toBeUpdated) throws CardDoesNotBelongToUserException, CardNotFoundException {
        Optional<Card> optionalCard = cardDao.findCardById(cardId);
        validOptionalCardForUser(optionalCard, user.getId(), cardId);

        toBeUpdated.setId(cardId);
        toBeUpdated.setUser(user);
        return cardDao.save(toBeUpdated);

    }


    @Override
    @Transactional
    public void deleteCard(Long cardId, Long userId) throws CardNotFoundException {
        Optional<Card> optionalCard = cardDao.findById(cardId);
        validOptionalCardForUser(optionalCard, userId, cardId);
        cardDao.delete(optionalCard.get());
    }


    private void validOptionalCardForUser(Optional<Card> optionalCard, Long userId, Long cardId) throws CardNotFoundException, CardDoesNotBelongToUserException {
        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            if (!card.isOwnedBy(userId)) {
                throw new CardDoesNotBelongToUserException("You do not have access to this card with card id :" + cardId);
            }
        } else {
            throw new CardNotFoundException("There is no card with the given id : " + cardId);
        }
    }
}