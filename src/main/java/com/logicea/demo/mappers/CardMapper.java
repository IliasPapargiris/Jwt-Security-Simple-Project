package com.logicea.demo.mappers;

import com.logicea.demo.dto.CardDto;
import com.logicea.demo.dto.CardResponseDto;
import com.logicea.demo.exceptions.BadRequestException;
import com.logicea.demo.models.Card;
import com.logicea.demo.util.CardStatus;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public Card toCard(CardDto cardDto) throws BadRequestException {
        Card card = new Card();
        card.setName(cardDto.getName());
        card.setColour(cardDto.getColour());
        card.setDescription(cardDto.getDescription());
        card.setStatus(CardStatus.fromString(cardDto.getStatus()));

        return card;
    }


    public CardResponseDto toCardResponseDto(Card card) {
        return new CardResponseDto(card.getId(),
                card.getName(),
                card.getColour(),
                card.getDescription(),
                card.getUser().getId(),
                card.getStatus().getStatus());
    }
}