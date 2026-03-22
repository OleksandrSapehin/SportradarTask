package org.example.sportradartask.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EventRequestDtoValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void close() {
        factory.close();
    }

    @Test
    void testValidRequestHasNoViolations() {
        // Given
        EventRequestDTO request = new EventRequestDTO(
                2025,
                LocalDate.now(),
                LocalTime.of(20, 0),
                "Football",
                "home-slug",
                "away-slug",
                null,
                null,
                "League",
                "Notes"
        );

        // When
        Set<ConstraintViolation<EventRequestDTO>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "Valid request should produce no constraint violations");
    }

    @Test
    void testSeasonBelowMinReportsViolation() {
        // Given
        EventRequestDTO request = new EventRequestDTO(
                1800,
                LocalDate.now(),
                LocalTime.NOON,
                "Football",
                "home-slug",
                "away-slug",
                null,
                null,
                null,
                null
        );

        // When
        Set<String> paths = propertyPaths(validator.validate(request));

        // Then
        assertTrue(paths.contains("season"), "Season below minimum should be reported on season");
    }

    @Test
    void testSeasonAboveMaxReportsViolation() {
        // Given
        EventRequestDTO request = new EventRequestDTO(
                2200,
                LocalDate.now(),
                LocalTime.NOON,
                "Football",
                "home-slug",
                "away-slug",
                null,
                null,
                null,
                null
        );

        // When
        Set<String> paths = propertyPaths(validator.validate(request));

        // Then
        assertTrue(paths.contains("season"), "Season above maximum should be reported on season");
    }

    @Test
    void testEventDateInPastReportsViolation() {
        // Given
        EventRequestDTO request = new EventRequestDTO(
                2025,
                LocalDate.now().minusDays(1),
                LocalTime.NOON,
                "Football",
                "home-slug",
                "away-slug",
                null,
                null,
                null,
                null
        );

        // When
        Set<String> paths = propertyPaths(validator.validate(request));

        // Then
        assertTrue(paths.contains("eventDate"), "Past event date should be reported on eventDate");
    }

    @Test
    void testBlankHomeTeamSlugReportsViolation() {
        // Given
        EventRequestDTO request = new EventRequestDTO(
                2025,
                LocalDate.now(),
                LocalTime.NOON,
                "Football",
                "",
                "away-slug",
                null,
                null,
                null,
                null
        );

        // When
        Set<String> paths = propertyPaths(validator.validate(request));

        // Then
        assertTrue(paths.contains("homeTeamSlug"), "Blank home team slug should be reported on homeTeamSlug");
    }

    @Test
    void testSportNameTooShortReportsViolation() {
        // Given
        EventRequestDTO request = new EventRequestDTO(
                2025,
                LocalDate.now(),
                LocalTime.NOON,
                "X",
                "home-slug",
                "away-slug",
                null,
                null,
                null,
                null
        );

        // When
        Set<String> paths = propertyPaths(validator.validate(request));

        // Then
        assertTrue(paths.contains("sportName"), "Sport name shorter than min size should be reported on sportName");
    }

    @Test
    void testDescriptionTooLongReportsViolation() {
        // Given
        EventRequestDTO request = new EventRequestDTO(
                2025,
                LocalDate.now(),
                LocalTime.NOON,
                "Football",
                "home-slug",
                "away-slug",
                null,
                null,
                null,
                "x".repeat(501)
        );

        // When
        Set<String> paths = propertyPaths(validator.validate(request));

        // Then
        assertTrue(paths.contains("description"), "Description over max length should be reported on description");
    }

    private static Set<String> propertyPaths(Set<ConstraintViolation<EventRequestDTO>> violations) {
        return violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());
    }
}
