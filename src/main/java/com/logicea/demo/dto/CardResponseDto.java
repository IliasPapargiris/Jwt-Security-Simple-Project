package com.logicea.demo.dto;

import lombok.Data;

@Data
public class CardResponseDto {
    private Long id;
    private String name;
    private String colour;
    private String description;
    private Long userId;
    private String status;

    public CardResponseDto(Long id, String name, String colour, String description, Long userId, String status) {
        this.id = id;
        this.name = name;
        this.colour = colour;
        this.description = description;
        this.userId = userId;
        this.status = status;
    }

}