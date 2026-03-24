package org.example.sportradartask.service;

import lombok.RequiredArgsConstructor;
import org.example.sportradartask.dto.EventRequestDTO;
import org.example.sportradartask.dto.EventResponseDTO;
import org.example.sportradartask.exceptions.NotFoundException;
import org.example.sportradartask.mapper.EventMapper;
import org.example.sportradartask.model.*;
import org.example.sportradartask.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final SportRepository sportRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;
    private final StageRepository stageRepository;
    private final MatchResultRepository matchResultRepository;
    private final EventMapper eventMapper;


    public List<EventResponseDTO> getEvents() {
        return eventMapper.toDto(
                eventRepository.findAll()
        );
    }

    public List<EventResponseDTO> getUpcomingEvents() {
        return eventMapper.toDto(
                eventRepository.findUpcomingEvents(MatchStatus.SCHEDULED, LocalDate.now())
        );
    }

    public List<EventResponseDTO> searchEvents(String keyword) {
        return eventMapper.toDto(eventRepository.searchByKeyword(keyword));
    }

    public EventResponseDTO getEventById(Long id) {
        return eventMapper.toDto(getEventByIdOrThrow(id));
    }

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO request) {

        Sport sport = getOrCreateSport(request.sportName());

        Team homeTeam = getTeamBySlug(request.homeTeamSlug());
        Team awayTeam = getTeamBySlug(request.awayTeamSlug());

        Venue venue = getVenue(request.venueName());
        Stage stage = getStage(request.stageName());

        Event event = eventMapper.toEntity(request, sport, homeTeam, awayTeam, venue, stage);

        return eventMapper.toDto(eventRepository.save(event));
    }

    @Transactional
    public EventResponseDTO updateEvent(Long id, EventRequestDTO request) {

        Event event = getEventByIdOrThrow(id);

        updateSimpleFields(event, request);

        updateRelations(event, request);

        return eventMapper.toDto(event);
    }

    @Transactional
    public EventResponseDTO addResultToEvent(Long eventId, Integer homeGoals, Integer awayGoals, Long winnerTeamId, String message) {

        Event event = getEventByIdOrThrow(eventId);

        MatchResult result = createMatchResult(homeGoals, awayGoals, winnerTeamId, message);

        matchResultRepository.save(result);

        event.setResult(result);
        event.setStatus(MatchStatus.PLAYED);

        return eventMapper.toDto(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.delete(getEventByIdOrThrow(id));
    }


    private Event getEventByIdOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }

    private Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Team not found with id: " + id));
    }

    private Team getTeamBySlug(String slug) {
        return teamRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Team not found: " + slug));
    }

    private Venue getVenue(String name) {
        return name == null ? null : venueRepository.findByName(name).orElse(null);
    }

    private Stage getStage(String name) {
        return name == null ? null : stageRepository.findByName(name).orElse(null);
    }


    public List<EventResponseDTO> findEventsByTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found: " + teamId));
        return eventMapper.toDto(eventRepository.findAllEventsByTeam(team));
    }

    public List<EventResponseDTO> findEventsBySport(String sportName) {
        return eventMapper.toDto(eventRepository.findBySportName(sportName));
    }

    public List<EventResponseDTO> findEventsByStatus(MatchStatus status) {
        return eventMapper.toDto(eventRepository.findByStatus(status));
    }

    public List<EventResponseDTO> findEventsByDate(LocalDate date) {
        return eventMapper.toDto(eventRepository.findByEventDate(date));
    }

    @Transactional
    public EventResponseDTO updateEventStatus(Long eventId, MatchStatus status) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        event.setStatus(status);

        return eventMapper.toDto(eventRepository.save(event));
    }

    private void updateSimpleFields(Event event, EventRequestDTO request) {
        event.setSeason(request.season());
        event.setEventDate(request.eventDate());
        event.setEventTime(request.eventTime());
        event.setCompetitionName(request.competitionName());
        event.setDescription(request.description());
    }

    private void updateRelations(Event event, EventRequestDTO request) {

        if (!event.getSport().getName().equals(request.sportName())) {
            event.setSport(getOrCreateSport(request.sportName()));
        }

        if (!event.getHomeTeam().getSlug().equals(request.homeTeamSlug())) {
            event.setHomeTeam(getTeamBySlug(request.homeTeamSlug()));
        }

        if (!event.getAwayTeam().getSlug().equals(request.awayTeamSlug())) {
            event.setAwayTeam(getTeamBySlug(request.awayTeamSlug()));
        }

        event.setVenue(getVenue(request.venueName()));
        event.setStage(getStage(request.stageName()));
    }

    private Sport getOrCreateSport(String sportName) {
        return sportRepository.findByName(sportName)
                .orElseGet(() -> sportRepository.save(new Sport(sportName)));
    }

    private MatchResult createMatchResult(Integer homeGoals,
                                           Integer awayGoals,
                                           Long winnerTeamId,
                                           String message) {
        Integer safeHomeGoals = homeGoals == null ? 0 : homeGoals;
        Integer safeAwayGoals = awayGoals == null ? 0 : awayGoals;

        Team winner = winnerTeamId == null ? null : getTeamById(winnerTeamId);

        MatchResult result = new MatchResult();
        result.setHomeGoals(safeHomeGoals);
        result.setAwayGoals(safeAwayGoals);
        result.setWinnerTeam(winner);
        result.setMessage(message);
        return result;
    }
}
