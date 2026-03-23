package org.example.sportradartask.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddResultRequestDTO(
        @NotNull(message = "Home goals are required")
        @Min(value = 0, message = "Home goals must be >= 0")
        Integer homeGoals,

        @NotNull(message = "Away goals are required")
        @Min(value = 0, message = "Away goals must be >= 0")
        Integer awayGoals,

        Long winnerTeamId,

        @Size(max = 500, message = "Message cannot exceed 500 characters")
        String message
) {
}

