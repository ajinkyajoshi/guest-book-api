# Guest Book API

Spring Boot 3.2 REST API for the Guestbook application.

## Tech Stack

- Java 17
- Spring Boot 3.2.4
- Spring Data JPA
- Spring Validation
- MySQL Connector/J
- Maven

## Project Structure

```
guest-book-api/
├── src/main/java/com/guestbook/
│   ├── GuestBookApplication.java          # Entry point
│   ├── controller/
│   │   └── GuestEntryController.java      # REST endpoints
│   ├── model/
│   │   ├── GuestEntry.java                # JPA entity
│   │   └── GuestBookStats.java            # Stats DTO
│   ├── repository/
│   │   └── GuestEntryRepository.java      # Data access layer
│   ├── service/
│   │   └── GuestEntryService.java         # Business logic
│   └── exception/
│       └── GlobalExceptionHandler.java    # Error handling
├── src/main/resources/
│   └── application.properties             # App configuration
├── Dockerfile
├── pom.xml
└── .dockerignore
```

## API Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/entries?page=0&size=10` | List entries (paginated, pinned first) | — |
| GET | `/api/entries?search=keyword` | Search by name or message | — |
| GET | `/api/entries/stats` | Get total entries, today's count, total likes | — |
| POST | `/api/entries` | Create a new entry | `{ name, message, email?, mood? }` |
| PUT | `/api/entries/{id}` | Update an entry | `{ name, message, email?, mood? }` |
| PATCH | `/api/entries/{id}/like` | Increment like count | — |
| PATCH | `/api/entries/{id}/pin` | Toggle pinned status | — |
| DELETE | `/api/entries/{id}` | Delete an entry | — |

## Data Model

| Field | Type | Constraints |
|-------|------|-------------|
| id | Long | Auto-generated |
| name | String | Required, max 100 chars |
| email | String | Optional, valid email, max 150 chars |
| message | String | Required, max 500 chars |
| mood | String | Default `😊`, max 10 chars |
| likes | int | Default 0 |
| pinned | boolean | Default false |
| createdAt | LocalDateTime | Auto-set on create |
| updatedAt | LocalDateTime | Auto-set on create/update |

## Sample Responses

### GET /api/entries
```json
{
  "content": [
    {
      "id": 1,
      "name": "Admin",
      "email": "admin@guestbook.app",
      "message": "Welcome to the Guestbook!",
      "mood": "👋",
      "likes": 5,
      "pinned": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "totalElements": 3,
  "totalPages": 1,
  "number": 0,
  "size": 10,
  "first": true,
  "last": true
}
```

### GET /api/entries/stats
```json
{
  "totalEntries": 3,
  "todayEntries": 1,
  "totalLikes": 9
}
```

### Error Response (Validation)
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "errors": ["name: must not be blank", "message: must not be blank"]
}
```

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `MYSQL_HOST` | localhost | MySQL hostname |
| `MYSQL_PORT` | 3306 | MySQL port |
| `MYSQL_DB` | guestbook | Database name |
| `MYSQL_USER` | guestuser | Database username |
| `MYSQL_PASSWORD` | guestpass | Database password |

## Run Locally

### Prerequisites
- Java 17+
- Maven 3.9+
- MySQL 8.0 running on localhost:3306

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/guest-book-api-1.0.0.jar

# Or with Maven
mvn spring-boot:run
```

API will be available at http://localhost:8080

## Docker

```bash
# Build image
docker build -t guestbook-api .

# Run container
docker run -d --name guestbook-api \
  -p 8080:8080 \
  -e MYSQL_HOST=host.docker.internal \
  -e MYSQL_PORT=3306 \
  -e MYSQL_DB=guestbook \
  -e MYSQL_USER=guestuser \
  -e MYSQL_PASSWORD=guestpass \
  guestbook-api
```

The Dockerfile uses a multi-stage build:
1. **Build stage** — `maven:3.9-eclipse-temurin-17` compiles the JAR
2. **Runtime stage** — `eclipse-temurin:17-jre-alpine` runs the app (~200MB image)

## Kubernetes Deployment

Deployed in the `guestbook-api` namespace. See `k8s/api/` for manifests.

```bash
kubectl apply -f ../k8s/api/
```

Cross-namespace MySQL connection via:
```
mysql.guestbook-db.svc.cluster.local:3306
```
