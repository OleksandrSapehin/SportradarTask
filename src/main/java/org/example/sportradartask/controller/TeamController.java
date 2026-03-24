package org.example.sportradartask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sportradartask.dto.TeamRequestDTO;
import org.example.sportradartask.exceptions.NotFoundException;
import org.example.sportradartask.dto.TeamDTO;
import org.example.sportradartask.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public List<TeamDTO> getAll() {
        return teamService.findAll();
    }

    @GetMapping("/{id}")
    public TeamDTO getById(@PathVariable Long id) {
        return teamService.findById(id);
    }

    @GetMapping("/by-slug/{slug}")
    public TeamDTO getBySlug(@PathVariable String slug) {
        return teamService.getTeamBySlug(slug).orElseThrow(() -> new NotFoundException("Team not found: " + slug));
    }

    @GetMapping("/by-country-code/{countryCode}")
    public List<TeamDTO> getByCountryCode(@PathVariable String countryCode) {
        return teamService.findByCountryCode(countryCode);
    }

    @GetMapping("/search")
    public List<TeamDTO> search(@RequestParam String keyword) {
        return teamService.searchByKeyword(keyword);
    }

    @PostMapping("/create")
    public TeamDTO createTeam(@RequestBody @Valid TeamRequestDTO request) {
        return teamService.createTeam(
                request.name(),
                request.slug(),
                request.abbreviation(),
                request.countryCode()
        );
    }

    @PutMapping("/{id}")
    public TeamDTO update(@PathVariable Long id, @RequestBody @Valid TeamDTO dto) {
        return teamService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

