package com.logicea.demo.controller;

import com.logicea.demo.dto.CardDto;
import com.logicea.demo.dto.CardResponseDto;
import com.logicea.demo.exceptions.BadRequestException;
import com.logicea.demo.exceptions.CardNotFoundException;
import com.logicea.demo.exceptions.ForbiddenException;
import com.logicea.demo.mappers.CardMapper;
import com.logicea.demo.models.Card;
import com.logicea.demo.models.User;
import com.logicea.demo.service.CardService;
import com.logicea.demo.util.CardStatus;
import com.logicea.demo.util.SortUtils;
import com.logicea.demo.validation.ValidCardStatus;
import io.swagger.annotations.*;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping(value = "api/v1/cards", path = "api/v1/cards")
@RequiredArgsConstructor
@Api(value = "CARDS", produces = MediaType.APPLICATION_JSON_VALUE)
public class CardController {

    private static final List<String> ALLOWED_SORT_FIELDS_FOR_CARD = Arrays.asList("name", "colour", "cardStatus");

    private final CardService cardService;

    private final CardMapper cardMapper;

    @GetMapping("/allCards")
    @ApiOperation(value = "Get all cards (admin only)", notes = "Returns a list of all cards. This endpoint is restricted to users with the 'ADMIN' role.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CardResponseDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<List<CardResponseDto>> getAllCardsAdmin() {

        List<CardResponseDto> cardResponseDtos = cardService.findAllCards()
                .stream()
                .map(cardMapper::toCardResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cardResponseDtos);

    }

    @GetMapping("/userCards")
    @ApiOperation(value = "Get user cards", notes = "Returns a list of all cards owned by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CardResponseDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public ResponseEntity<List<CardResponseDto>> getUserCards() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<CardResponseDto> cardResponseDtos = cardService.findCardsForUser(user.getId())
                .stream()
                .map(cardMapper::toCardResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(cardResponseDtos);
    }
    @ApiOperation(value = "Get paginated and filtered/sorted cards")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved paginated and filtered/sorted cards"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/filteredCardsPaginated")
    public ResponseEntity<Page<CardResponseDto>> getFilteredSortedCards(
            @ApiParam(value = "Name of the card to filter by", required = true) @RequestParam(value = "name") String name,
            @ApiParam(value = "Colour of the card to filter by (format: hexadecimal colour code)")
            @Pattern(regexp = CardDto.COLOUR_REGEX,message = "Invalid colour code,colour must follow hex colour code format") @RequestParam(value = "colour", required = false) String colour,
            @ApiParam(value = "Status of the card , valid values: to do, in progress , done")
            @ValidCardStatus  @Nullable @RequestParam(value = "cardStatus", required = false) String cardStatus,
            @ApiParam(value = "Page number to retrieve (default: 0)") @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @ApiParam(value = "Number of cards per page (default: 10)") @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @ApiParam(value = "Sort parameters (format: fieldname,direction; can specify multiple fields, separated by commas)") @RequestParam(value = "sortParams", required = false) Optional<String[]> sortParams
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable;
        CardStatus status = cardStatus != null ? CardStatus.fromString(cardStatus) : null;
        if (sortParams.isPresent()) {
            Sort validatedSort = SortUtils.validateSort(sortParams.get(), ALLOWED_SORT_FIELDS_FOR_CARD);

            pageable = PageRequest.of(page, size, validatedSort);
            Page<Card> cards = cardService.findCardsWithFilters(user.getId(), colour, name, status, pageable);

            Page<CardResponseDto> cardResponseDtos = cards.map(cardMapper::toCardResponseDto);
            return ResponseEntity.ok(cardResponseDtos);
        }

        pageable = PageRequest.of(page, size);
        Page<Card> cards = cardService.findCardsWithFilters(user.getId(), colour, name, status, pageable);

        Page<CardResponseDto> cardResponseDtos = cards.map(cardMapper::toCardResponseDto);

        return ResponseEntity.ok(cardResponseDtos);
    }

    @ApiOperation(value = "Create a new card", notes = "Creates a new card for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Card successfully created"),
            @ApiResponse(code = 400, message = "Invalid card data")
    })
    @PostMapping("/createCard")
    public ResponseEntity<CardResponseDto> createCard(@ApiParam(value = "Card data", required = true) @Valid @RequestBody CardDto cardDto, BindingResult ignoredBindingResult) throws BadRequestException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Card card = cardMapper.toCard(cardDto);
        Card createdCard = cardService.createCard(card, user);
        return ResponseEntity.ok(cardMapper.toCardResponseDto(createdCard));
    }


    @ApiOperation(value = "Find a card", notes = "Finds the specified card belonging to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Card successfully found", response = Card.class),
            @ApiResponse(code = 403, message = "Forbidden c for user"),
            @ApiResponse(code = 404, message = "Card not found")
    })
    @GetMapping("/getCard/{cardId}")
    public ResponseEntity<CardResponseDto> findCard(@ApiParam(value = "The ID of the card to find", required = true) @PathVariable Long cardId) throws CardNotFoundException {
        // get the authenticated user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //validates that card exists and is owned by user
        Card userCard = cardService.findCardForUser(cardId, user);
        CardResponseDto cardResponseDto = cardMapper.toCardResponseDto(userCard);
        return ResponseEntity.ok(cardResponseDto);
    }

    @ApiOperation(value = "Update a card", notes = "Updates the information for a card with the given ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Card updated successfully"),
            @ApiResponse(code = 400, message = "Invalid card data"),
            @ApiResponse(code = 403, message = "You do not have access to this card"),
            @ApiResponse(code = 404, message = "Card not found")
    })
    @PutMapping("/updateCard/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(
            @ApiParam(value = "ID of the card to update", required = true) @PathVariable Long cardId,
            @ApiParam(value = "Updated card information", required = true) @Valid @RequestBody CardDto cardDto
            , BindingResult ignoredBindingResult
    ) throws CardNotFoundException, ForbiddenException, BadRequestException {

        // get the authenticated user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // check if the card exists and the user has access to it
        Card toBeUpdated = cardMapper.toCard(cardDto);
        Card updateCard = cardService.updateCard(cardId, user, toBeUpdated);
        return ResponseEntity.ok(cardMapper.toCardResponseDto(updateCard));

    }

    @ApiOperation(value = "Delete a user's card", notes = "Deletes the specified card belonging to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Card successfully deleted"),
            @ApiResponse(code = 403, message = "User is not authorized to delete this card"),
            @ApiResponse(code = 404, message = "Card not found")
    })
    @DeleteMapping("/deleteCard/{cardId}")
    public ResponseEntity<?> deleteCard(@ApiParam(value = "ID of the card to delete", required = true) @PathVariable Long cardId) throws CardNotFoundException, ForbiddenException {
        // get the authenticated user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //// check if the card exists and the user has access to it to delete it
        cardService.deleteCard(cardId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
