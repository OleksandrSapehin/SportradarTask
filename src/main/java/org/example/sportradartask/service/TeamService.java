package org.example.sportradartask.service;

import org.example.sportradartask.dto.TeamDTO;
import org.example.sportradartask.mapper.TeamMapper;
import org.example.sportradartask.model.Team;
import org.example.sportradartask.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TeamService extends BaseService<Team, TeamDTO, TeamRepository> {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        super(teamRepository, teamMapper);
        this.teamRepository = teamRepository;
    }

    public Optional<TeamDTO> findBySlug(String slug) {
        return teamRepository.findBySlug(slug)
                .map(mapper::toDto);
    }

    public List<TeamDTO> findByCountryCode(String countryCode) {
        return mapper.toDto(teamRepository.findByCountryCode(countryCode));
    }


    public List<TeamDTO> searchByKeyword(String keyword) {
        return mapper.toDto(teamRepository.searchByKeyword(keyword));
    }

    @Transactional
    public TeamDTO createTeam(String name, String slug, String abbreviation, String countryCode) {
        if (teamRepository.existsBySlug(slug)) {
            throw new RuntimeException("Team with slug '" + slug + "' already exists");
        }

        Team newTeam = new Team();
        newTeam.setName(name);
        newTeam.setOfficialName(name);
        newTeam.setSlug(slug);
        newTeam.setAbbreviation(abbreviation);
        newTeam.setCountryCode(countryCode);

        Team saved = teamRepository.save(newTeam);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public Optional<TeamDTO> getTeamBySlug(String slug) {
        return teamRepository.findBySlug(slug)
                .map(mapper::toDto);
    }

    public boolean existsBySlug(String slug) {
        return teamRepository.existsBySlug(slug);
    }
}
