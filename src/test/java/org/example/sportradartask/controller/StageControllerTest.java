package org.example.sportradartask.controller;

import org.example.sportradartask.dto.StageDTO;
import org.example.sportradartask.service.StageService;
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

@WebMvcTest(StageController.class)
class StageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StageService stageService;

    @Autowired
    private ObjectMapper objectMapper;

    private StageDTO stage;

    @BeforeEach
    void setUp() {
        stage = new StageDTO( "Group Stage", 1);
    }

    // GET /api/stages
    @Test
    void shouldReturnAllStages() throws Exception {
        // Given
        given(stageService.findAllOrdered()).willReturn(List.of(stage));

        // When & Then
        mockMvc.perform(get("/api/stages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Group Stage"))
                .andExpect(jsonPath("$[0].ordering").value(1));
    }

    // GET /api/stages/{id}
    @Test
    void shouldReturnStageById() throws Exception {
        // Given
        given(stageService.findById(1L)).willReturn(stage);

        // When & Then
        mockMvc.perform(get("/api/stages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Group Stage"))
                .andExpect(jsonPath("$.ordering").value(1));
    }

    // GET /api/stages/by-name/{name}
    @Test
    void shouldReturnStageByName() throws Exception {
        // Given
        given(stageService.findByName("Group Stage")).willReturn(Optional.of(stage));

        // When & Then
        mockMvc.perform(get("/api/stages/by-name/Group Stage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Group Stage"))
                .andExpect(jsonPath("$.ordering").value(1));
    }
    // Validation
    @Test
    void shouldReturn404WhenStageByNameNotFound() throws Exception {
        // Given
        given(stageService.findByName("Knockout")).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/stages/by-name/Knockout"))
                .andExpect(status().isNotFound());
    }

    // POST /api/stages
    @Test
    void shouldCreateStage() throws Exception {
        // Given
        StageDTO request = new StageDTO( "Knockout", 2);
        StageDTO saved = new StageDTO( "Knockout", 2);
        given(stageService.create(any(StageDTO.class))).willReturn(saved);

        // When & Then
        mockMvc.perform(post("/api/stages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Knockout"))
                .andExpect(jsonPath("$.ordering").value(2));
    }

    // PUT /api/stages/{id}
    @Test
    void shouldUpdateStage() throws Exception {
        // Given
        StageDTO request = new StageDTO("Quarterfinals", 3);
        StageDTO updated = new StageDTO("Quarterfinals", 3);
        given(stageService.update(eq(1L), any(StageDTO.class))).willReturn(updated);

        // When & Then
        mockMvc.perform(put("/api/stages/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Quarterfinals"))
                .andExpect(jsonPath("$.ordering").value(3));
    }

    // DELETE /api/stages/{id}
    @Test
    void shouldDeleteStage() throws Exception {
        // Given
        willDoNothing().given(stageService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/stages/1"))
                .andExpect(status().isNoContent());

        // Then
        then(stageService).should().delete(1L);
    }
}