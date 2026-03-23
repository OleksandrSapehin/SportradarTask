package org.example.sportradartask.service;

import org.example.sportradartask.dto.SportDTO;
import org.example.sportradartask.mapper.SportMapper;
import org.example.sportradartask.model.Sport;
import org.example.sportradartask.repository.SportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SportServiceTest {

    @Mock
    private SportRepository sportRepository;
    @Mock
    private SportMapper sportMapper;

    private SportService sportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sportService = new SportService(sportRepository, sportMapper);
    }

    @Test
    void testCreateSportThrowsWhenSportAlreadyExists() {
        // Given
        String name = "Football";
        when(sportRepository.existsByName(eq(name))).thenReturn(true);

        // Then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sportService.createSport(name));
        assertTrue(ex.getMessage().contains("already exists"));

        // Then
        verify(sportRepository).existsByName(eq(name));
        verifyNoInteractions(sportMapper);
    }

    @Test
    void testCreateSportCreatesAndMapsWhenMissing() {
        // Given
        String name = "Football";

        Sport saved = new Sport();
        saved.setId(1L);
        saved.setName(name);

        SportDTO mapped = new SportDTO( name);

        when(sportRepository.existsByName(eq(name))).thenReturn(false);
        when(sportRepository.save(any(Sport.class))).thenReturn(saved);
        when(sportMapper.toDto(eq(saved))).thenReturn(mapped);

        // When
        SportDTO result = sportService.createSport(name);

        // Then
        assertEquals(mapped, result);
        verify(sportRepository).save(any(Sport.class));
        verify(sportMapper).toDto(eq(saved));
    }

    @Test
    void testFindByNameReturnsOptionalMappedDto() {
        // Given
        String name = "Tennis";
        Sport sport = new Sport();
        sport.setId(2L);
        sport.setName(name);

        SportDTO dto = new SportDTO( name);

        when(sportRepository.findByName(eq(name))).thenReturn(Optional.of(sport));
        when(sportMapper.toDto(eq(sport))).thenReturn(dto);

        // When
        Optional<SportDTO> result = sportService.findByName(name);

        // Then
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(sportRepository).findByName(eq(name));
    }

    @Test
    void testGetSportByNameThrowsWhenMissing() {
        // Given
        String name = "NoSuchSport";
        when(sportRepository.findByName(eq(name))).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sportService.getSportByName(name));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void testGetSportByNameReturnsMappedDto() {
        // Given
        String name = "Basketball";
        Sport sport = new Sport();
        sport.setId(9L);
        sport.setName(name);

        SportDTO dto = new SportDTO(name);

        when(sportRepository.findByName(eq(name))).thenReturn(Optional.of(sport));
        when(sportMapper.toDto(eq(sport))).thenReturn(dto);

        // When
        SportDTO result = sportService.getSportByName(name);

        // Then
        assertEquals(dto, result);
        verify(sportRepository).findByName(eq(name));
        verify(sportMapper).toDto(eq(sport));
    }

    @Test
    void testExistsByNameDelegatesToRepository() {
        // Given
        String name = "Football";
        when(sportRepository.existsByName(eq(name))).thenReturn(false);

        // When
        boolean result = sportService.existsByName(name);

        // Then
        assertEquals(false, result);
        verify(sportRepository).existsByName(eq(name));
    }
}

