package org.example.sportradartask.dto;

public record MatchResultDTO(
        Long id,
        Integer homeGoals,
        Integer awayGoals,
        Long winnerTeamId,
        String winnerTeamName,
        String message
) {}
