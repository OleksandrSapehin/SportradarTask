package org.example.sportradartask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sportradartask.dto.AddResultRequestDTO;
import org.example.sportradartask.dto.EventFilterDTO;
import org.example.sportradartask.dto.EventRequestDTO;
import org.example.sportradartask.dto.EventResponseDTO;
import org.example.sportradartask.model.MatchStatus;
import org.example.sportradartask.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public EventResponseDTO createEvent(@RequestBody @Valid EventRequestDTO request) {
        return eventService.createEvent(request);
    }

    @GetMapping
    public List<EventResponseDTO> getEvents() {
        return eventService.getEvents();
    }

    @PutMapping("/{id}")
    public EventResponseDTO updateEvent(@PathVariable Long id, @RequestBody @Valid EventRequestDTO request) {
        return eventService.updateEvent(id, request);
    }

    @GetMapping("/{id}")
    public EventResponseDTO getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/upcoming")
    public List<EventResponseDTO> getUpcomingEvents() {
        return eventService.getUpcomingEvents();
    }

    @GetMapping("/search")
    public List<EventResponseDTO> searchEvents(@RequestParam String keyword) {
        return eventService.searchEvents(keyword);
    }

    @GetMapping("/by-team/{teamId}")
    public List<EventResponseDTO> getEventsByTeam(@PathVariable Long teamId) {
        return eventService.findEventsByTeam(teamId);
    }

    @GetMapping("/by-sport/{sportName}")
    public List<EventResponseDTO> getEventsBySport(@PathVariable String sportName) {
        return eventService.findEventsBySport(sportName);
    }

    @GetMapping("/by-status/{status}")
    public List<EventResponseDTO> getEventsByStatus(@PathVariable  MatchStatus status) {
        return eventService.findEventsByStatus(status);
    }

    @GetMapping("/by-date/{date}")
    public List<EventResponseDTO> getEventsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return eventService.findEventsByDate(date);
    }

    @PatchMapping("/{id}/status")
    public EventResponseDTO updateStatus(@PathVariable Long id, @RequestParam MatchStatus status) {
        return eventService.updateEventStatus(id, status);
    }

    @PutMapping("/{id}/result")
    public EventResponseDTO addResultToEvent(@PathVariable("id") Long eventId, @RequestBody @Valid AddResultRequestDTO request) {
        return eventService.addResultToEvent(
                eventId,
                request.homeGoals(),
                request.awayGoals(),
                request.winnerTeamId(),
                request.message()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}

