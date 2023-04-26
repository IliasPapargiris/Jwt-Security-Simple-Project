package com.logicea.demo.controller;

import com.logicea.demo.dto.CardDto;
import com.logicea.demo.models.Card;
import com.logicea.demo.models.User;
import com.logicea.demo.service.CardService;
import com.logicea.demo.util.CardStatus;
import com.logicea.demo.util.Roles;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping(value = "api/v1/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/allCards")
    public List<Card> getAllCards() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRole() == Roles.ADMIN) {
            return cardService.findAllCards();
        }
        return cardService.findCardsForUser(user.getId());
    }

    @GetMapping("/filteredCards")
    public List<Card> getFilteredCards(@RequestParam(value = "name") String name, @Pattern(regexp = CardDto.COLOUR_REGEX) @RequestParam(value = "colour", required = false) String colour,
                                       @RequestParam(value = "cardStatus", required = false) CardStatus cardStatus) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return cardService.findCardsWithFilters(user.getId(), colour, name, cardStatus);
    }

    @GetMapping("/filteredCardsPaginated")
    public Page<Card> getFilteredCards(
            @RequestParam(value = "name") String name,
            @Pattern(regexp = CardDto.COLOUR_REGEX) @RequestParam(value = "colour", required = false) String colour,
            @RequestParam(value = "cardStatus", required = false) CardStatus cardStatus,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String[] sort
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return cardService.findCardsWithFilters(user.getId(), colour, name, cardStatus, pageable);
    }

    @PostMapping("/createCard")
    public Card createCard(@Valid @RequestBody CardDto card, BindingResult ignoredBindingResult) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return cardService.createCard(card.getName(), Optional.of(card.getDescription()), Optional.of(card.getColour()), user);
    }

    @GetMapping ("/getCard")
    public Card findCard(@PathVariable Long cardId, BindingResult ignoredBindingResult) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!cardService.cardExists(cardId)){
        throw new Exception("there is no card with id:"+cardId);
        }
        Optional<Card> usersCard = Optional.of(cardService.findCardsForUser(user.getId())
                .stream()
                .filter(card -> card.getId() == cardId)
                .findFirst().orElseThrow(()->new Exception(user.getUsername()+ " has no access to the card with id:"+cardId) ));

        return usersCard.get();
    }
}
