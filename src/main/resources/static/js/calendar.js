import { currentDate, eventsByDate, selectedDate, setSelectedDate } from './config.js';

export function buildCalendar() {
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const startWeekday = firstDay.getDay();
    const daysInMonth = lastDay.getDate();

    const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'];
    document.getElementById('monthYear').innerText = `${monthNames[month]} ${year}`;

    const grid = document.getElementById('calendarGrid');
    grid.innerHTML = '';

    const weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    weekdays.forEach(day => {
        const div = document.createElement('div');
        div.className = 'calendar-weekday';
        div.innerText = day;
        grid.appendChild(div);
    });

    for (let i = 0; i < startWeekday; i++) {
        const empty = document.createElement('div');
        empty.className = 'calendar-day';
        empty.style.backgroundColor = '#f1f5f9';
        grid.appendChild(empty);
    }

    for (let d = 1; d <= daysInMonth; d++) {
        const dateStr = `${year}-${String(month+1).padStart(2,'0')}-${String(d).padStart(2,'0')}`;
        const div = document.createElement('div');
        div.className = 'calendar-day';
        if (eventsByDate[dateStr] && eventsByDate[dateStr].length > 0) {
            div.classList.add('has-event');
        }
        if (selectedDate === dateStr) {
            div.classList.add('selected');
        }
        div.innerText = d;
        div.onclick = () => {
            setSelectedDate(dateStr);
            if (window.loadEventsByDate) window.loadEventsByDate(dateStr);
            buildCalendar();
        };
        grid.appendChild(div);
    }
}