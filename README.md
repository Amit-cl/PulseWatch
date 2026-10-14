# PulseWatch — API Reliability & Incident Management Platform

## What is PulseWatch?
PulseWatch monitors your HTTP APIs, detects outages, tracks incidents, and sends notifications when things break or recover.

## Tech Stack
| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.3 |
| Database | PostgreSQL |
| Migrations | Flyway |
| Auth | JWT (jjwt) |
| Build | Maven |
| Testing | JUnit 5, Mockito, Testcontainers |

## Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 15+

## Database Setup
```sql
CREATE DATABASE pulsewatch;
```

## Run the Application
```bash
mvn spring-boot:run
```
The app starts on `http://localhost:8080`.

## Verify It Works
```bash
curl http://localhost:8080/
```
Expected response:
```json
{
  "application": "PulseWatch",
  "status": "UP",
  "timestamp": "2024-01-15T10:30:00"
}
```

## Project Structure
```
com.pulsewatch
├── PulseWatchApplication.java      # Entry point
├── config/                          # Security, properties, health
├── common/exception/                # Global error handling
├── auth/                            # User entity & repository
├── monitor/                         # Monitor entity & repository
├── check/                           # CheckResult entity & repository
├── incident/                        # Incident entity & repository
├── notification/                    # NotificationLog entity & repository
└── analytics/                       # (coming in Step 07)
```

## Implementation Progress
- [x] Step 01 — Project Setup & Architecture
- [ ] Step 02 — Monitor Management APIs
- [ ] Step 03 — Automatic API Health Checks
- [ ] Step 04 — Incident Detection & Recovery
- [ ] Step 05 — Authentication & Security
- [ ] Step 06 — Metrics & Analytics
- [ ] Step 07 — Email Notifications
- [ ] Step 08 — Testing & Reliability
- [ ] Step 09 — React Dashboard
- [ ] Step 10 — Docker, Deployment & Documentation
