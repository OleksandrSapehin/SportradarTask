package org.example.sportradartask.model;

public enum MatchStatus {
    PLAYED,
    SCHEDULED,
    POSTPONED,
    CANCELLED;

    public String getValue() {
        return name().toLowerCase();
    }
}
