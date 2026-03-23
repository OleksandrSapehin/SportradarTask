package org.example.sportradartask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sportradartask.exeptions.NotFoundException;
import org.example.sportradartask.dto.StageDTO;
import org.example.sportradartask.service.StageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stages")
public class StageController {

    private final StageService stageService;

    @GetMapping
    public List<StageDTO> getAllOrdered() {
        return stageService.findAllOrdered();
    }

    @GetMapping("/{id}")
    public StageDTO getById(@PathVariable Long id) {
        return stageService.findById(id);
    }

    @GetMapping("/by-name/{name}")
    public StageDTO getByName(@PathVariable String name) {
        return stageService.findByName(name).orElseThrow(() -> new NotFoundException("Stage not found: " + name));
    }

    @PostMapping
    public StageDTO create(@RequestBody @Valid StageDTO dto) {
        return stageService.create(dto);
    }

    @PutMapping("/{id}")
    public StageDTO update(@PathVariable Long id, @RequestBody @Valid StageDTO dto) {
        return stageService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

