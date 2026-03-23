package org.example.sportradartask.service;

import org.example.sportradartask.dto.EventRequestDTO;
import org.example.sportradartask.dto.EventResponseDTO;
import org.example.sportradartask.mapper.EventMapper;
import org.example.sportradartask.model.*;
import org.example.sportradartask.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private SportRepository sportRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private VenueRepository venueRepository;
    @Mock
    private StageRepository stageRepository;
    @Mock
    private MatchResultRepository matchResultRepository;
    @Mock
    private EventMapper eventMapper;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventService = new EventService(
                eventRepository,
                sportRepository,
                teamRepository,
                venueRepository,
                stageRepository,
                matchResultRepository,
                eventMapper
        );
    }

    @Test
    void testCreateEventCreatesSportWhenMissingAndSavesEvent() {
        // Given
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

        Sport sport = new Sport();
        sport.setId(1L);
        sport.setName("Soccer");

        Team homeTeam = new Team();
        homeTeam.setId(2L);
        homeTeam.setSlug("home-slug");

        Team awayTeam = new Team();
        awayTeam.setId(3L);
        awayTeam.setSlug("away-slug");

        Venue venue = new Venue();
        venue.setId(40L);
        venue.setName("Park");

        Stage stage = new Stage();
        stage.setId(5L);
        stage.setName("Final");

        Event eventEntity = new Event();

        EventResponseDTO expected = emptyEventResponseDTO();

        when(sportRepository.findByName(eq("Soccer"))).thenReturn(Optional.empty());
        when(sportRepository.save(any(Sport.class))).thenReturn(sport);

        when(teamRepository.findBySlug(eq("home-slug"))).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findBySlug(eq("away-slug"))).thenReturn(Optional.of(awayTeam));

        when(venueRepository.findByName(eq("Park"))).thenReturn(Optional.of(venue));
        when(stageRepository.findByName(eq("Final"))).thenReturn(Optional.of(stage));

        when(eventMapper.toEntity(eq(request), eq(sport), eq(homeTeam), eq(awayTeam), eq(venue), eq(stage)))
                .thenReturn(eventEntity);
        when(eventRepository.save(eq(eventEntity))).thenReturn(eventEntity);
        when(eventMapper.toDto(eq(eventEntity))).thenReturn(expected);

        // When
        EventResponseDTO result = eventService.createEvent(request);

        // Then
        assertSame(expected, result);
        verify(sportRepository).findByName(eq("Soccer"));
        verify(sportRepository).save(any(Sport.class));
        verify(teamRepository).findBySlug(eq("home-slug"));
        verify(teamRepository).findBySlug(eq("away-slug"));
        verify(venueRepository).findByName(eq("Park"));
        verify(stageRepository).findByName(eq("Final"));
        verify(eventMapper).toEntity(eq(request), eq(sport), eq(homeTeam), eq(awayTeam), eq(venue), eq(stage));
        verify(eventRepository).save(eq(eventEntity));
        verify(eventMapper).toDto(eq(eventEntity));
    }

    @Test
    void findEventsByTeam_ShouldReturnEventList_WhenTeamExists() {
        // Given
        Long teamId = 1L;
        Team team = new Team();
        team.setId(teamId);
        team.setName("Arsenal");

        List<Event> events = List.of(new Event(), new Event());
        List<EventResponseDTO> expectedDtos = List.of(
                emptyEventResponseDTO(),
                emptyEventResponseDTO()
        );

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(eventRepository.findAllEventsByTeam(team)).thenReturn(events);
        when(eventMapper.toDto(events)).thenReturn(expectedDtos);

        // When
        List<EventResponseDTO> result = eventService.findEventsByTeam(teamId);

        // Then
        assertSame(expectedDtos, result);
        verify(teamRepository).findById(teamId);
        verify(eventRepository).findAllEventsByTeam(team);
        verify(eventMapper).toDto(events);
    }

    @Test
    void findEventsByTeam_ShouldThrowException_WhenTeamNotFound() {
        // Given
        Long teamId = 999L;
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> eventService.findEventsByTeam(teamId));
        verify(teamRepository).findById(teamId);
        verifyNoInteractions(eventRepository, eventMapper);
    }

    @Test
    void findEventsBySport_ShouldReturnEventList() {
        // Given
        String sportName = "Soccer";
        List<Event> events = List.of(new Event(), new Event());
        List<EventResponseDTO> expectedDtos = List.of(
                emptyEventResponseDTO(),
                emptyEventResponseDTO()
        );

        when(eventRepository.findBySportName(sportName)).thenReturn(events);
        when(eventMapper.toDto(events)).thenReturn(expectedDtos);

        // When
        List<EventResponseDTO> result = eventService.findEventsBySport(sportName);

        // Then
        assertSame(expectedDtos, result);
        verify(eventRepository).findBySportName(sportName);
        verify(eventMapper).toDto(events);
    }

    @Test
    void findEventsByStatus_ShouldReturnEventList() {
        // Given
        MatchStatus status = MatchStatus.SCHEDULED;
        List<Event> events = List.of(new Event(), new Event());
        List<EventResponseDTO> expectedDtos = List.of(
                emptyEventResponseDTO(),
                emptyEventResponseDTO()
        );

        when(eventRepository.findByStatus(status)).thenReturn(events);
        when(eventMapper.toDto(events)).thenReturn(expectedDtos);

        // When
        List<EventResponseDTO> result = eventService.findEventsByStatus(status);

        // Then
        assertSame(expectedDtos, result);
        verify(eventRepository).findByStatus(status);
        verify(eventMapper).toDto(events);
    }

    @Test
    void findEventsByDate_ShouldReturnEventList() {
        // Given
        LocalDate date = LocalDate.of(2026, 3, 23);
        List<Event> events = List.of(new Event(), new Event());
        List<EventResponseDTO> expectedDtos = List.of(
                emptyEventResponseDTO(),
                emptyEventResponseDTO()
        );

        when(eventRepository.findByEventDate(date)).thenReturn(events);
        when(eventMapper.toDto(events)).thenReturn(expectedDtos);

        // When
        List<EventResponseDTO> result = eventService.findEventsByDate(date);

        // Then
        assertSame(expectedDtos, result);
        verify(eventRepository).findByEventDate(date);
        verify(eventMapper).toDto(events);
    }

    @Test
    void testUpdateEventUpdatesVenueAndStageWhenNamesChange() {
        // Given
        Long eventId = 100L;

        Sport oldSport = new Sport();
        oldSport.setName("OldSport");
        oldSport.setId(1L);

        Sport newSport = new Sport();
        newSport.setName("Soccer");
        newSport.setId(2L);

        Team oldHome = new Team();
        oldHome.setSlug("old-home");
        oldHome.setId(10L);

        Team oldAway = new Team();
        oldAway.setSlug("old-away");
        oldAway.setId(11L);

        Team newHome = new Team();
        newHome.setSlug("home-slug");
        newHome.setId(12L);

        Team newAway = new Team();
        newAway.setSlug("away-slug");
        newAway.setId(13L);

        Venue oldVenue = new Venue();
        oldVenue.setName("OldPark");
        oldVenue.setId(40L);

        Venue newVenue = new Venue();
        newVenue.setName("Park");
        newVenue.setId(41L);

        Stage oldStage = new Stage();
        oldStage.setName("OldStage");
        oldStage.setId(5L);

        Stage newStage = new Stage();
        newStage.setName("Final");
        newStage.setId(6L);

        Event event = new Event();
        event.setSeason(2025);
        event.setEventDate(LocalDate.of(2025, 1, 1));
        event.setEventTime(LocalTime.of(10, 0));
        event.setCompetitionName("OldCup");
        event.setDescription("OldDesc");
        event.setSport(oldSport);
        event.setHomeTeam(oldHome);
        event.setAwayTeam(oldAway);
        event.setVenue(oldVenue);
        event.setStage(oldStage);

        EventRequestDTO request = new EventRequestDTO(
                2026,
                LocalDate.of(2026, 7, 15),
                LocalTime.of(19, 0),
                "Soccer",
                "home-slug",
                "away-slug",
                "Park",
                "Final",
                "Cup",
                "Kickoff at 19:00"
        );

        EventResponseDTO expected = emptyEventResponseDTO();

        when(eventRepository.findById(eq(eventId))).thenReturn(Optional.of(event));
        when(sportRepository.findByName(eq("Soccer"))).thenReturn(Optional.of(newSport));
        when(teamRepository.findBySlug(eq("home-slug"))).thenReturn(Optional.of(newHome));
        when(teamRepository.findBySlug(eq("away-slug"))).thenReturn(Optional.of(newAway));
        when(venueRepository.findByName(eq("Park"))).thenReturn(Optional.of(newVenue));
        when(stageRepository.findByName(eq("Final"))).thenReturn(Optional.of(newStage));
        when(eventMapper.toDto(eq(event))).thenReturn(expected);

        // When
        EventResponseDTO result = eventService.updateEvent(eventId, request);

        // Then
        assertSame(expected, result);
        assertEquals(2026, event.getSeason());
        assertEquals(request.eventDate(), event.getEventDate());
        assertEquals(request.eventTime(), event.getEventTime());
        assertEquals("Cup", event.getCompetitionName());
        assertEquals("Kickoff at 19:00", event.getDescription());

        assertSame(newSport, event.getSport());
        assertSame(newHome, event.getHomeTeam());
        assertSame(newAway, event.getAwayTeam());
        assertSame(newVenue, event.getVenue());
        assertSame(newStage, event.getStage());

        verify(eventRepository).findById(eq(eventId));
        verify(sportRepository).findByName(eq("Soccer"));
        verify(teamRepository).findBySlug(eq("home-slug"));
        verify(teamRepository).findBySlug(eq("away-slug"));
        verify(venueRepository).findByName(eq("Park"));
        verify(stageRepository).findByName(eq("Final"));
        verify(eventMapper).toDto(eq(event));
    }

    @Test
    void testAddResultToEventSetsResultAndStatusToPlayed() {
        // Given
        Long eventId = 200L;
        Long winnerTeamId = 300L;

        Event event = new Event();
        event.setId(eventId);
        event.setStatus(MatchStatus.SCHEDULED);

        Team winnerTeam = new Team();
        winnerTeam.setId(winnerTeamId);
        winnerTeam.setName("Champions");

        when(eventRepository.findById(eq(eventId))).thenReturn(Optional.of(event));
        when(teamRepository.findById(eq(winnerTeamId))).thenReturn(Optional.of(winnerTeam));

        EventResponseDTO expected = emptyEventResponseDTO();
        when(eventMapper.toDto(eq(event))).thenReturn(expected);

        ArgumentCaptor<MatchResult> captor = ArgumentCaptor.forClass(MatchResult.class);
        when(matchResultRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        EventResponseDTO result = eventService.addResultToEvent(eventId, 2, 1, winnerTeamId, "FT");

        // Then
        assertSame(expected, result);
        assertEquals(MatchStatus.PLAYED, event.getStatus());
        assertSame(winnerTeam, event.getResult().getWinnerTeam());
        assertEquals(2, event.getResult().getHomeGoals());
        assertEquals(1, event.getResult().getAwayGoals());
        assertEquals("FT", event.getResult().getMessage());

        verify(matchResultRepository).save(any(MatchResult.class));
        verify(eventMapper).toDto(eq(event));
    }

    private static EventResponseDTO emptyEventResponseDTO() {
        return new EventResponseDTO(
                null, null, null, null, null,
                null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null
        );
    }
}

