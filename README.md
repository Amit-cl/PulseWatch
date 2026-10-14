# PulseWatch ⏱️
> **Production-Grade API Reliability, Incident Detection & Uptime Monitoring Platform**

[![Java](https://img.shields.io/badge/Java-17-orange.svg?style=flat&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg?style=flat&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg?style=flat&logo=postgresql)](https://www.postgresql.org/)
[![Flyway](https://img.shields.io/badge/Flyway-Migrations-red.svg?style=flat&logo=flyway)](https://flywaydb.org/)
[![JWT](https://img.shields.io/badge/Auth-JWT%20(jjwt)-black.svg?style=flat&logo=jsonwebtokens)](https://jwt.io/)

---

## 📌 Executive Summary (The "Elevator Pitch")

**PulseWatch** is a backend monitoring system engineered to continuously verify HTTP/S endpoint health, detect systemic outages, compute latency percentiles ($p50$, $p95$), automatically manage incident lifecycles, and deliver instant alert notifications.

Unlike toy CRUD apps, PulseWatch tackles real-world distributed systems and backend challenges:
1. **Network Reliability & Bound Execution:** Safe HTTP worker execution with configurable connect/read timeouts.
2. **Concurrency Safety:** Bounded thread pool scheduling to prevent thread starvation and duplicate simultaneous checks.
3. **Database-Enforced State Machine:** PostgreSQL partial unique indexes to guarantee **at most one open incident** per monitor, eliminating race-condition alert storms.
4. **Security & SSRF Mitigation:** Strict destination validation guarding against Server-Side Request Forgery (SSRF) and private cloud IP probing (loopback, 10.x, 192.168.x, 169.254.x).

---

## 🏗️ Architecture & Component Flow

```
   ┌─────────────────────────────────────────────────────────────┐
   │                     PulseWatch System                       │
   └─────────────────────────────────────────────────────────────┘
                                  │
      ┌───────────────────────────┼───────────────────────────┐
      ▼                           ▼                           ▼
┌──────────────┐           ┌──────────────┐           ┌──────────────┐
│  Auth & JWT  │           │   Monitor    │           │  Analytics   │
│  Validation  │           │  Management  │           │ & Percentiles│
└──────────────┘           └──────────────┘           └──────────────┘
      │                           │                           │
      │                     ┌─────┴─────┐                     │
      │                     ▼           ▼                     │
      │               Scheduled     Manual API                │
      │                 Worker        Trigger                 │
      │                     └─────┬─────┘                     │
      ▼                           ▼                           ▼
┌─────────────────────────────────────────────────────────────┐
│                 Health Check Worker Pool                    │
│   - SSRF Protection / Private IP Blocking                  │
│   - HttpClient with Bounded Connect/Read Timeouts           │
│   - Latency Measurement & Classification (TIMEOUT/DNS/HTTP) │
└─────────────────────────────┬───────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  Incident Detection Engine                  │
│   - N consecutive failures  → OPEN incident                 │
│   - M consecutive successes → RESOLVE incident              │
│   - Deduplication: DB partial unique index                  │
└─────────────────────────────┬───────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     Notification Engine                     │
│   - Outage & Recovery Email Dispatches                      │
│   - Deduplication: Unique constraint (incident_id, event)   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      PostgreSQL DB                          │
│     users · monitors · check_results · incidents · logs     │
└─────────────────────────────────────────────────────────────┘
```

---

## 🗺️ 10-Step Comprehensive Implementation Plan

| Step | Milestone | Goal & Focus | Status |
| :---: | :--- | :--- | :---: |
| **01** | **Project Setup & Architecture** | Java 17, Spring Boot 3.3, Flyway SQL (V1–V5), Entities, Repositories, Custom Exceptions | ✅ **Completed** |
| **02** | **Monitor CRUD APIs** | REST endpoints (`POST`, `GET`, `PATCH`, `DELETE`, `/pause`, `/resume`), DTOs, Bean Validation | ✅ **Completed** |
| **03** | **Health Check Engine** | High-performance HTTP client, connection/socket timeouts, latency measurement, error classification | ⏳ *Next Up* |
| **04** | **Background Scheduler & Concurrency** | Non-overlapping execution, bounded thread pool, skip paused monitors | 📋 *Queued* |
| **05** | **Incident State Machine** | Consecutive failure/success triggers, auto-transition `OPEN` ↔ `RESOLVED`, DB deduplication | 📋 *Queued* |
| **06** | **Security, JWT & SSRF Defense** | BCrypt hashing, JWT filter, endpoint ownership check, DNS/IP blacklisting (SSRF guard) | 📋 *Queued* |
| **07** | **Analytics & Latency Percentiles** | Calculate uptime %, error breakdown, $p50$, $p90$, $p95$ response times across time windows | 📋 *Queued* |
| **08** | **Notification Engine** | JavaMailSender, alert dispatch for outages & recovery, retry counter, deduplication logs | 📋 *Queued* |
| **09** | **Testing & Failure Simulation** | JUnit 5 + Mockito unit tests, Testcontainers PostgreSQL integration tests, mock server timeouts | 📋 *Queued* |
| **10** | **Docker & Production Packaging** | Multi-stage Dockerfile, `docker-compose.yml` (App + Postgres), API documentation, React UI | 📋 *Queued* |

---

## 💾 Database Design (PostgreSQL + Flyway)

PulseWatch relies on **Flyway database migrations** to ensure repeatable, deterministic database evolution.

```
src/main/resources/db/migration/
├── V1__create_users_table.sql
├── V2__create_monitors_table.sql
├── V3__create_check_results_table.sql
├── V4__create_incidents_table.sql
└── V5__create_notification_logs_table.sql
```

### Table Details & Technical Rationales

1. **`users`**
   * Stores credentials with BCrypt hashes (`password_hash`).
   * Unique constraint on `email`.

2. **`monitors`**
   * Configurable check interval (`interval_seconds >= 10`), timeout (`1000ms <= timeout_ms <= 60000ms`).
   * Configurable thresholds (`consecutive_failures_threshold`, `consecutive_successes_threshold`).
   * Flag `enabled` for zero-loss pause/resume. Partial index on `WHERE enabled = TRUE` for quick polling.

3. **`check_results`**
   * Captures every ping attempt (`checked_at`, `success`, `status_code`, `latency_ms`, `error_type`).
   * Composite index `(monitor_id, checked_at DESC)` ensures lightning-fast queries for recent history and percentile calculations.
   * *Zero sensitive response bodies stored* to prevent leaking confidential API payloads.

4. **`incidents`**
   * Captures outage windows: `started_at`, `resolved_at`, `status` (`OPEN` / `RESOLVED`), `failure_reason`.
   * **Key Engineering Highlight:**
     ```sql
     CREATE UNIQUE INDEX uq_incidents_monitor_open
         ON incidents (monitor_id) WHERE status = 'OPEN';
     ```
     *Enforces that no race condition or concurrent scheduler thread can open multiple simultaneous incidents for the same target.*

5. **`notification_logs`**
   * Tracks alert dispatches (`OUTAGE`, `RECOVERY`) via `EMAIL`.
   * Unique constraint `(incident_id, event_type)` ensures users are never spammed twice for the exact same event.

---

## 📡 REST API Reference

Base Path: `/api/v1`

### 1. Monitor Management (`/api/v1/monitors`)

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :---: |
| `POST` | `/api/v1/monitors` | Register a new target URL for health checking | `201 Created` |
| `GET` | `/api/v1/monitors` | List all monitors for the authenticated user | `200 OK` |
| `GET` | `/api/v1/monitors/{id}` | Retrieve details for a specific monitor | `200 OK` |
| `PATCH` | `/api/v1/monitors/{id}` | Update partial configurations (interval, timeout, etc.) | `200 OK` |
| `DELETE`| `/api/v1/monitors/{id}` | Remove monitor & cascade delete check logs | `204 No Content` |
| `POST` | `/api/v1/monitors/{id}/pause` | Pause checks without deleting historical data | `200 OK` |
| `POST` | `/api/v1/monitors/{id}/resume`| Resume active checks | `200 OK` |

#### Sample: Create Monitor Request
```json
POST /api/v1/monitors
Content-Type: application/json

{
  "name": "Payments Production API",
  "url": "https://api.payments.example.com/health",
  "intervalSeconds": 30,
  "timeoutMs": 5000,
  "expectedStatusCode": 200,
  "consecutiveFailuresThreshold": 3,
  "consecutiveSuccessesThreshold": 2
}
```

#### Sample Response:
```json
{
  "id": 1,
  "name": "Payments Production API",
  "url": "https://api.payments.example.com/health",
  "intervalSeconds": 30,
  "timeoutMs": 5000,
  "expectedStatusCode": 200,
  "consecutiveFailuresThreshold": 3,
  "consecutiveSuccessesThreshold": 2,
  "enabled": true,
  "createdAt": "2026-10-14T15:20:00"
}
```

---

## 💼 3-Month Intern Interview Cheat Sheet

When discussing PulseWatch in technical interviews, focus on **engineering trade-offs**, not just "how I used Spring Boot annotations."

### Q1: "Why did you use Flyway instead of `spring.jpa.hibernate.ddl-auto=update`?"
> **Answer:** "`ddl-auto=update` is dangerous in production because it can make silent, irreversible schema modifications and cannot handle data migrations or complex constraints like partial indexes. Flyway treats database migrations as versioned code (`V1`, `V2`, etc.), ensuring every environment—local, testing, and production—has the exact same schema."

### Q2: "How do you prevent duplicate incidents if multiple health-check threads fail at once?"
> **Answer:** "I implemented a two-tier defense:
> 1. **Application Level:** The incident service checks for an existing `OPEN` incident before creating a new one.
> 2. **Database Level:** PostgreSQL enforces a partial unique index:
>    `CREATE UNIQUE INDEX uq_incidents_monitor_open ON incidents (monitor_id) WHERE status = 'OPEN';`
>    Even under high concurrency or distributed workers, the database rejects any secondary insert with a constraint violation."

### Q3: "What happens if a monitored endpoint hangs indefinitely?"
> **Answer:** "PulseWatch uses custom HTTP client configurations with strict **connect timeouts** and **socket read timeouts** (e.g., 5 seconds). A slow target will never exhaust the application's thread pool. When the timeout expires, the check records a `TIMEOUT` failure and frees the worker thread."

### Q4: "What is SSRF and how does PulseWatch protect against it?"
> **Answer:** "Server-Side Request Forgery happens when a user asks our server to request an internal IP (like `http://localhost:8080`, `http://192.168.1.1`, or AWS metadata `http://169.254.169.254`). PulseWatch resolves the domain via DNS before dispatching the request and blocks loopback, private, and link-local IP addresses."

### Q5: "Why did you choose a Modular Monolith instead of Microservices?"
> **Answer:** "At this scale, a modular monolith eliminates unnecessary network latency, serialization overhead, and distributed transaction complexity while keeping domain boundaries clear (`monitor`, `check`, `incident`, `notification`). It can easily be extracted into microservices later if scaling demands it."

---

## 🚀 Getting Started Locally

### Prerequisites
* **Java 17** or higher
* **Maven 3.8+**
* **PostgreSQL 15+** (or Docker)

### 1. Database Setup
```bash
# Connect to PostgreSQL and create database
psql -U postgres -c "CREATE DATABASE pulsewatch;"
```

### 2. Configure Environment (Optional)
Defaults in `application.yml`:
* `DB_HOST`: `localhost`
* `DB_PORT`: `5432`
* `DB_NAME`: `pulsewatch`
* `DB_USERNAME`: `postgres`
* `DB_PASSWORD`: `postgres`

### 3. Build & Run
```bash
# Clone the repository
git clone https://github.com/Amit-cl/PulseWatch.git
cd PulseWatch

# Compile and run
mvn clean spring-boot:run
```

### 4. Verify Health
```bash
curl http://localhost:8080/
```
Expected response:
```json
{
  "application": "PulseWatch",
  "status": "UP",
  "timestamp": "2026-10-14T15:25:00"
}
```

---

## 🛠️ Tech Stack & Dependencies

* **Core Framework:** Spring Boot 3.3.5 (Web, Data JPA, Security, Actuator, Validation, Mail)
* **Language:** Java 17 (Records, Pattern Matching, Stream API)
* **Database & Migrations:** PostgreSQL, Flyway Core
* **Security & Token:** Spring Security, BCrypt, JJWT 0.12.6
* **Testing:** JUnit 5, Mockito, AssertJ, H2 (unit), Testcontainers (integration)

---

## 👤 Author
* **Amit Kumar** ([@Amit-cl](https://github.com/Amit-cl))
* GitHub: [https://github.com/Amit-cl/PulseWatch](https://github.com/Amit-cl/PulseWatch)
