package org.example.sportradartask.service;

import org.example.sportradartask.dto.SportDTO;
import org.example.sportradartask.mapper.SportMapper;
import org.example.sportradartask.model.Sport;
import org.example.sportradartask.repository.SportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SportService extends BaseService<Sport, SportDTO, SportRepository> {

    private final SportRepository sportRepository;

    public SportService(SportRepository sportRepository, SportMapper sportMapper) {
        super(sportRepository, sportMapper);
        this.sportRepository = sportRepository;
    }

    public Optional<SportDTO> findByName(String name) {
        return sportRepository.findByName(name)
                .map(mapper::toDto);
    }

    @Transactional
    public SportDTO createSport(String name) {
        if (sportRepository.existsByName(name)) {
            throw new RuntimeException("Sport with name '" + name + "' already exists");
        }

        Sport newSport = new Sport();
        newSport.setName(name);
        Sport saved = sportRepository.save(newSport);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public SportDTO getSportByName(String name) {
        return sportRepository.findByName(name)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Sport not found with name: " + name));
    }

    public boolean existsByName(String name) {
        return sportRepository.existsByName(name);
    }
}
