package org.example.sportradartask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sportradartask.dto.VenueDTO;
import org.example.sportradartask.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    public List<VenueDTO> getAll() {
        return venueService.findAll();
    }

    @GetMapping("/{id}")
    public VenueDTO getById(@PathVariable Long id) {
        return venueService.findById(id);
    }

    @PostMapping
    public VenueDTO create(@RequestBody @Valid VenueDTO dto) {
        return venueService.create(dto);
    }

    @PutMapping("/{id}")
    public VenueDTO update(@PathVariable Long id, @RequestBody @Valid VenueDTO dto) {
        return venueService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

