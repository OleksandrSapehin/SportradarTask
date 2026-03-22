package org.example.sportradartask.mapper;

import org.example.sportradartask.dto.StageDTO;
import org.example.sportradartask.model.Stage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StageMapper extends Mappable<Stage, StageDTO> {

    @Override
    StageDTO toDto(Stage stage);

    @Override
    List<StageDTO> toDto(List<Stage> stages);

    @Override
    Stage toEntity(StageDTO stageDTO);
}
