package org.example.sportradartask.dto;

import java.time.LocalDate;

public record EventFilterDTO(
        String sport,
        Long teamId,
        LocalDate date
) {
}

