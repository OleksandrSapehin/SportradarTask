package org.example.sportradartask.dto;

import java.util.List;

public record TeamDTO(
        String name,
        String officialName,
        String slug,
        String abbreviation,
        String countryCode,
        List<EventResponseDTO> homeEvents,
        List<EventResponseDTO> awayEvents,
        List<MatchResultDTO> wonMatches
) {}
