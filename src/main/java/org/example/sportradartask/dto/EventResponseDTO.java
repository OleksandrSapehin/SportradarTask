package org.example.sportradartask.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventResponseDTO(
        Long id,
        Integer season,
        String status,
        LocalDate eventDate,
        LocalTime eventTime,

        // Sport info
        Long sportId,
        String sportName,

        // Teams info
        Long homeTeamId,
        String homeTeamName,
        String homeTeamAbbreviation,

        Long awayTeamId,
        String awayTeamName,
        String awayTeamAbbreviation,

        // Venue info
        Long venueId,
        String venueName,
        String venueCity,

        // Result info
        Integer homeGoals,
        Integer awayGoals,
        String winnerTeamName,
        String resultMessage,

        String competitionName
) {}
