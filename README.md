# RAG Chat Storage

A production-ready backend microservice in **Java Spring Boot** to store chat histories and sessions for a Retrieval-Augmented Generation (RAG) chatbot.

## Features

- Create/manage chat **sessions** (rename, mark favorite, delete)
- Store **messages** per session (sender, content, optional RAG **context** JSON)
- **API key** authentication via `x-api-key` header (from env)
- **Rate limiting** with Redis (fallback in-memory)
- Centralized **logging** & global error handling
- **Dockerized** app + PostgreSQL + Redis + **pgAdmin**
- **Swagger/OpenAPI** docs (springdoc)
- **Health checks**: `/healthz`, `/readyz` (+ Actuator)
- **CORS** configurable via env
- **Pagination** on message retrieval
- Basic **unit tests** with JUnit 5

## Quick Start (Docker Compose)

1. Create `.env` from example:
   ```bash
   cp .env.example .env
   # edit .env as needed
   ```
2. Build & run:
   ```bash
   docker compose up --build
   ```
3. Visit:
   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
   - Actuator: `http://localhost:8080/actuator/health`

## Run locally (without Docker)

```bash
# Requires Java 17+ and Maven
mvn clean package
export API_KEY=changeme-super-secret-key
export DATABASE_URL=jdbc:postgresql://localhost:5432/rag_chat
export POSTGRES_USER=postgres
export POSTGRES_PASSWORD=Pass@123
java -jar target/rag-chat-storage-svc-1.0.0.jar
```

## API Overview

All requests must include header: `x-api-key: <API_KEY>`

### Sessions
- **Create**: `POST /api/sessions`
  - Body: `{ "userId": "u123", "title": "Optional Title" }`
- **List**: `GET /api/sessions?userId=u123&page=0&size=20`
- **Get**: `GET /api/sessions/{sessionId}`
- **Rename**: `PATCH /api/sessions/{sessionId}/rename`
  - Body: `{ "title": "New Title" }`
- **Favorite/Unfavorite**: `PATCH /api/sessions/{sessionId}/favorite`
  - Body: `{ "isFavorite": true }`
- **Delete**: `DELETE /api/sessions/{sessionId}`

### Messages
- **Add**: `POST /api/sessions/{sessionId}/messages`
  - Body: `{ "sender": "user|assistant|system", "content": "...", "context": { ... } }`
- **List**: `GET /api/sessions/{sessionId}/messages?page=0&size=50&order=asc|desc`
  - Response: `{ items: [...], page, pageSize, total }`

## Notes

- Schema auto-updates (`hibernate.ddl-auto=update`) for demo; use Flyway/Liquibase in production.
- Rate limiting uses Redis if available; otherwise falls back to in-memory (per-instance).

