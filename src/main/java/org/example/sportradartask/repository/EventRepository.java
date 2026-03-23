package org.example.sportradartask.repository;

import org.example.sportradartask.model.Event;
import org.example.sportradartask.model.MatchStatus;
import org.example.sportradartask.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventDate(LocalDate date);

    List<Event> findByStatus(MatchStatus status);


    @Query("SELECT e FROM Event e WHERE e.homeTeam = :team OR e.awayTeam = :team")
    List<Event> findAllEventsByTeam(@Param("team") Team team);

    @Query("SELECT e FROM Event e WHERE e.sport.name = :sportName")
    List<Event> findBySportName(@Param("sportName") String sportName);

    @Query("SELECT e FROM Event e WHERE e.status = :status AND e.eventDate >= :date ORDER BY e.eventDate, e.eventTime")
    List<Event> findUpcomingEvents(@Param("status") MatchStatus status, @Param("date") LocalDate date);

    @Query("SELECT e FROM Event e WHERE " +
            "LOWER(e.competitionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.homeTeam.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.awayTeam.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Event> searchByKeyword(@Param("keyword") String keyword);

}
