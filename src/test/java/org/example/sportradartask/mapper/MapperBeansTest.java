package org.example.sportradartask.mapper;

import org.example.sportradartask.dto.EventRequestDTO;
import org.example.sportradartask.dto.SportDTO;
import org.example.sportradartask.dto.StageDTO;
import org.example.sportradartask.dto.TeamDTO;
import org.example.sportradartask.dto.VenueDTO;
import org.example.sportradartask.model.Event;
import org.example.sportradartask.model.MatchResult;
import org.example.sportradartask.model.MatchStatus;
import org.example.sportradartask.model.Sport;
import org.example.sportradartask.model.Stage;
import org.example.sportradartask.model.Team;
import org.example.sportradartask.model.Venue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MapperBeansTest {

    private EventMapperImpl eventMapper;
    private SportMapperImpl sportMapper;
    private TeamMapperImpl teamMapper;
    private VenueMapperImpl venueMapper;
    private StageMapperImpl stageMapper;
    private MatchResultMapperImpl matchResultMapper;

    @BeforeEach
    void setUpMappers() throws Exception {
        eventMapper = new EventMapperImpl();
        sportMapper = new SportMapperImpl();
        venueMapper = new VenueMapperImpl();
        stageMapper = new StageMapperImpl();
        matchResultMapper = new MatchResultMapperImpl();

        teamMapper = new TeamMapperImpl();
        inject(teamMapper, "eventMapper", eventMapper);
        inject(teamMapper, "matchResultMapper", matchResultMapper);
    }

    private static void inject(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void testSportToDtoMapsAllProperties() {
        // Given
        Sport sport = new Sport();
        sport.setId(1L);
        sport.setName("Football");

        // When
        var dto = sportMapper.toDto(sport);

        // Then
        assertEquals("Football", dto.name());
    }

    @Test
    void testSportToEntityMapsAllProperties() {
        // Given
        var dto = new SportDTO( "Football");

        // When
        Sport result = sportMapper.toEntity(dto);

        // Then
        assertEquals("Football", result.getName());
    }

    @Test
    void testVenueToDtoMapsAllProperties() {
        // Given
        Venue venue = new Venue();
        venue.setId(10L);
        venue.setName("Arena");
        venue.setCity("Berlin");
        venue.setCountry("DE");
        venue.setCapacity(50000);

        // When
        var dto = venueMapper.toDto(venue);

        // Then
        assertEquals("Arena", dto.name());
        assertEquals("Berlin", dto.city());
        assertEquals("DE", dto.country());
        assertEquals(50000, dto.capacity());
    }

    @Test
    void testVenueToEntityMapsAllProperties() {
        // Given
        var dto = new VenueDTO("Arena", "Berlin", "DE", 50000);

        // When
        Venue result = venueMapper.toEntity(dto);

        // Then
        assertEquals("Arena", result.getName());
        assertEquals("Berlin", result.getCity());
        assertEquals("DE", result.getCountry());
        assertEquals(50000, result.getCapacity());
    }

    @Test
    void testStageToDtoMapsAllProperties() {
        // Given
        Stage stage = new Stage();
        stage.setId(3L);
        stage.setName("Quarter-final");
        stage.setOrdering(2);

        // When
        var dto = stageMapper.toDto(stage);

        // Then
        assertEquals("Quarter-final", dto.name());
        assertEquals(2, dto.ordering());
    }

    @Test
    void testStageToEntityMapsAllProperties() {
        // Given
        var dto = new StageDTO( "Quarter-final", 2);

        // When
        Stage result = stageMapper.toEntity(dto);

        // Then
        assertEquals("Quarter-final", result.getName());
        assertEquals(2, result.getOrdering());
    }

    @Test
    void testMatchResultToDtoMapsAllProperties() {
        // Given
        Team winner = new Team();
        winner.setId(7L);
        winner.setName("Champions");

        MatchResult entity = new MatchResult();
        entity.setId(100L);
        entity.setHomeGoals(3);
        entity.setAwayGoals(1);
        entity.setWinnerTeam(winner);
        entity.setMessage("Regulation");

        // When
        var dto = matchResultMapper.toDto(entity);

        // Then
        assertEquals(3, dto.homeGoals());
        assertEquals(1, dto.awayGoals());
        assertEquals(7L, dto.winnerTeamId());
        assertEquals("Champions", dto.winnerTeamName());
        assertEquals("Regulation", dto.message());
    }

    @Test
    void testMatchResultToEntityMapsAllProperties() {
        // Given
        Team winner = new Team();
        winner.setId(7L);
        winner.setName("Champions");

        // When
        MatchResult result = matchResultMapper.toEntity(3, 1, winner, "Walkover");

        // Then
        assertEquals(3, result.getHomeGoals());
        assertEquals(1, result.getAwayGoals());
        assertSame(winner, result.getWinnerTeam());
        assertEquals("Walkover", result.getMessage());
        assertNull(result.getId(), "Id should be ignored on insert mapping");
        assertNull(result.getCreatedAt(), "CreatedAt should be ignored");
        assertNull(result.getUpdatedAt(), "UpdatedAt should be ignored");
    }

    @Test
    void testMatchResultToDtoUsesNullWinnerFieldsWhenWinnerMissing() {
        // Given
        MatchResult entity = new MatchResult();
        entity.setId(1L);
        entity.setHomeGoals(0);
        entity.setAwayGoals(0);
        entity.setWinnerTeam(null);
        entity.setMessage("Draw");

        // When
        var dto = matchResultMapper.toDto(entity);

        // Then
        assertNull(dto.winnerTeamId(), "Winner team id should be null when winner is absent");
        assertNull(dto.winnerTeamName(), "Winner team name should be null when winner is absent");
        assertEquals("Draw", dto.message());
    }

    @Test
    void testEventToDtoMapsAllProperties() {
        // Given
        Sport sport = new Sport();
        sport.setId(1L);
        sport.setName("Soccer");

        Team home = new Team();
        home.setId(10L);
        home.setName("Home United");
        home.setAbbreviation("HOM");

        Team away = new Team();
        away.setId(11L);
        away.setName("Away City");
        away.setAbbreviation("AWY");

        Venue venue = new Venue();
        venue.setId(20L);
        venue.setName("National Stadium");
        venue.setCity("Capital");

        MatchResult result = new MatchResult();
        result.setHomeGoals(2);
        result.setAwayGoals(1);
        result.setWinnerTeam(home);
        result.setMessage("FT");

        Event event = new Event();
        event.setId(99L);
        event.setSeason(2025);
        event.setStatus(MatchStatus.PLAYED);
        event.setEventDate(LocalDate.of(2025, 3, 1));
        event.setEventTime(LocalTime.of(20, 45));
        event.setSport(sport);
        event.setHomeTeam(home);
        event.setAwayTeam(away);
        event.setVenue(venue);
        event.setResult(result);
        event.setCompetitionName("League");

        // When
        var dto = eventMapper.toDto(event);

        // Then
        assertEquals(99L, dto.id());
        assertEquals(2025, dto.season());
        assertEquals("played", dto.status());
        assertEquals(LocalDate.of(2025, 3, 1), dto.eventDate());
        assertEquals(LocalTime.of(20, 45), dto.eventTime());
        assertEquals(1L, dto.sportId());
        assertEquals("Soccer", dto.sportName());
        assertEquals(10L, dto.homeTeamId());
        assertEquals("Home United", dto.homeTeamName());
        assertEquals("HOM", dto.homeTeamAbbreviation());
        assertEquals(11L, dto.awayTeamId());
        assertEquals("Away City", dto.awayTeamName());
        assertEquals("AWY", dto.awayTeamAbbreviation());
        assertEquals(20L, dto.venueId());
        assertEquals("National Stadium", dto.venueName());
        assertEquals("Capital", dto.venueCity());
        assertEquals(2, dto.homeGoals());
        assertEquals(1, dto.awayGoals());
        assertEquals("Home United", dto.winnerTeamName());
        assertEquals("FT", dto.resultMessage());
        assertEquals("League", dto.competitionName());
    }

    @Test
    void testEventToEntityMapsAllProperties() {
        // Given
        Sport sport = new Sport();
        sport.setId(1L);
        sport.setName("Soccer");

        Team home = new Team();
        home.setId(2L);
        Team away = new Team();
        away.setId(3L);

        Venue venue = new Venue();
        venue.setId(40L);
        venue.setName("Park");

        Stage stage = new Stage();
        stage.setId(5L);
        stage.setName("Final");

        LocalDate eventDate = LocalDate.of(2026, 7, 15);
        LocalTime eventTime = LocalTime.of(19, 0);

        EventRequestDTO request = new EventRequestDTO(
                2026,
                eventDate,
                eventTime,
                "Soccer",
                "home-slug",
                "away-slug",
                "Park",
                "Final",
                "Cup",
                "Kickoff at 19:00"
        );

        // When
        Event result = eventMapper.toEntity(request, sport, home, away, venue, stage);

        // Then
        assertEquals(2026, result.getSeason());
        assertEquals(eventDate, result.getEventDate());
        assertEquals(eventTime, result.getEventTime());
        assertEquals(MatchStatus.SCHEDULED, result.getStatus(), "Status should default to SCHEDULED for new events");
        assertSame(sport, result.getSport());
        assertSame(home, result.getHomeTeam());
        assertSame(away, result.getAwayTeam());
        assertSame(venue, result.getVenue());
        assertSame(stage, result.getStage());
        assertEquals("Cup", result.getCompetitionName());
        assertEquals("Kickoff at 19:00", result.getDescription());
        assertNull(result.getId(), "Id should not be set from request mapping");
        assertNull(result.getResult(), "Result should be ignored for request mapping");
        assertNull(result.getCreatedAt(), "CreatedAt should be ignored");
        assertNull(result.getUpdatedAt(), "UpdatedAt should be ignored");
    }

    @Test
    void testEventToEntityUsesScheduledStatusWhenVenueAndStageAreNull() {
        // Given
        Sport sport = new Sport();
        sport.setId(1L);
        Team home = new Team();
        home.setId(2L);
        Team away = new Team();
        away.setId(3L);

        EventRequestDTO request = new EventRequestDTO(
                2025,
                LocalDate.now(),
                LocalTime.NOON,
                "Soccer",
                "h",
                "a",
                null,
                null,
                null,
                null
        );

        // When
        Event result = eventMapper.toEntity(request, sport, home, away, null, null);

        // Then
        assertEquals(MatchStatus.SCHEDULED, result.getStatus(), "Status should default to SCHEDULED");
        assertNull(result.getVenue());
        assertNull(result.getStage());
    }

    @Test
    void testTeamToEntityMapsAllProperties() {
        // Given
        var dto = new TeamDTO( "Name", "Official", "slug", "AB", "US", null, null, null);

        // When
        Team result = teamMapper.toEntity(dto);

        // Then
        assertEquals("Name", result.getName());
        assertEquals("Official", result.getOfficialName());
        assertEquals("slug", result.getSlug());
        assertEquals("AB", result.getAbbreviation());
        assertEquals("US", result.getCountryCode());
        assertNotNull(result.getHomeEvents(), "Home events list should remain initialized");
        assertNotNull(result.getAwayEvents(), "Away events list should remain initialized");
        assertNotNull(result.getWonMatches(), "Won matches list should remain initialized");
        assertTrue(result.getHomeEvents().isEmpty(), "Nested home events from DTO should not be mapped");
        assertTrue(result.getAwayEvents().isEmpty(), "Nested away events from DTO should not be mapped");
        assertTrue(result.getWonMatches().isEmpty(), "Nested won matches from DTO should not be mapped");
    }

    @Test
    void testTeamToDtoMapsAllProperties() {
        // Given
        Team team = new Team();
        team.setId(8L);
        team.setName("Club");
        team.setOfficialName("Club Official");
        team.setSlug("club");
        team.setAbbreviation("CL");
        team.setCountryCode("GB");

        // When
        var dto = teamMapper.toDto(team);

        // Then
        assertEquals("Club", dto.name());
        assertEquals("Club Official", dto.officialName());
        assertEquals("club", dto.slug());
        assertEquals("CL", dto.abbreviation());
        assertEquals("GB", dto.countryCode());
        assertNotNull(dto.homeEvents());
        assertNotNull(dto.awayEvents());
        assertNotNull(dto.wonMatches());
        assertTrue(dto.homeEvents().isEmpty());
        assertTrue(dto.awayEvents().isEmpty());
        assertTrue(dto.wonMatches().isEmpty());
    }
}
