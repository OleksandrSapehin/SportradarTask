package org.example.sportradartask.controller;

import org.example.sportradartask.dto.SportDTO;
import org.example.sportradartask.service.SportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SportController.class)
class SportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SportService sportService;

    @Test
    void shouldReturnAllSports() throws Exception {
        // given
        SportDTO dto = new SportDTO( "football");
        given(sportService.findAll()).willReturn(List.of(dto));

        // when & then
        mockMvc.perform(get("/api/sports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("football"));
    }

    @Test
    void shouldReturnSportById() throws Exception {
        // given
        given(sportService.findById(1L)).willReturn(new SportDTO( "football"));

        // when & then
        mockMvc.perform(get("/api/sports/1"))
                .andExpect(status().isOk());
    }
    @Test
    void shouldReturnSportByName() throws Exception {
        // given
        given(sportService.getSportByName("football")).willReturn(new SportDTO( "football"));

        // when & then
        mockMvc.perform(get("/api/sports/by-name/football"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("football"));
    }

    @Test
    void shouldCreateSport() throws Exception {
        // given
        SportDTO dto = new SportDTO( "tennis");
        SportDTO savedDto = new SportDTO( "tennis");
        given(sportService.create(any(SportDTO.class))).willReturn(savedDto);

        // when & then
        mockMvc.perform(post("/api/sports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"tennis"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("tennis"));
    }

    @Test
    void shouldUpdateSport() throws Exception {
        // given
        SportDTO dto = new SportDTO( "basketball");
        SportDTO updatedDto = new SportDTO( "basketball");
        given(sportService.update(eq(1L), any(SportDTO.class))).willReturn(updatedDto);

        // when & then
        mockMvc.perform(put("/api/sports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"basketball"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("basketball"));
    }

    @Test
    void shouldDeleteSport() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/sports/1"))
                .andExpect(status().isNoContent());

        then(sportService).should().delete(1L);
    }
}
