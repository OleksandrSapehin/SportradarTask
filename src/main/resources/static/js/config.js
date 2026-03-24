export let sports = [];
export let teams = [];
export let venues = [];
export let stages = [];

export function setSports(data) { sports = data; }
export function setTeams(data) { teams = data; }
export function setVenues(data) { venues = data; }
export function setStages(data) { stages = data; }

export let currentDate = new Date();
export let eventsByDate = {};
export let selectedDate = null;

export function setEventsByDate(data) { eventsByDate = data; }
export function setSelectedDate(date) { selectedDate = date; }