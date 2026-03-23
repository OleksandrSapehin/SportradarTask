package org.example.sportradartask.service;

import org.example.sportradartask.dto.VenueDTO;
import org.example.sportradartask.mapper.VenueMapper;
import org.example.sportradartask.model.Venue;
import org.example.sportradartask.repository.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;
    @Mock
    private VenueMapper venueMapper;

    private VenueService venueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        venueService = new VenueService(venueRepository, venueMapper);
    }

    @Test
    void testFindByNameReturnsOptionalMappedDto() {
        // Given
        String name = "Stadium";

        Venue entity = new Venue();
        entity.setId(1L);
        entity.setName(name);

        VenueDTO dto = new VenueDTO( name, "Berlin", "DE", 75000);

        when(venueRepository.findByName(eq(name))).thenReturn(Optional.of(entity));
        when(venueMapper.toDto(eq(entity))).thenReturn(dto);

        // When
        Optional<VenueDTO> result = venueService.findByName(name);

        // Then
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(venueRepository).findByName(eq(name));
        verify(venueMapper).toDto(eq(entity));
    }

    @Test
    void testFindByCityMapsRepositoryResult() {
        // Given
        String city = "Berlin";
        Venue a = new Venue();
        Venue b = new Venue();
        List<Venue> entities = List.of(a, b);

        List<VenueDTO> mapped = List.of(
                new VenueDTO(null, city, null, null),
                new VenueDTO(null, city, null, null)
        );

        when(venueRepository.findByCity(eq(city))).thenReturn(entities);
        when(venueMapper.toDto(eq(entities))).thenReturn(mapped);

        // When
        List<VenueDTO> result = venueService.findByCity(city);

        // Then
        assertEquals(mapped, result);
        verify(venueRepository).findByCity(eq(city));
        verify(venueMapper).toDto(eq(entities));
    }

    @Test
    void testCreateDelegatesToBaseServiceFlow() {
        // Given
        VenueDTO request = new VenueDTO( "Arena", "Berlin", "DE", 50000);

        Venue entity = new Venue();
        Venue saved = new Venue();
        saved.setId(7L);

        VenueDTO mapped = new VenueDTO( "Arena", "Berlin", "DE", 50000);

        when(venueMapper.toEntity(eq(request))).thenReturn(entity);
        when(venueRepository.save(eq(entity))).thenReturn(saved);
        when(venueMapper.toDto(eq(saved))).thenReturn(mapped);

        // When
        VenueDTO result = venueService.create(request);

        // Then
        assertEquals(mapped, result);
        verify(venueMapper).toEntity(eq(request));
        verify(venueRepository).save(eq(entity));
        verify(venueMapper).toDto(eq(saved));
    }

    @Test
    void testUpdateDelegatesToBaseServiceAndUpdatesEntity() {
        // Given
        Long id = 10L;
        Venue existing = new Venue();
        existing.setId(id);

        VenueDTO request = new VenueDTO( "Updated", "Munich", "DE", 1);
        VenueDTO mapped = request;

        when(venueRepository.findById(eq(id))).thenReturn(Optional.of(existing));
        doNothing().when(venueMapper).updateEntityFromDto(eq(request), eq(existing));
        when(venueMapper.toDto(eq(existing))).thenReturn(mapped);

        // When
        VenueDTO result = venueService.update(id, request);

        // Then
        assertEquals(mapped, result);
        verify(venueRepository).findById(eq(id));
        verify(venueMapper).updateEntityFromDto(eq(request), eq(existing));
        verify(venueMapper).toDto(eq(existing));
    }
}

