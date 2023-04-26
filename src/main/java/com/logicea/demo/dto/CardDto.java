package com.logicea.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class CardDto {

    public static final String COLOUR_REGEX = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";


    @NotBlank(message = "name is mandatory")
    @Size(max = 100)
    private String name;


    @Size(max = 7)
    @Pattern(regexp = COLOUR_REGEX)
    private String colour;


    @Size(max = 100)
    String description;

}