package com.logicea.demo.service;

import com.logicea.demo.dao.CardDao;
import com.logicea.demo.dao.CardSpecification;
import com.logicea.demo.models.Card;
import com.logicea.demo.models.User;
import com.logicea.demo.util.CardStatus;
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
    public List<Card> findCardsWithFilters(Long userId, String colour, String name, CardStatus cardStatus) {
        return cardDao.findAll(Specification.where(
                        CardSpecification.hasName(name))
                .and(colour == null ? null : CardSpecification.hasColour(colour))
                .and(cardStatus == null ? null : CardSpecification.hasStatus(cardStatus)
                )
        );

    }

    @Override
    public Page<Card> findCardsWithFilters(Long userId, String colour, String name, CardStatus cardStatus, Pageable pageable) {
        Specification<Card> spec = Specification.where(CardSpecification.hasName(name))
                .and(colour == null ? null : CardSpecification.hasColour(colour))
                .and(cardStatus == null ? null : CardSpecification.hasStatus(cardStatus));
        return cardDao.findAll(spec, pageable);

    }

    @Override
    public List<Card> findCardsForUser(Long userId) {
        return cardDao.findCardByUserId(userId);
    }


    @Override
    public List<Card> findAllCards() {
        return cardDao.findAll();
    }


    @Override
    public Card createCard(String name, Optional<String> description, Optional<String> colour, User user) {
        return cardDao.save(new Card(name, colour.get(), description.get(), user, CardStatus.TODO));
    }

    @Override
    public Boolean cardExists(Long cardId) {
        return cardDao.existsById(cardId);
    }
}
