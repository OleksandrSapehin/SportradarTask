import { fetchJSON } from './api.js';
import { sports, teams, venues, stages, setSports, setTeams, setVenues, setStages } from './config.js';
import { fillSelect } from './utils.js';

export async function loadAllData() {
    try {
        const [s, t, v, st] = await Promise.all([
            fetchJSON('/sports'),
            fetchJSON('/teams'),
            fetchJSON('/venues'),
            fetchJSON('/stages')
        ]);
        setSports(s);
        setTeams(t);
        setVenues(v);
        setStages(st);

        fillSelect("sport", sports, "name", "name");
        fillSelect("homeTeam", teams, "slug", "name");
        fillSelect("awayTeam", teams, "slug", "name");
        fillSelect("venue", venues, "name", "name");

        fillSelect("editSport", sports, "name", "name");
        fillSelect("editHomeTeam", teams, "slug", "name");
        fillSelect("editAwayTeam", teams, "slug", "name");
        fillSelect("editVenue", venues, "name", "name");

        const datalist = document.getElementById("sportList");
        if (datalist) {
            datalist.innerHTML = "";
            sports.forEach(sport => {
                const option = document.createElement("option");
                option.value = sport.name;
                datalist.appendChild(option);
            });
        }
    } catch (e) {
        console.error(e);
        alert("Failed to load data. Check API URL.");
    }
}

export async function createSport(name) {
    return await fetchJSON('/sports', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name })
    });
}

export async function createVenue(name, city, country, capacity) {
    return await fetchJSON('/venues', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, city, country, capacity })
    });
}

export async function createStage(name, ordering) {
    return await fetchJSON('/stages', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, ordering })
    });
}

export async function createTeam(name, slug, abbreviation, countryCode) {
    return await fetchJSON('/teams/create', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, slug, abbreviation, countryCode })
    });
}