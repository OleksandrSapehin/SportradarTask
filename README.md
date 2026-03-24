# ⚽ Event Calendar

## 📌 Overview
**Event Manager** is a full-stack web application for managing sports events.  
Users can create/manage events, organize teams/venues/stages, view events in a calendar, filter by date/sport, and track results.  
Built with **Spring Boot**, REST APIs, and Vanilla JS frontend.

---

## 🏗 Architecture
- **Controller Layer** – HTTP requests
- **Service Layer** – Business logic
- **DTO Layer** – API input/output
- **Frontend** – HTML, CSS, JS

---

## 🚀 Tech Stack
**Backend:** Java 21, Spring Boot, Spring MVC, Validation, Spring Data JPA, Hibernate, PostgreSQL  
**Frontend:** HTML5, CSS3, JavaScript  
**Testing:** JUnit 5, Mockito

---

## ⚙️ How to Run

git clone https://github.com/your-username/event-manager.git
cd event-manager
# configure database
mvn clean install
mvn spring-boot:run

Access: http://localhost:8080

Swagger: http://localhost:8080/swagger-ui.html

 ## Database Entities
- Sport – Football, Basketball, etc.
- Team – name, slug, abbreviation, country
- Venue – name, city, country, capacity
- Stage – Group Stage, Quarter-final, etc.
- Event – date, time, season, status, results

  Relationships:
  Event → Sport, Team (home/away), Venue, Stage (Many-to-One)
  Event → Result (One-to-One)

🔗 API Endpoints

Sports:
GET /api/sports, POST /api/sports, GET/PUT/DELETE /api/sports/{id}

Teams:
GET /api/teams, POST /api/teams/create, GET/PUT/DELETE /api/teams/{id}, GET /api/teams/by-slug/{slug}

Venues:
GET /api/venues, POST /api/venues, GET/PUT/DELETE /api/venues/{id}

Stages:
GET /api/stages, POST /api/stages, GET/PUT/DELETE /api/stages/{id}

Events:
GET /api/events, POST /api/events, GET/PUT/DELETE /api/events/{id}
GET /api/events/by-date/{date}, /by-sport/{sportName}, /by-status/{status}, /by-team/{teamId}, /upcoming
PATCH /api/events/{id}/status, PUT /api/events/{id}/result




 