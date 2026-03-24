import { getApiUrl } from './api.js';
import { loadAllData, createSport, createVenue, createStage, createTeam } from './entities.js';
import { loadEvents, filterEventsBySport, filterEventsByDate, createEvent, deleteEvent, renderEvents } from './events.js';
import { buildCalendar } from './calendar.js';
import { showEditModal, updateEventHandler, showStatusModal, updateStatusHandler, showResultModal, addResultHandler } from './modals.js';
import { closeModal, setMinDateTime } from './utils.js';
import { setEventsByDate, setSelectedDate, currentDate } from './config.js';

window.deleteEventById = async (id) => {
    if (!confirm("Delete this event?")) return;
    try {
        await deleteEvent(id);
        alert("Deleted");
        await window.refreshCalendarEvents();
        await loadEvents();
    } catch (err) {
        alert("Error: " + err.message);
    }
};

window.showEditModal = async (id) => {
    try {
        const event = await fetch(`${getApiUrl()}/events/${id}`).then(r => r.json());
        showEditModal(event);
    } catch (err) {
        alert("Error loading event: " + err.message);
    }
};

window.showStatusModal = showStatusModal;
window.showResultModal = showResultModal;
window.updateEvent = updateEventHandler;
window.updateStatus = updateStatusHandler;
window.addResult = addResultHandler;

window.applyFilters = async function() {
    const sportInput = document.getElementById("sportFilter").value.trim();
    const date = document.getElementById("dateFilter").value;
    try {
        let events;
        if (sportInput) {
            events = await filterEventsBySport(sportInput);
        } else if (date) {
            events = await filterEventsByDate(date);
            setSelectedDate(date);
            buildCalendar();
        } else {
            events = await fetch(`${getApiUrl()}/events`).then(r => r.json());
        }
        renderEvents(events);
    } catch (e) {
        console.error(e);
        alert("Filter error: " + e.message);
    }
};

window.loadEvents = loadEvents;
window.loadEventsByDate = async (date) => {
    try {
        const events = await filterEventsByDate(date);
        renderEvents(events);
        document.getElementById('dateFilter').value = date;
        setSelectedDate(date);
        buildCalendar();
    } catch (e) {
        console.error(e);
    }
};

window.refreshCalendarEvents = async () => {
    const events = await fetch(`${getApiUrl()}/events`).then(r => r.json());
    const map = {};
    events.forEach(ev => {
        const d = ev.eventDate;
        if (!map[d]) map[d] = [];
        map[d].push(ev);
    });
    setEventsByDate(map);
    buildCalendar();
};

async function init() {
    setMinDateTime();
    await loadAllData();
    await window.refreshCalendarEvents();
    await loadEvents();

    document.getElementById("eventForm").addEventListener("submit", async (e) => {
        e.preventDefault();
        const sport = document.getElementById("sport").value;
        const homeTeam = document.getElementById("homeTeam").value;
        const awayTeam = document.getElementById("awayTeam").value;
        const venue = document.getElementById("venue").value;
        const season = parseInt(document.getElementById("season").value, 10);
        const competitionName = document.getElementById("competitionName").value.trim();
        const description = document.getElementById("description").value.trim();
        const dateTime = document.getElementById("dateTime").value;

        if (!sport || !homeTeam || !awayTeam || !venue || !season || !dateTime) {
            alert("Fill all required fields (Sport, Home Team, Away Team, Venue, Season, Date)");
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

        console.log("Sending event:", body);

        try {
            await createEvent(body);
            alert("Event created");
            document.getElementById("eventForm").reset();
            await window.refreshCalendarEvents();
            await loadEvents();
        } catch (err) {
            alert("Error: " + err.message);
        }
    });

    document.getElementById("sportForm").addEventListener("submit", async (e) => {
        e.preventDefault();
        const name = document.getElementById("sportName").value.trim();
        if (!name) return;
        await createSport(name);
        alert("Sport added");
        await loadAllData();
        document.getElementById("sportName").value = "";
    });
    document.getElementById("venueForm").addEventListener("submit", async (e) => {
        e.preventDefault();
        const name = document.getElementById("venueName").value.trim();
        const city = document.getElementById("venueCity").value.trim();
        const country = document.getElementById("venueCountry").value.trim();
        const cap = parseInt(document.getElementById("venueCapacity").value, 10);
        if (!name || !city || !country || isNaN(cap)) return alert("Fill all fields");
        await createVenue(name, city, country, cap);
        alert("Venue added");
        await loadAllData();
        document.getElementById("venueForm").reset();
    });
    document.getElementById("stageForm").addEventListener("submit", async (e) => {
        e.preventDefault();
        const name = document.getElementById("stageName").value.trim();
        const ord = parseInt(document.getElementById("stageOrdering").value, 10);
        if (!name || isNaN(ord)) return;
        await createStage(name, ord);
        alert("Stage added");
        await loadAllData();
        document.getElementById("stageForm").reset();
    });
    document.getElementById("teamForm").addEventListener("submit", async (e) => {
        e.preventDefault();
        const name = document.getElementById("teamName").value.trim();
        const slug = document.getElementById("teamSlug").value.trim();
        const abbr = document.getElementById("teamAbbreviation").value.trim();
        const cc = document.getElementById("teamCountryCode").value.trim();
        if (!name || !slug || !abbr || !cc) return alert("All fields required");
        await createTeam(name, slug, abbr, cc);
        alert("Team added");
        await loadAllData();
        document.getElementById("teamForm").reset();
    });

    document.getElementById("prevMonth").addEventListener("click", async () => {
        currentDate.setMonth(currentDate.getMonth() - 1);
        await window.refreshCalendarEvents();
    });
    document.getElementById("nextMonth").addEventListener("click", async () => {
        currentDate.setMonth(currentDate.getMonth() + 1);
        await window.refreshCalendarEvents();
    });

    document.querySelectorAll(".close").forEach(btn => {
        btn.onclick = function() { closeModal(this.closest(".modal").id); };
    });
    window.onclick = function(e) {
        if (e.target.classList.contains("modal")) closeModal(e.target.id);
    };
}

init();