package org.example.sportradartask.service;

import org.example.sportradartask.dto.StageDTO;
import org.example.sportradartask.mapper.StageMapper;
import org.example.sportradartask.model.Stage;
import org.example.sportradartask.repository.StageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class StageServiceTest {

    @Mock
    private StageRepository stageRepository;
    @Mock
    private StageMapper stageMapper;

    private StageService stageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stageService = new StageService(stageRepository, stageMapper);
    }

    @Test
    void testFindByNameReturnsOptionalMappedDto() {
        // Given
        String name = "Final";
        Stage entity = new Stage();
        entity.setId(1L);
        entity.setName(name);

        StageDTO dto = new StageDTO(name, 3);

        when(stageRepository.findByName(eq(name))).thenReturn(Optional.of(entity));
        when(stageMapper.toDto(eq(entity))).thenReturn(dto);

        // When
        Optional<StageDTO> result = stageService.findByName(name);

        // Then
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(stageRepository).findByName(eq(name));
        verify(stageMapper).toDto(eq(entity));
    }

    @Test
    void testFindAllOrderedMapsRepositoryResult() {
        // Given
        Stage a = new Stage();
        Stage b = new Stage();
        List<Stage> entities = List.of(a, b);

        List<StageDTO> mapped = List.of(
                new StageDTO( "A", 1),
                new StageDTO( "B", 2)
        );

        when(stageRepository.findByOrderByOrderingAsc()).thenReturn(entities);
        when(stageMapper.toDto(eq(entities))).thenReturn(mapped);

        // When
        List<StageDTO> result = stageService.findAllOrdered();

        // Then
        assertEquals(mapped, result);
        verify(stageRepository).findByOrderByOrderingAsc();
        verify(stageMapper).toDto(eq(entities));
    }

    @Test
    void testCreateDelegatesToBaseServiceFlow() {
        // Given
        StageDTO request = new StageDTO( "Final", 3);

        Stage entity = new Stage();
        Stage saved = new Stage();
        saved.setId(7L);
        saved.setName("Final");
        saved.setOrdering(3);

        StageDTO mapped = new StageDTO( "Final", 3);

        when(stageMapper.toEntity(eq(request))).thenReturn(entity);
        when(stageRepository.save(eq(entity))).thenReturn(saved);
        when(stageMapper.toDto(eq(saved))).thenReturn(mapped);

        // When
        StageDTO result = stageService.create(request);

        // Then
        assertEquals(mapped, result);
        verify(stageMapper).toEntity(eq(request));
        verify(stageRepository).save(eq(entity));
        verify(stageMapper).toDto(eq(saved));
    }

    @Test
    void testUpdateDelegatesToBaseServiceAndUpdatesEntity() {
        // Given
        Long id = 10L;
        Stage existing = new Stage();
        existing.setId(id);
        StageDTO request = new StageDTO( "Quarter-final", 2);

        StageDTO mapped = new StageDTO( "Quarter-final", 2);

        when(stageRepository.findById(eq(id))).thenReturn(Optional.of(existing));
        doNothing().when(stageMapper).updateEntityFromDto(eq(request), eq(existing));
        when(stageMapper.toDto(eq(existing))).thenReturn(mapped);

        // When
        StageDTO result = stageService.update(id, request);

        // Then
        assertEquals(mapped, result);
        verify(stageRepository).findById(eq(id));
        verify(stageMapper).updateEntityFromDto(eq(request), eq(existing));
        verify(stageMapper).toDto(eq(existing));
    }
}

