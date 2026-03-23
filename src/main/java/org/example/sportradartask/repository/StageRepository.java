package org.example.sportradartask.repository;

import org.example.sportradartask.model.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StageRepository extends JpaRepository<Stage,Long> {

    Optional<Stage> findByName(String name);

    List<Stage> findByOrderByOrderingAsc();

}
