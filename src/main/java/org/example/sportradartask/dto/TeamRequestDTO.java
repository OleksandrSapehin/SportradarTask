package org.example.sportradartask.dto;

import jakarta.validation.constraints.NotBlank;

public record TeamRequestDTO(
        @NotBlank String name,
        @NotBlank String slug,
        @NotBlank String abbreviation,
        @NotBlank String countryCode) {
}
