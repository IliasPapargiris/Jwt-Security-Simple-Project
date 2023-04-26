package com.logicea.demo.dao;

import com.logicea.demo.models.Card;
import com.logicea.demo.util.CardStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Data
public class CardSpecification implements Specification<Card> {


    @Override
    public Specification<Card> and(Specification<Card> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Card> or(Specification<Card> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<Card> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }

    public static Specification<Card> hasColour(String color) {
        return (cardRoot, query, criteriaBuilder) -> criteriaBuilder.equal(cardRoot.get("colour"), color);
    }

    public static Specification<Card> hasName(String name) {
        return (cardRoot, query, criteriaBuilder) -> criteriaBuilder.equal(cardRoot.get("name"), name);
    }

    public static Specification<Card> hasDate(LocalDate date) {
        return (cardRoot, query, criteriaBuilder) -> criteriaBuilder.equal(cardRoot.get("date"), date);
    }

    public static Specification<Card> hasStatus(CardStatus status) {
        return (cardRoot, query, criteriaBuilder) -> criteriaBuilder.equal(cardRoot.get("status"), status);
    }

}
