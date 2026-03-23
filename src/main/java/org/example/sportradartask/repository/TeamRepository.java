package org.example.sportradartask.repository;

import org.example.sportradartask.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {

    Optional<Team> findBySlug(String slug);

    List<Team> findByCountryCode(String countryCode);

    List<Team> findByNameContainingIgnoreCase(String name);

    @Query("SELECT t FROM Team t WHERE t.name LIKE %:keyword% OR t.officialName LIKE %:keyword%")
    List<Team> searchByKeyword(@Param("keyword") String keyword);

    boolean existsBySlug(String slug);

}
