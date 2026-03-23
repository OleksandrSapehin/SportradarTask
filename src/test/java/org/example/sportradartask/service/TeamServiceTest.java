package org.example.sportradartask.service;

import org.example.sportradartask.dto.TeamDTO;
import org.example.sportradartask.mapper.TeamMapper;
import org.example.sportradartask.model.Team;
import org.example.sportradartask.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;
    @Mock
    private TeamMapper teamMapper;

    private TeamService teamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        teamService = new TeamService(teamRepository, teamMapper);
    }

    @Test
    void testCreateTeamThrowsWhenSlugAlreadyExists() {
        // Given
        String slug = "home-slug";
        when(teamRepository.existsBySlug(eq(slug))).thenReturn(true);

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> teamService.createTeam("Home", slug, "H", "DE"));
        assertTrue(ex.getMessage().contains("already exists"));

        // Then
        verify(teamRepository).existsBySlug(eq(slug));
        verifyNoInteractions(teamMapper);
    }

    @Test
    void testCreateTeamCreatesAndMapsWhenMissing() {
        // Given
        String name = "Home";
        String slug = "home-slug";
        String abbreviation = "H";
        String countryCode = "DE";

        when(teamRepository.existsBySlug(eq(slug))).thenReturn(false);

        Team saved = new Team();
        saved.setId(10L);
        saved.setName(name);
        saved.setSlug(slug);
        saved.setAbbreviation(abbreviation);
        saved.setCountryCode(countryCode);

        TeamDTO mapped = new TeamDTO(saved.getName(), saved.getName(), saved.getSlug(),
                saved.getAbbreviation(), saved.getCountryCode(), null, null, null);

        when(teamRepository.save(any(Team.class))).thenReturn(saved);
        when(teamMapper.toDto(eq(saved))).thenReturn(mapped);

        // When
        TeamDTO result = teamService.createTeam(name, slug, abbreviation, countryCode);

        // Then
        assertEquals(mapped, result);
        verify(teamRepository).save(any(Team.class));
        verify(teamMapper).toDto(eq(saved));
    }

    @Test
    void testFindBySlugReturnsOptionalMappedDto() {
        // Given
        String slug = "home-slug";
        Team entity = new Team();
        entity.setId(1L);
        entity.setSlug(slug);

        TeamDTO dto = new TeamDTO( null, null, slug, null, null, null, null, null);

        when(teamRepository.findBySlug(eq(slug))).thenReturn(Optional.of(entity));
        when(teamMapper.toDto(eq(entity))).thenReturn(dto);

        // When
        Optional<TeamDTO> result = teamService.findBySlug(slug);

        // Then
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(teamRepository).findBySlug(eq(slug));
        verify(teamMapper).toDto(eq(entity));
    }

    @Test
    void testGetTeamBySlugReturnsOptionalMappedDto() {
        // Given
        String slug = "away-slug";
        Team entity = new Team();
        entity.setId(2L);
        entity.setSlug(slug);

        TeamDTO dto = new TeamDTO( null, null, slug, null, null, null, null, null);

        when(teamRepository.findBySlug(eq(slug))).thenReturn(Optional.of(entity));
        when(teamMapper.toDto(eq(entity))).thenReturn(dto);

        // When
        Optional<TeamDTO> result = teamService.getTeamBySlug(slug);

        // Then
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(teamRepository).findBySlug(eq(slug));
    }

    @Test
    void testSearchByKeywordUsesRepositoryAndMapsList() {
        // Given
        String keyword = "club";
        Team a = new Team();
        Team b = new Team();

        List<Team> entities = List.of(a, b);
        List<TeamDTO> mapped = List.of(
                new TeamDTO( null, null, null, null, null, null, null, null),
                new TeamDTO( null, null, null, null, null, null, null, null)
        );

        when(teamRepository.searchByKeyword(eq(keyword))).thenReturn(entities);
        when(teamMapper.toDto(eq(entities))).thenReturn(mapped);

        // When
        List<TeamDTO> result = teamService.searchByKeyword(keyword);

        // Then
        assertEquals(mapped, result);
        verify(teamRepository).searchByKeyword(eq(keyword));
        verify(teamMapper).toDto(eq(entities));
    }

    @Test
    void testExistsBySlugDelegatesToRepository() {
        // Given
        String slug = "x";
        when(teamRepository.existsBySlug(eq(slug))).thenReturn(true);

        // When
        boolean result = teamService.existsBySlug(slug);

        // Then
        assertTrue(result);
        verify(teamRepository).existsBySlug(eq(slug));
    }
}

