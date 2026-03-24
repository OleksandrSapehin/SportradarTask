import { fetchJSON } from './api.js';
import { sports, teams, venues, stages } from './config.js';
import { buildCalendar } from './calendar.js';

export async function loadEvents() {
    try {
        const events = await fetchJSON('/events');
        renderEvents(events);
        document.getElementById("sportFilter").value = "";
        document.getElementById("dateFilter").value = "";
        window.selectedDate = null;
        buildCalendar();
    } catch (e) {
        console.error(e);
    }
}

export async function loadEventsByDate(date) {
    try {
        const events = await fetchJSON(`/events/by-date/${date}`);
        renderEvents(events);
        document.getElementById('dateFilter').value = date;
        window.selectedDate = date;
        buildCalendar();
    } catch (e) {
        console.error(e);
        renderEvents([]);
    }
}

export async function filterEventsBySport(sportInput) {
    const foundSport = sports.find(s => s.name.toLowerCase() === sportInput.toLowerCase());
    const sportName = foundSport ? foundSport.name : sportInput;
    const events = await fetchJSON(`/events/by-sport/${encodeURIComponent(sportName)}`);
    if (events.length === 0 && !foundSport) {
        alert(`Sport "${sportInput}" not found. Please check the name.`);
    }
    return events;
}

export async function filterEventsByDate(date) {
    return await fetchJSON(`/events/by-date/${date}`);
}

export async function deleteEvent(id) {
    await fetchJSON(`/events/${id}`, { method: "DELETE" });
}

export async function updateEvent(id, data) {
    return await fetchJSON(`/events/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });
}

export async function updateEventStatus(id, status) {
    return await fetchJSON(`/events/${id}/status?status=${status}`, { method: "PATCH" });
}

export async function addEventResult(id, resultData) {
    return await fetchJSON(`/events/${id}/result`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(resultData)
    });
}

export async function createEvent(eventData) {
    return await fetchJSON('/events', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(eventData)
    });
}

export function renderEvents(events) {
    const container = document.getElementById("eventsContainer");
    if (!container) return;
    container.innerHTML = "";
    if (events.length === 0) {
        container.innerHTML = "<p>No events found.</p>";
        return;
    }

    events.forEach(e => {
        const sportName = e.sportName || (e.sport && e.sport.name) || "Unknown";
        const homeTeam = e.homeTeamName || (e.homeTeam && e.homeTeam.name) || "?";
        const awayTeam = e.awayTeamName || (e.awayTeam && e.awayTeam.name) || "?";
        const venueName = e.venueName || (e.venue && e.venue.name) || "Unknown";
        const season = e.season || "?";
        const competitionName = e.competitionName ? ` (${e.competitionName})` : "";
        const status = e.status || "SCHEDULED";

        const eventDiv = document.createElement("div");
        eventDiv.className = "event";
        eventDiv.innerHTML = `
            <div><strong>${sportName}${competitionName}</strong> | ${homeTeam} vs ${awayTeam}</div>
            <div>🏟 ${venueName} | Season: ${season}</div>
            <div>📅 ${e.eventDate} ${e.eventTime} <span class="status">${status}</span></div>
            <div class="event-buttons">
                <button onclick="window.deleteEventById(${e.id})" class="delete-btn">Delete</button>
                <button onclick="window.showEditModal(${e.id})" class="edit-btn">Edit</button>
                <button onclick="window.showStatusModal(${e.id}, '${status}')" class="status-btn">Status</button>
                <button onclick="window.showResultModal(${e.id})" class="result-btn">Result</button>
            </div>
        `;
        container.appendChild(eventDiv);
    });
}