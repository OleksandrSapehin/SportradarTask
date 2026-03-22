package org.example.sportradartask.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "official_name", nullable = false, length = 100)
    private String officialName;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "abbreviation", nullable = false, length = 10)
    private String abbreviation;

    @Column(name = "country_code", length = 3)
    private String countryCode;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "homeTeam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> homeEvents = new ArrayList<>();

    @OneToMany(mappedBy = "awayTeam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> awayEvents = new ArrayList<>();

    @OneToMany(mappedBy = "winnerTeam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MatchResult> wonMatches = new ArrayList<>();

    public Team(Long id, String name, String officialName, String slug, String abbreviation, String countryCode, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.officialName = officialName;
        this.slug = slug;
        this.abbreviation = abbreviation;
        this.countryCode = countryCode;
        this.createdAt = createdAt;
    }
}
