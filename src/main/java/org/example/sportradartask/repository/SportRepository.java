package org.example.sportradartask.repository;

import org.example.sportradartask.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<Sport,Long> {

    Optional<Sport> findByName(String name);

    boolean existsByName(String name);

}
