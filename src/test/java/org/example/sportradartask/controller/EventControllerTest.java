package org.example.sportradartask.controller;

import org.example.sportradartask.dto.AddResultRequestDTO;
import org.example.sportradartask.dto.EventRequestDTO;
import org.example.sportradartask.dto.EventResponseDTO;
import org.example.sportradartask.model.MatchStatus;
import org.example.sportradartask.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.example.sportradartask.model.MatchStatus.SCHEDULED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@Import(RestExceptionHandler.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private EventResponseDTO response;

    @BeforeEach
    void setUp() {
        response = new EventResponseDTO(
                1L,
                2025,
                "SCHEDULED",
                LocalDate.of(2026, 3, 23),
                LocalTime.of(18, 0),

                1L,
                "football",

                10L,
                "Arsenal",
                "ARS",

                20L,
                "Chelsea",
                "CHE",

                100L,
                "Emirates Stadium",
                "London",

                null,
                null,
                null,
                null,

                "Premier League"
        );
    }

    // GET /api/events
    @Test
    void shouldReturnAllEvents() throws Exception {
        // given
        EventResponseDTO event = new EventResponseDTO(
                1L,
                2026,
                "SCHEDULED",
                LocalDate.of(2026, 3, 23),
                LocalTime.of(20, 0),
                1L,
                "football",
                10L,
                "Arsenal",
                "ARS",
                20L,
                "Chelsea",
                "CHE",
                100L,
                "Emirates Stadium",
                "London",
                null,
                null,
                null,
                null,
                "Premier League"
        );

        given(eventService.getEvents()).willReturn(List.of(event));

        // when & then
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sportName").value("football"))
                .andExpect(jsonPath("$[0].homeTeamName").value("Arsenal"))
                .andExpect(jsonPath("$[0].awayTeamName").value("Chelsea"))
                .andExpect(jsonPath("$[0].competitionName").value("Premier League"));
    }

    // GET BY ID
    @Test
    void shouldReturnEventById() throws Exception {
        // given
        given(eventService.getEventById(1L)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homeTeamName").value("Arsenal"));
    }

    // GET /api/events/upcoming
    @Test
    void shouldReturnUpcomingEvents() throws Exception {
        // given
        given(eventService.getUpcomingEvents()).willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/events/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].homeTeamName").value("Arsenal"));
    }

    // GET /api/events/search?keyword=
    @Test
    void shouldReturnEventsByKeyword() throws Exception {
        // given
        given(eventService.searchEvents("Arsenal")).willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/events/search")
                        .param("keyword", "Arsenal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].homeTeamName").value("Arsenal"));
    }

    @Test
    void shouldUpdateEventStatus() throws Exception {
        // given
        MatchStatus newStatus = SCHEDULED;
        EventResponseDTO updatedResponse = new EventResponseDTO(
                1L,
                2025,
                "SCHEDULED",
                LocalDate.of(2026, 3, 23),
                LocalTime.of(18, 0),
                1L,
                "football",
                10L,
                "Arsenal",
                "ARS",
                20L,
                "Chelsea",
                "CHE",
                100L,
                "Emirates Stadium",
                "London",
                null,
                null,
                null,
                null,
                "Premier League"
        );

        given(eventService.updateEventStatus(1L, newStatus)).willReturn(updatedResponse);

        // when & then
        mockMvc.perform(patch("/api/events/1/status")
                        .param("status", newStatus.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.homeTeamName").value("Arsenal"))
                .andExpect(jsonPath("$.awayTeamName").value("Chelsea"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    // VALIDATION TEST
    @Test
    void shouldReturn400_whenInvalidRequest() throws Exception {
        EventRequestDTO request = new EventRequestDTO(
                null, // season
                null, //  date
                null, //  time
                "",   //  sport
                "",   //  home
                "",   //  away
                null,
                null,
                null,
                null
        );

        // when & then
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    // CREATE
    @Test
    void shouldCreateEvent() throws Exception {
        // given
        EventRequestDTO request = new EventRequestDTO(
                2025,
                LocalDate.now().plusDays(1),
                LocalTime.of(18, 0),
                "football",
                "arsenal",
                "chelsea",
                "Emirates Stadium",
                "Final",
                "Premier League",
                "Top match"
        );

        given(eventService.createEvent(any())).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.competitionName").value("Premier League"));
    }

    // UPDATE
    @Test
    void shouldUpdateEvent() throws Exception {
        // given
        EventRequestDTO request = new EventRequestDTO(
                2025,
                LocalDate.now().plusDays(2),
                LocalTime.of(20, 0),
                "football",
                "arsenal",
                "chelsea",
                "Emirates Stadium",
                "Final",
                "Premier League",
                "Updated"
        );

        given(eventService.updateEvent(eq(1L), any())).willReturn(response);

        // when & then
        mockMvc.perform(put("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void shouldReturnEventsByTeam() throws Exception {
        // given
        given(eventService.findEventsByTeam(10L)).willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/events/by-team/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].homeTeamName").value("Arsenal"));
    }

    @Test
    void shouldReturnEventsBySport() throws Exception {
        // given
        given(eventService.findEventsBySport("football")).willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/events/by-sport/football"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sportName").value("football"));
    }

    @Test
    void shouldReturnEventsByStatus() throws Exception {
        // given
        given(eventService.findEventsByStatus(SCHEDULED)).willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/events/by-status/SCHEDULED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"));
    }

    @Test
    void shouldReturnEventsByDate() throws Exception {
        // given
        LocalDate date = LocalDate.of(2026, 3, 23);
        given(eventService.findEventsByDate(date)).willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/events/by-date/2026-03-23"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventDate").value("2026-03-23"))
                .andExpect(jsonPath("$[0].sportName").value("football"))
                .andExpect(jsonPath("$[0].homeTeamName").value("Arsenal"));
    }

    // ADD RESULT
    @Test
    void shouldAddResult() throws Exception {
        // given
        AddResultRequestDTO request = new AddResultRequestDTO(2, 1, 10L, "Great match");

        given(eventService.addResultToEvent(eq(1L), any(), any(), any(), any()))
                .willReturn(response);

        // when & then
        mockMvc.perform(put("/api/events/1/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // DELETE
    @Test
    void shouldDeleteEvent() throws Exception {
        // given
        willDoNothing().given(eventService).deleteEvent(1L);

        // when & then
        mockMvc.perform(delete("/api/events/1"))
                .andExpect(status().isNoContent());
    }
}