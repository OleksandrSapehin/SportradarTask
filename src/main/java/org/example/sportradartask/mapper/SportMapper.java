package org.example.sportradartask.mapper;

import org.example.sportradartask.dto.SportDTO;
import org.example.sportradartask.model.Sport;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SportMapper extends Mappable<Sport, SportDTO> {

    @Override
    SportDTO toDto(Sport sport);

    @Override
    List<SportDTO> toDto(List<Sport> sports);

    @Override
    Sport toEntity(SportDTO sportDTO);
}
