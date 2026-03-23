package org.example.sportradartask.service;

import org.example.sportradartask.dto.VenueDTO;
import org.example.sportradartask.mapper.VenueMapper;
import org.example.sportradartask.model.Venue;
import org.example.sportradartask.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VenueService extends BaseService<Venue, VenueDTO, VenueRepository> {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository, VenueMapper venueMapper) {
        super(venueRepository, venueMapper);
        this.venueRepository = venueRepository;
    }

    public Optional<VenueDTO> findByName(String name) {
        return venueRepository.findByName(name)
                .map(mapper::toDto);
    }

    public List<VenueDTO> findByCity(String city) {
        return mapper.toDto(venueRepository.findByCity(city));
    }

    public List<VenueDTO> findByCountry(String country) {
        return mapper.toDto(venueRepository.findByCountry(country));
    }

    public List<VenueDTO> findByCityAndCountry(String city, String country) {
        return mapper.toDto(venueRepository.findByCityAndCountry(city, country));
    }
}
