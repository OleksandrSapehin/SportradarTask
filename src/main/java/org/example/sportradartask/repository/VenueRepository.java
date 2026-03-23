package org.example.sportradartask.repository;

import org.example.sportradartask.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue,Long> {

    Optional<Venue> findByName(String name);

    List<Venue> findByCity(String city);

    List<Venue> findByCountry(String country);

    List<Venue> findByCityAndCountry(String city, String country);
}
