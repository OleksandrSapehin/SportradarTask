package org.example.sportradartask.controller;

import org.example.sportradartask.dto.VenueDTO;
import org.example.sportradartask.service.VenueService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VenueController.class)
class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VenueService venueService;

    @Autowired
    private ObjectMapper objectMapper;

    private VenueDTO venue;

    @BeforeEach
    void setUp() {
        venue = new VenueDTO(
                "Emirates Stadium",
                "London",
                "ENG",
                60000
        );
    }

    // GET /api/venues
    @Test
    void shouldReturnAllVenues() throws Exception {
        // Given
        given(venueService.findAll()).willReturn(List.of(venue));

        // When & Then
        mockMvc.perform(get("/api/venues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Emirates Stadium"))
                .andExpect(jsonPath("$[0].city").value("London"))
                .andExpect(jsonPath("$[0].country").value("ENG"))
                .andExpect(jsonPath("$[0].capacity").value(60000));
    }

    // GET /api/venues/{id}
    @Test
    void shouldReturnVenueById() throws Exception {
        // Given
        given(venueService.findById(1L)).willReturn(venue);

        // When & Then
        mockMvc.perform(get("/api/venues/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Emirates Stadium"))
                .andExpect(jsonPath("$.capacity").value(60000));
    }

    // GET /api/venues/by-name/{name}
    @Test
    void shouldReturnVenueByName() throws Exception {
        // Given
        given(venueService.findByName("Emirates Stadium")).willReturn(Optional.of(venue));

        // When & Then
        mockMvc.perform(get("/api/venues/by-name/Emirates Stadium"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("London"));
    }

    // GET /api/venues/by-city/{city}
    @Test
    void shouldReturnVenuesByCity() throws Exception {
        // Given
        given(venueService.findByCity("London")).willReturn(List.of(venue));

        // When & Then
        mockMvc.perform(get("/api/venues/by-city/London"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("London"));
    }

    // GET /api/venues/by-country/{country}
    @Test
    void shouldReturnVenuesByCountry() throws Exception {
        // Given
        given(venueService.findByCountry("ENG")).willReturn(List.of(venue));

        // When & Then
        mockMvc.perform(get("/api/venues/by-country/ENG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country").value("ENG"));
    }

    // GET /api/venues/by-city-and-country/{city}/{country}
    @Test
    void shouldReturnVenuesByCityAndCountry() throws Exception {
        // Given
        given(venueService.findByCityAndCountry("London", "ENG")).willReturn(List.of(venue));

        // When & Then
        mockMvc.perform(get("/api/venues/by-city-and-country/London/ENG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("London"))
                .andExpect(jsonPath("$[0].country").value("ENG"));
    }

    // POST /api/venues
    @Test
    void shouldCreateVenue() throws Exception {
        // Given
        VenueDTO request = new VenueDTO( "Old Trafford", "Manchester", "ENG", 75000);
        VenueDTO saved = new VenueDTO( "Old Trafford", "Manchester", "ENG", 75000);

        given(venueService.create(any())).willReturn(saved);

        // When & Then
        mockMvc.perform(post("/api/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Old Trafford"))
                .andExpect(jsonPath("$.capacity").value(75000));
    }

    // PUT /api/venues/{id}
    @Test
    void shouldUpdateVenue() throws Exception {
        // Given
        VenueDTO request = new VenueDTO( "Emirates Stadium", "London", "ENG", 61000);
        VenueDTO updated = new VenueDTO( "Emirates Stadium", "London", "ENG", 61000);

        given(venueService.update(eq(1L), any())).willReturn(updated);

        // When & Then
        mockMvc.perform(put("/api/venues/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacity").value(61000));
    }

    // DELETE /api/venues/{id}
    @Test
    void shouldDeleteVenue() throws Exception {
        // Given
        willDoNothing().given(venueService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/venues/1"))
                .andExpect(status().isNoContent());

        then(venueService).should().delete(1L);
    }
}
