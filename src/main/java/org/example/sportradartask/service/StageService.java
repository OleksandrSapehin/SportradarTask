package org.example.sportradartask.service;

import org.example.sportradartask.dto.StageDTO;
import org.example.sportradartask.mapper.StageMapper;
import org.example.sportradartask.model.Stage;
import org.example.sportradartask.repository.StageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StageService extends BaseService<Stage, StageDTO, StageRepository> {

    private final StageRepository stageRepository;

    public StageService(StageRepository stageRepository, StageMapper stageMapper) {
        super(stageRepository, stageMapper);
        this.stageRepository = stageRepository;
    }

    public Optional<StageDTO> findByName(String name) {
        return stageRepository.findByName(name)
                .map(mapper::toDto);
    }

    public List<StageDTO> findAllOrdered() {
        return mapper.toDto(stageRepository.findByOrderByOrderingAsc());
    }
}
