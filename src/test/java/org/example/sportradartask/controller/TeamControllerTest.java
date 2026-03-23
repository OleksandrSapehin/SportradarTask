package org.example.sportradartask.controller;

import org.example.sportradartask.dto.TeamDTO;
import org.example.sportradartask.dto.TeamRequestDTO;
import org.example.sportradartask.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    private TeamDTO team;

    @BeforeEach
    void setUp() {
        team = new TeamDTO(
                "Arsenal",
                "Arsenal FC",
                "arsenal",
                "ARS",
                "ENG",
                List.of(),
                List.of(),
                List.of()
        );
    }

    // GET /api/teams
    @Test
    void shouldReturnAllTeams() throws Exception {
        // Given
        given(teamService.findAll()).willReturn(List.of(team));

        // When & Then
        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Arsenal"))
                .andExpect(jsonPath("$[0].officialName").value("Arsenal FC"))
                .andExpect(jsonPath("$[0].slug").value("arsenal"))
                .andExpect(jsonPath("$[0].abbreviation").value("ARS"))
                .andExpect(jsonPath("$[0].countryCode").value("ENG"));
    }

    // GET /api/teams/{id}
    @Test
    void shouldReturnTeamById() throws Exception {
        // Given
        given(teamService.findById(1L)).willReturn(team);

        // When & Then
        mockMvc.perform(get("/api/teams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.officialName").value("Arsenal FC"));
    }

    // GET /api/teams/by-slug/{slug}
    @Test
    void shouldReturnTeamBySlug() throws Exception {
        // Given
        given(teamService.getTeamBySlug("arsenal")).willReturn(Optional.of(team));

        // When & Then
        mockMvc.perform(get("/api/teams/by-slug/arsenal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Arsenal"));
    }

    @Test
    void shouldReturn404WhenTeamBySlugNotFound() throws Exception {
        // Given
        given(teamService.getTeamBySlug("chelsea")).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/teams/by-slug/chelsea"))
                .andExpect(status().isNotFound());
    }

    // GET /api/teams/by-country-code/{countryCode}
    @Test
    void shouldReturnTeamsByCountryCode() throws Exception {
        // Given
        given(teamService.findByCountryCode("ENG")).willReturn(List.of(team));

        // When & Then
        mockMvc.perform(get("/api/teams/by-country-code/ENG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].countryCode").value("ENG"));
    }

    // GET /api/teams/search?keyword=
    @Test
    void shouldReturnTeamsByKeyword() throws Exception {
        // Given
        given(teamService.searchByKeyword("Arsenal")).willReturn(List.of(team));

        // When & Then
        mockMvc.perform(get("/api/teams/search")
                        .param("keyword", "Arsenal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Arsenal"));
    }

    // POST /api/teams/create
    @Test
    void shouldCreateTeamUsingCreateTeamRequest() throws Exception {
        // Given
        TeamRequestDTO request = new TeamRequestDTO("Chelsea", "chelsea", "CHE", "ENG");
        TeamDTO saved = new TeamDTO(
                "Chelsea",
                "Chelsea FC",
                "chelsea",
                "CHE",
                "ENG",
                List.of(),
                List.of(),
                List.of()
        );
        given(teamService.createTeam(any(), any(), any(), any())).willReturn(saved);

        // When & Then
        mockMvc.perform(post("/api/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chelsea"))
                .andExpect(jsonPath("$.officialName").value("Chelsea FC"));
    }

    // POST /api/teams
    @Test
    void shouldCreateTeamUsingDTO() throws Exception {
        // Given
        TeamDTO request = new TeamDTO(
                "Liverpool",
                "Liverpool FC",
                "liverpool",
                "LIV",
                "ENG",
                List.of(),
                List.of(),
                List.of()
        );
        TeamDTO saved = new TeamDTO(
                "Liverpool",
                "Liverpool FC",
                "liverpool",
                "LIV",
                "ENG",
                List.of(),
                List.of(),
                List.of()
        );
        given(teamService.create(any())).willReturn(saved);

        // When & Then
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.officialName").value("Liverpool FC"));
    }

    // PUT /api/teams/{id}
    @Test
    void shouldUpdateTeam() throws Exception {
        // Given
        TeamDTO request = new TeamDTO(
                "Manchester United",
                "Manchester United FC",
                "manutd",
                "MNU",
                "ENG",
                List.of(),
                List.of(),
                List.of()
        );
        TeamDTO updated = new TeamDTO(
                "Manchester United",
                "Manchester United FC",
                "manutd",
                "MNU",
                "ENG",
                List.of(),
                List.of(),
                List.of()
        );
        given(teamService.update(eq(1L), any())).willReturn(updated);

        // When & Then
        mockMvc.perform(put("/api/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.officialName").value("Manchester United FC"));
    }

    // DELETE /api/teams/{id}
    @Test
    void shouldDeleteTeam() throws Exception {
        // Given
        willDoNothing().given(teamService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/teams/1"))
                .andExpect(status().isNoContent());

        // Then
        then(teamService).should().delete(1L);
    }
}
