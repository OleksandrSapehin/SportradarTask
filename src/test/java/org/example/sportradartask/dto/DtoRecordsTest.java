package org.example.sportradartask.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DtoRecordsTest {

    @Test
    void testEventResponseDtoMapsAllProperties() {
        // Given
        LocalDate eventDate = LocalDate.of(2025, 6, 1);
        LocalTime eventTime = LocalTime.of(18, 30);

        // When
        EventResponseDTO dto = new EventResponseDTO(
                1L, 2025, "scheduled", eventDate, eventTime,
                10L, "Soccer",
                20L, "Home FC", "HOM",
                21L, "Away FC", "AWY",
                30L, "Arena", "Berlin",
                2, 1, "Home FC", "Full time",
                "Cup"
        );

        // Then
        assertEquals(1L, dto.id());
        assertEquals(2025, dto.season());
        assertEquals("scheduled", dto.status());
        assertEquals(eventDate, dto.eventDate());
        assertEquals(eventTime, dto.eventTime());
        assertEquals(10L, dto.sportId());
        assertEquals("Soccer", dto.sportName());
        assertEquals(2, dto.homeGoals());
        assertEquals("Home FC", dto.winnerTeamName());
        assertEquals("Cup", dto.competitionName());
    }

    @Test
    void testSportDtoEqualityAndHashCode() {
        // Given
        SportDTO a = new SportDTO( "Tennis");
        SportDTO b = new SportDTO( "Tennis");

        // When
        int hashA = a.hashCode();
        int hashB = b.hashCode();

        // Then
        assertEquals(b, a, "Equal components should yield equal records");
        assertEquals(hashB, hashA, "Equal records should have equal hashCode");
    }

    @Test
    void testTeamDtoAllowsNullNestedLists() {
        // When
        TeamDTO dto = new TeamDTO( "n", "official", "slug", "AB", "DE", null, null, null);

        // Then
        assertNull(dto.homeEvents(), "homeEvents should be null when not provided");
        assertNull(dto.awayEvents(), "awayEvents should be null when not provided");
        assertNull(dto.wonMatches(), "wonMatches should be null when not provided");
    }

    @Test
    void testTeamDtoWithNestedListsExposesCollections() {
        // Given
        EventResponseDTO event = new EventResponseDTO(
                1L, 2025, "played", LocalDate.now(), LocalTime.NOON,
                1L, "S", 1L, "H", "H", 2L, "A", "A",
                null, null, null, 1, 0, "H", null, "C"
        );
        MatchResultDTO mr = new MatchResultDTO( 1, 0, 1L, "H", null);

        // When
        TeamDTO dto = new TeamDTO( "n", "o", "s", "AB", "DE",
                List.of(event), List.of(), List.of(mr));

        // Then
        assertEquals(1, dto.homeEvents().size());
        assertEquals(0, dto.awayEvents().size());
        assertEquals(1, dto.wonMatches().size());
    }

    @Test
    void testStageVenueMatchResultDtoAccessors() {
        // Given
        StageDTO stage = new StageDTO( "Final", 3);
        VenueDTO venue = new VenueDTO( "Stadium", "Munich", "DE", 75000);
        MatchResultDTO result = new MatchResultDTO( 2, 2, null, null, "Draw");

        // When
        Integer ordering = stage.ordering();
        Integer capacity = venue.capacity();
        String message = result.message();

        // Then
        assertEquals(3, ordering);
        assertEquals(75000, capacity);
        assertEquals("Draw", message);
    }
}
