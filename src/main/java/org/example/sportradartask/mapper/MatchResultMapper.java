package org.example.sportradartask.mapper;

import org.example.sportradartask.dto.MatchResultDTO;
import org.example.sportradartask.model.MatchResult;
import org.example.sportradartask.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchResultMapper extends Mappable<MatchResult, MatchResultDTO> {

    @Override
    @Mapping(target = "winnerTeamId", source = "winnerTeam.id")
    @Mapping(target = "winnerTeamName", source = "winnerTeam.name")
    MatchResultDTO toDto(MatchResult result);

    @Override
    List<MatchResultDTO> toDto(List<MatchResult> results);

    @Override
    default MatchResult toEntity(MatchResultDTO dto) {
        throw new UnsupportedOperationException("Use toEntity with parameters instead");
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "winnerTeam", source = "winnerTeam")
    MatchResult toEntity(Integer homeGoals,
                         Integer awayGoals,
                         Team winnerTeam,
                         String message);
}
