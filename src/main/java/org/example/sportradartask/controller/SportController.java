package org.example.sportradartask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sportradartask.dto.SportDTO;
import org.example.sportradartask.service.SportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sports")
public class SportController {

    private final SportService sportService;

    @GetMapping
    public List<SportDTO> getAll() {
        return sportService.findAll();
    }

    @GetMapping("/{id}")
    public SportDTO getById(@PathVariable Long id) {
        return sportService.findById(id);
    }

    @GetMapping("/by-name/{name}")
    public SportDTO getByName(@PathVariable String name) {
        return sportService.getSportByName(name);
    }

    @PostMapping
    public SportDTO create(@RequestBody @Valid SportDTO dto) {
        return sportService.create(dto);
    }

    @PutMapping("/{id}")
    public SportDTO update(@PathVariable Long id, @RequestBody @Valid SportDTO dto) {
        return sportService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

