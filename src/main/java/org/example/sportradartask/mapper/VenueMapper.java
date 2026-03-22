package org.example.sportradartask.mapper;

import org.example.sportradartask.dto.VenueDTO;
import org.example.sportradartask.model.Venue;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VenueMapper extends Mappable<Venue, VenueDTO> {

    @Override
    VenueDTO toDto(Venue venue);

    @Override
    List<VenueDTO> toDto(List<Venue> venues);

    @Override
    Venue toEntity(VenueDTO venueDTO);
}
