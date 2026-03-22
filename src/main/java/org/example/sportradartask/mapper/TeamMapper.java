package org.example.sportradartask.mapper;

import org.example.sportradartask.dto.TeamDTO;
import org.example.sportradartask.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EventMapper.class, MatchResultMapper.class})
public interface TeamMapper extends Mappable<Team, TeamDTO> {
    @Override
    @Mapping(target = "homeEvents", source = "homeEvents")
    @Mapping(target = "awayEvents", source = "awayEvents")
    @Mapping(target = "wonMatches", source = "wonMatches")
    TeamDTO toDto(Team team);

    @Override
    List<TeamDTO> toDto(List<Team> teams);

    @Override
    @Mapping(target = "homeEvents", ignore = true)
    @Mapping(target = "awayEvents", ignore = true)
    @Mapping(target = "wonMatches", ignore = true)
    Team toEntity(TeamDTO teamDTO);
}
