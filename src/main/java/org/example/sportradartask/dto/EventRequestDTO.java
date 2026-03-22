package org.example.sportradartask.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventRequestDTO(

        @NotNull(message = "Season is required")
        @Min(value = 1900, message = "Season must be at least 1900")
        @Max(value = 2100, message = "Season cannot exceed 2100")
        Integer season,

        @NotNull(message = "Event date is required")
        @FutureOrPresent(message = "Event date cannot be in the past")
        LocalDate eventDate,

        @NotNull(message = "Event time is required")
        LocalTime eventTime,

        @NotBlank(message = "Sport name is required")
        @Size(min = 2, max = 50, message = "Sport name must be between 2 and 50 characters")
        String sportName,

        @NotBlank(message = "Home team slug is required")
        String homeTeamSlug,

        @NotBlank(message = "Away team slug is required")
        String awayTeamSlug,

        String venueName,

        String stageName,

        @Size(max = 100, message = "Competition name cannot exceed 100 characters")
        String competitionName,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description
) {}