package org.example.sportradartask.dto;

public record VenueDTO(
        Long id,
        String name,
        String city,
        String country,
        Integer capacity
) {}
