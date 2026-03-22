package org.example.sportradartask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "season", nullable = false)
    private Integer season;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MatchStatus status;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_time", nullable = false)
    private LocalTime eventTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foreignkey_sport_id", nullable = false)
    private Sport sport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foreignkey_home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foreignkey_away_team_id", nullable = false)
    private Team awayTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foreignkey_venue_id")
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "foreignkey_stage_id")
    private Stage stage;

    @OneToOne
    @JoinColumn(name = "foreignkey_result_id")
    private MatchResult result;

    @Column(name = "competition_name", length = 100)
    private String competitionName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Event(Long id, Integer season, MatchStatus status, LocalDate eventDate, LocalTime eventTime, Sport sport, Team homeTeam, Team awayTeam, Venue venue, Stage stage, MatchResult result, String competitionName, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.season = season;
        this.status = status;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.sport = sport;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.venue = venue;
        this.stage = stage;
        this.result = result;
        this.competitionName = competitionName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
