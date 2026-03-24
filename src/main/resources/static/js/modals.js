import { closeModal } from './utils.js';
import { updateEvent, updateEventStatus, addEventResult, loadEvents } from './events.js';
import { teams } from './config.js';

let currentEditId = null;

export function showEditModal(event) {
    currentEditId = event.id;
    document.getElementById("editSport").value = event.sportName || "";
    document.getElementById("editHomeTeam").value = event.homeTeamName || "";
    document.getElementById("editAwayTeam").value = event.awayTeamName || "";
    document.getElementById("editVenue").value = event.venueName || "";
    document.getElementById("editSeason").value = event.season || "";
    document.getElementById("editCompetitionName").value = event.competitionName || "";
    document.getElementById("editDescription").value = event.description || "";

    if (event.eventDate && event.eventTime) {
        const dt = event.eventDate + "T" + event.eventTime.substring(0,5);
        document.getElementById("editDateTime").value = dt;
    } else {
        document.getElementById("editDateTime").value = "";
    }
    document.getElementById("editModal").style.display = "block";
}

export async function updateEventHandler() {
    const sport = document.getElementById("editSport").value;
    const homeTeam = document.getElementById("editHomeTeam").value;
    const awayTeam = document.getElementById("editAwayTeam").value;
    const venue = document.getElementById("editVenue").value;
    const season = parseInt(document.getElementById("editSeason").value, 10);
    const competitionName = document.getElementById("editCompetitionName").value.trim();
    const description = document.getElementById("editDescription").value.trim();
    const dateTime = document.getElementById("editDateTime").value;

    if (!sport || !homeTeam || !awayTeam || !venue || !season || !dateTime) {
        alert("Fill all required fields");
        return;
    }
    const [date, time] = dateTime.split("T");
    const body = {
        season: season,
        sportName: sport,
        homeTeamSlug: homeTeam,
        awayTeamSlug: awayTeam,
        venueName: venue,
        eventDate: date,
        eventTime: time + ":00"
    };

    if (competitionName) body.competitionName = competitionName;
    if (description) body.description = description;

    try {
        await updateEvent(currentEditId, body);
        alert("Event updated");
        closeModal("editModal");
        await loadEvents();
        await window.refreshCalendarEvents();
    } catch (err) {
        alert("Error: " + err.message);
    }
}

export function showStatusModal(id, currentStatus) {
    currentEditId = id;
    document.getElementById("statusSelect").value = currentStatus;
    document.getElementById("statusModal").style.display = "block";
}

export async function updateStatusHandler() {
    const newStatus = document.getElementById("statusSelect").value;
    try {
        await updateEventStatus(currentEditId, newStatus);
        alert("Status updated");
        closeModal("statusModal");
        await loadEvents();
        await window.refreshCalendarEvents();
    } catch (err) {
        alert("Error: " + err.message);
    }
}

export function showResultModal(id) {
    currentEditId = id;
    const teamSelect = document.getElementById("winnerTeamId");
    teamSelect.innerHTML = "<option value=''>-- winner (optional) --</option>";
    teams.forEach(team => {
        const opt = document.createElement("option");
        opt.value = team.id;
        opt.textContent = team.name;
        teamSelect.appendChild(opt);
    });
    document.getElementById("resultModal").style.display = "block";
}

export async function addResultHandler() {
    const homeGoals = parseInt(document.getElementById("homeGoals").value, 10);
    const awayGoals = parseInt(document.getElementById("awayGoals").value, 10);
    const winnerTeamId = document.getElementById("winnerTeamId").value ? parseInt(document.getElementById("winnerTeamId").value, 10) : null;
    const message = document.getElementById("resultMessage").value.trim();

    if (isNaN(homeGoals) || isNaN(awayGoals)) {
        alert("Enter valid numbers for goals");
        return;
    }
    const body = { homeGoals, awayGoals, winnerTeamId, message };
    try {
        await addEventResult(currentEditId, body);
        alert("Result added");
        closeModal("resultModal");
        await loadEvents();
        await window.refreshCalendarEvents();
    } catch (err) {
        alert("Error: " + err.message);
    }
}