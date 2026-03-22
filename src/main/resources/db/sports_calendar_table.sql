CREATE TYPE match_status AS ENUM ('played', 'scheduled', 'postponed', 'cancelled');

CREATE TABLE sports (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(50) NOT NULL UNIQUE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE teams (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       official_name VARCHAR(100) NOT NULL,
                       slug VARCHAR(100) UNIQUE NOT NULL,
                       abbreviation VARCHAR(10) NOT NULL,
                       country_code CHAR(3),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE venues (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        city VARCHAR(100) NOT NULL,
                        country VARCHAR(100) NOT NULL,
                        capacity INTEGER,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stages (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        ordering INTEGER,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE match_results (
                               id SERIAL PRIMARY KEY,
                               home_goals INTEGER DEFAULT 0,
                               away_goals INTEGER DEFAULT 0,
                               foreignkey_winner_team_id INTEGER REFERENCES teams(id) ON DELETE SET NULL,
                               message TEXT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events  (
                        id SERIAL PRIMARY KEY,
                        season INTEGER NOT NULL,
                        status match_status NOT NULL DEFAULT 'scheduled',
                        event_date DATE NOT NULL,
                        event_time TIME NOT NULL,
                        foreignkey_sport_id INTEGER REFERENCES sports(id) ON DELETE CASCADE NOT NULL,
                        foreignkey_home_team_id INTEGER REFERENCES teams(id) ON DELETE CASCADE NOT NULL,
                        foreignkey_away_team_id INTEGER REFERENCES teams(id) ON DELETE CASCADE NOT NULL,
                        foreignkey_venue_id INTEGER REFERENCES venues(id) ON DELETE SET NULL,
                        foreignkey_stage_id INTEGER REFERENCES stages(id) ON DELETE SET NULL,
                        foreignkey_result_id INTEGER UNIQUE REFERENCES match_results(id) ON DELETE SET NULL,
                        competition_name VARCHAR(100),
                        description TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);