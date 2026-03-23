package org.example.sportradartask.dto;

public record MatchResultDTO(
        Integer homeGoals,
        Integer awayGoals,
        Long winnerTeamId,
        String winnerTeamName,
        String message
) {}
