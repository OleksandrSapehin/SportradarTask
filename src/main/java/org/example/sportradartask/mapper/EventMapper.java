package org.example.sportradartask.mapper;

import org.example.sportradartask.dto.EventRequestDTO;
import org.example.sportradartask.dto.EventResponseDTO;
import org.example.sportradartask.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {SportMapper.class, TeamMapper.class, VenueMapper.class, StageMapper.class, MatchResultMapper.class})
public interface EventMapper extends Mappable<Event, EventResponseDTO> {

    @Override
    @Mapping(target = "status", expression = "java(event.getStatus() == null ? null : event.getStatus().getValue())")
    @Mapping(target = "sportId", source = "event.sport.id")
    @Mapping(target = "sportName", source = "event.sport.name")
    @Mapping(target = "homeTeamId", source = "event.homeTeam.id")
    @Mapping(target = "homeTeamName", source = "event.homeTeam.name")
    @Mapping(target = "homeTeamAbbreviation", source = "event.homeTeam.abbreviation")
    @Mapping(target = "awayTeamId", source = "event.awayTeam.id")
    @Mapping(target = "awayTeamName", source = "event.awayTeam.name")
    @Mapping(target = "awayTeamAbbreviation", source = "event.awayTeam.abbreviation")
    @Mapping(target = "venueId", source = "event.venue.id")
    @Mapping(target = "venueName", source = "event.venue.name")
    @Mapping(target = "venueCity", source = "event.venue.city")
    @Mapping(target = "homeGoals", source = "event.result.homeGoals")
    @Mapping(target = "awayGoals", source = "event.result.awayGoals")
    @Mapping(target = "winnerTeamName", source = "event.result.winnerTeam.name")
    @Mapping(target = "resultMessage", source = "event.result.message")
    @Mapping(target = "competitionName", source = "event.competitionName")
    EventResponseDTO toDto(Event event);

    @Override
    List<EventResponseDTO> toDto(List<Event> events);

    @Override
    default Event toEntity(EventResponseDTO dto) {
        throw new UnsupportedOperationException("Use toEntity with EventRequestDTO instead");
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(org.example.sportradartask.model.MatchStatus.SCHEDULED)")
    @Mapping(target = "result", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Event toEntity(EventRequestDTO request,
                   Sport sport,
                   Team homeTeam,
                   Team awayTeam,
                   Venue venue,
                   Stage stage);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sport", ignore = true)
    @Mapping(target = "homeTeam", ignore = true)
    @Mapping(target = "awayTeam", ignore = true)
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "stage", ignore = true)
    @Mapping(target = "result", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(EventResponseDTO dto, @MappingTarget Event entity);
}
